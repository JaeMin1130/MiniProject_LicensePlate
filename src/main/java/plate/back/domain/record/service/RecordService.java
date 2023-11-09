package plate.back.domain.record.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.AmazonS3Exception;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import plate.back.domain.car.repository.CarInfoRepository;
import plate.back.domain.history.dto.HistoryDto;
import plate.back.domain.history.entity.History;
import plate.back.domain.history.repository.HistoryRepository;
import plate.back.domain.image.dto.ImageDto;
import plate.back.domain.image.entity.Image;
import plate.back.domain.image.repository.ImageRepository;
import plate.back.domain.predictedPlate.dto.PredictedPlateDto;
import plate.back.domain.predictedPlate.entity.PredictedPlate;
import plate.back.domain.predictedPlate.repository.PredictedPlateRepository;
import plate.back.domain.record.dto.RecordDto;
import plate.back.domain.record.entity.Record;
import plate.back.domain.record.repository.RecordRepository;
import plate.back.global.aws.s3.service.FileService;
import plate.back.global.flask.FlaskService;
import plate.back.global.response.ResponseDto;

@Transactional
@RequiredArgsConstructor
@Service
public class RecordService {
    private final CarInfoRepository carRepo;
    private final RecordRepository recordRepo;
    private final ImageRepository imgRepo;
    private final PredictedPlateRepository predRepo;
    private final HistoryRepository histRepo;
    private final FileService fileService;
    private final FlaskService flaskService;
    private final ResponseDto response;

    // 3. 차량 출입 로그 기록
    public ResponseEntity<?> recordLog(MultipartFile file) throws IOException {
        Object[] dtos = new Object[3];
        // flask api 호출
        LinkedHashMap<String, Object> flaskResponse;
        try {
            flaskResponse = (LinkedHashMap<String, Object>) flaskService.callApi(file).getBody();
        } catch (java.lang.ClassCastException caseException) {
            return response.fail("Too many requests. Please try again later.", HttpStatus.TOO_MANY_REQUESTS);
        } catch (Exception e) {
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 번호판 인식 실패
        if ((int) flaskResponse.get("status") == 500) {
            Record savedLog = recordRepo.save(Record.builder()
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy(0.0)
                    .state("수정 필요")
                    .build());

            // 현장 이미지만 업로드
            String[] vehicleImgArr = fileService.uploadFile(file, 0);
            String vehicleImgUrl = vehicleImgArr[0];
            String vehicleImgTitle = vehicleImgArr[1];
            Image vehicleImg = imgRepo.save(Image.builder()
                    .record(savedLog)
                    .imageUrl(vehicleImgUrl)
                    .imageType("vehicle")
                    .imageTitle(vehicleImgTitle).build());

            RecordDto dto = RecordDto.builder()
                    .recordId(savedLog.getRecordId())
                    .vehicleImage(vehicleImg.getImageUrl())
                    .plateImage("인식 실패")
                    .state(savedLog.getState())
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy("인식 실패")
                    .build();
            dtos[0] = dto;
            return response.fail(dtos, (String) flaskResponse.get("error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LinkedHashMap<String, ArrayList<Object>> predictValue = (LinkedHashMap<String, ArrayList<Object>>) flaskResponse
                .get("predictedResults");

        ArrayList<PredictedPlateDto> predList = new ArrayList<>();
        for (String key : predictValue.keySet()) {
            List<Object> list = predictValue.get(key);
            if (list == null) {
                predList.add(new PredictedPlateDto(key, "예측 실패", 0));
            } else {
                predList.add(new PredictedPlateDto(key, String.valueOf(list.get(0)), (double) list.get(1)));
            }
        }

        int isPresent = 0;
        for (int i = 0; i < predList.size(); i++) {
            PredictedPlateDto dto = predList.get(i);
            if (carRepo.findByLicensePlate(String.valueOf(dto.getPredictedText())).isPresent()) {
                dto.setIsPresent(true);
                isPresent++;
            }
        }

        double maxVal = 0;
        int idx = 0;
        for (int i = 0; i < predList.size(); i++) {
            PredictedPlateDto dto = predList.get(i);
            if (maxVal < (double) dto.getAccuracy()) {
                maxVal = dto.getAccuracy();
                idx = i;
            }
        }

        // Log 엔티티 저장
        Record savedLog = recordRepo.save(Record.builder()
                .modelType(predList.get(idx).getModelType())
                .licensePlate(predList.get(idx).getPredictedText())
                .accuracy(maxVal)
                .state(isPresent >= 2 ? "수정 불필요" : "수정 필요")
                .build());

        // Image 엔티티 저장
        // 원본 file 업로드(Aws S3)
        String[] vehicleImgArr = fileService.uploadFile(file, 0);
        String vehicleImgUrl = vehicleImgArr[0];
        String vehicleImgTitle = vehicleImgArr[1];
        String plateImgUrl = String.valueOf(flaskResponse.get("plateImgUrl"));
        String plateImgTitle = String.valueOf(flaskResponse.get("plateImgTitle"));

        Image vehicleImg = imgRepo.save(Image.builder()
                .record(savedLog)
                .imageUrl(vehicleImgUrl)
                .imageType("vehicle")
                .imageTitle(vehicleImgTitle).build());

        Image plateImg = imgRepo.save(Image.builder()
                .record(savedLog)
                .imageUrl(plateImgUrl)
                .imageType("plate")
                .imageTitle(plateImgTitle).build());

        // PredictLog 엔티티 저장
        for (int i = 0; i < predList.size(); i++) {
            PredictedPlateDto dto = predList.get(i);
            dto.setLogId(savedLog.getRecordId());
            predRepo.save(PredictedPlate.builder()
                    .record(savedLog)
                    .modelType(dto.getModelType())
                    .isPresent(dto.isPresent())
                    .accuracy(dto.getAccuracy())
                    .predictedText(dto.getPredictedText()).build());
        }

        // logDto 구성
        RecordDto dto = RecordDto.builder()
                .recordId(savedLog.getRecordId())
                .vehicleImage(vehicleImg.getImageUrl())
                .plateImage(plateImg.getImageUrl())
                .state(savedLog.getState())
                .modelType(savedLog.getModelType())
                .licensePlate(savedLog.getLicensePlate())
                .accuracy(savedLog.getAccuracy() == 0.0 ? "-" : String.valueOf(savedLog.getAccuracy()))
                .build();

        dtos[0] = dto;
        dtos[1] = predList;
        dtos[2] = ImageDto.builder()
                .recordId(savedLog.getRecordId())
                .imageType("plate")
                .imageUrl(plateImgUrl).build();
        return response.success(dtos);
    }

    // LogEntity & ImageEntity -> LogDto
    private List<RecordDto> createLogDto(List<Record> logEntities) {
        List<RecordDto> list = new ArrayList<>();
        for (Record record : logEntities) {
            List<Image> imgEntities = imgRepo.findByRecord(record);
            String vehicleImg;
            String plateImg;
            if (imgEntities.size() == 2) {
                vehicleImg = imgEntities.get(0).getImageUrl();
                plateImg = imgEntities.get(1).getImageUrl();
            } else if (imgEntities.size() == 1) {
                vehicleImg = imgEntities.get(0).getImageUrl();
                plateImg = "인식 실패";
            } else {
                vehicleImg = "https://licenseplate-iru.s3.ap-northeast-2.amazonaws.com/sample/img10.jpg";
                plateImg = "https://licenseplate-iru.s3.ap-northeast-2.amazonaws.com/sample/3441e8bb-f992-4879-8360-c2c70488902e.jpg";
            }
            list.add(RecordDto.builder()
                    .recordId(record.getRecordId())
                    .modelType(record.getModelType())
                    .vehicleImage(vehicleImg)
                    .plateImage(plateImg)
                    .state(record.getState())
                    .licensePlate(record.getLicensePlate())
                    .accuracy(record.getAccuracy() == 0.0 ? "-" : String.valueOf(record.getAccuracy()))
                    .build());
        }
        return list;
    }

    // 4. 날짜별 로그 조회 yy-MM-dd
    public ResponseEntity<?> searchDate(String start, String end) throws ParseException {

        // String -> Date 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        System.out.printf("%s ~ %s", startDate, endDate);

        // 로그 조회
        List<Record> logEntities = recordRepo.findByCreateDateBetween(startDate, endDate);
        System.out.println("logEntities : " + logEntities);

        // LogEntity -> LogDto 변환
        List<RecordDto> list = createLogDto(logEntities);

        return response.success(list);
    }

    // 5. 차량 번호별 로그 조회
    public ResponseEntity<?> searchPlate(String plate) {

        // 로그 조회
        List<Record> logEntities = recordRepo.findByLicensePlate(plate);

        // LogEntity -> LogDto 변환
        List<RecordDto> list = createLogDto(logEntities);

        return response.success(list);
    }

    // 6. 수정/삭제 기록 조회
    public ResponseEntity<?> getHistory() {
        List<History> entities = histRepo.findAll();
        List<HistoryDto> list = new ArrayList<>();
        for (History entity : entities) {
            list.add(HistoryDto.builder()
                    .id(entity.getId())
                    .recordId(entity.getRecordId())
                    .memberId(entity.getMemberId())
                    .workType(entity.getTaskType())
                    .currentText(entity.getCurrentText())
                    .previousText(entity.getPreviousText())
                    .build());
        }
        return response.success(list);
    }

    // 7. 로그 수정(admin)
    public ResponseEntity<?> updateLog(ArrayList<RecordDto> list) throws IOException {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        for (RecordDto dto : list) {
            Optional<Record> optionalLog = recordRepo.findById(dto.getRecordId());
            if (!optionalLog.isPresent()) {
                return response.fail("존재하지 않는 기록입니다.", HttpStatus.BAD_REQUEST);
            }

            Record record = optionalLog.get();

            // history 기록
            histRepo.save(History.builder()
                    .recordId(record.getRecordId())
                    .previousText(record.getLicensePlate())
                    .currentText(dto.getLicensePlate())
                    .taskType("update")
                    .memberId(memberId).build());

            // 수정된 log 기록
            record.updateLicensePlate(dto.getLicensePlate(), "수정 완료");

            recordRepo.save(record);

            // AWS S3 파일 이동
            try {
                List<Image> imgEntities = imgRepo.findByRecord(record);

                for (Image imageEntity : imgEntities) {
                    String answer = record.getLicensePlate();
                    String imageTitle = imageEntity.getImageTitle();
                    String imageType = imageEntity.getImageType();

                    Map<String, String> map = fileService.moveFile(imageTitle, imageType, answer);

                    // AWS S3 파일 삭제
                    fileService.deleteFile(imageTitle, imageType);

                    imageEntity.updateImage(map.get("title"), map.get("url"));
                    imgRepo.save(imageEntity);
                }

            } catch (AmazonS3Exception s3Exception) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                return response.fail(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
        return response.success();
    }

    // 8. 로그 삭제(admin)
    public ResponseEntity<?> deleteLog(ArrayList<RecordDto> list) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            for (RecordDto dto : list) {
                int recordId = dto.getRecordId();

                // AWS S3 파일 삭제
                List<Image> imgEntities = imgRepo.findByRecord(recordRepo.findByRecordId(recordId).get());
                for (Image imgEntity : imgEntities) {
                    String imageTitle = imgEntity.getImageTitle();
                    String imageType = imgEntity.getImageType();
                    fileService.deleteFile(imageTitle, imageType);
                }

                // log 삭제
                recordRepo.deleteById(recordId);

                // history 기록
                histRepo.save(History.builder()
                        .recordId(dto.getRecordId())
                        .previousText("delete")
                        .currentText("delete")
                        .taskType("delete")
                        .memberId(memberId).build());
            }
            return response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }
}
