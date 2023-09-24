package plate.back.service;

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

import lombok.RequiredArgsConstructor;
import plate.back.dto.HistoryDto;
import plate.back.dto.ImageDto;
import plate.back.dto.LogDto;
import plate.back.dto.PredictDto;
import plate.back.dto.ResponseDto;
import plate.back.entity.HistoryEntity;
import plate.back.entity.ImageEntity;
import plate.back.entity.LogEntity;
import plate.back.entity.PredictLogEntity;
import plate.back.persistence.CarInfoRepository;
import plate.back.persistence.HistoryRepository;
import plate.back.persistence.ImageRepository;
import plate.back.persistence.LogRepository;
import plate.back.persistence.PredictLogRepository;

@RequiredArgsConstructor
@Service
public class LogService {
    private final CarInfoRepository carRepo;
    private final LogRepository logRepo;
    private final ImageRepository imgRepo;
    private final PredictLogRepository predRepo;
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
            LogEntity savedLog = logRepo.save(LogEntity.builder()
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy(0.0)
                    .state("수정 필요")
                    .build());
            // 현장 이미지만 업로드
            String[] vehicleImgArr = fileService.uploadFile(file, 0);
            String vehicleImgUrl = vehicleImgArr[0];
            String vehicleImgTitle = vehicleImgArr[1];
            ImageEntity vehicleImg = imgRepo.save(ImageEntity.builder()
                    .logEntity(savedLog)
                    .imageUrl(vehicleImgUrl)
                    .imageType("vehicle")
                    .imageTitle(vehicleImgTitle).build());

            LogDto dto = LogDto.builder()
                    .logId(savedLog.getLogId())
                    .vehicleImage(vehicleImg.getImageUrl())
                    .plateImage("인식 실패")
                    .state(savedLog.getState())
                    .date(savedLog.getDate())
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy("인식 실패")
                    .build();
            dtos[0] = dto;
            return response.fail(dtos, (String) flaskResponse.get("error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LinkedHashMap<String, ArrayList<Object>> predictValue = (LinkedHashMap<String, ArrayList<Object>>) flaskResponse
                .get("predictedResults");

        ArrayList<PredictDto> predList = new ArrayList<>();
        for (String key : predictValue.keySet()) {
            List<Object> list = predictValue.get(key);
            if (list == null) {
                predList.add(new PredictDto(key, "예측 실패", 0));
            } else {
                predList.add(new PredictDto(key, String.valueOf(list.get(0)), (double) list.get(1)));
            }
        }

        int isPresent = 0;
        for (int i = 0; i < predList.size(); i++) {
            PredictDto dto = predList.get(i);
            if (carRepo.findByLicensePlate(String.valueOf(dto.getPredictedText())).isPresent()) {
                dto.setIsPresent(true);
                isPresent++;
            }
        }

        double maxVal = 0;
        int idx = 0;
        for (int i = 0; i < predList.size(); i++) {
            PredictDto dto = predList.get(i);
            if (maxVal < (double) dto.getAccuracy()) {
                maxVal = dto.getAccuracy();
                idx = i;
            }
        }

        // Log 엔티티 저장
        LogEntity savedLog = logRepo.save(LogEntity.builder()
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

        ImageEntity vehicleImg = imgRepo.save(ImageEntity.builder()
                .logEntity(savedLog)
                .imageUrl(vehicleImgUrl)
                .imageType("vehicle")
                .imageTitle(vehicleImgTitle).build());

        ImageEntity plateImg = imgRepo.save(ImageEntity.builder()
                .logEntity(savedLog)
                .imageUrl(plateImgUrl)
                .imageType("plate")
                .imageTitle(plateImgTitle).build());

        // PredictLog 엔티티 저장
        for (int i = 0; i < predList.size(); i++) {
            PredictDto dto = predList.get(i);
            dto.setLogId(savedLog.getLogId());
            PredictLogEntity predEntity = predRepo.save(PredictLogEntity.builder()
                    .logEntity(savedLog)
                    .modelType(dto.getModelType())
                    .isPresent(dto.isPresent())
                    .accuracy(dto.getAccuracy())
                    .predictedText(dto.getPredictedText()).build());
        }

        // logDto 구성
        LogDto dto = LogDto.builder()
                .logId(savedLog.getLogId())
                .vehicleImage(vehicleImg.getImageUrl())
                .plateImage(plateImg.getImageUrl())
                .state(savedLog.getState())
                .date(savedLog.getDate())
                .modelType(savedLog.getModelType())
                .licensePlate(savedLog.getLicensePlate())
                .accuracy(savedLog.getAccuracy() == 0.0 ? "-" : String.valueOf(savedLog.getAccuracy()))
                .build();

        dtos[0] = dto;
        dtos[1] = predList;
        dtos[2] = ImageDto.builder()
                .logId(savedLog.getLogId())
                .imageType("plate")
                .imageUrl(plateImgUrl).build();
        return response.success(dtos);
    }

    // LogEntity & ImageEntity -> LogDto
    private List<LogDto> createLogDto(List<LogEntity> logEntities) {
        List<LogDto> list = new ArrayList<>();
        for (LogEntity logEntity : logEntities) {
            List<ImageEntity> imgEntities = imgRepo.findByLogId(logEntity.getLogId());
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
            list.add(LogDto.builder()
                    .logId(logEntity.getLogId())
                    .modelType(logEntity.getModelType())
                    .vehicleImage(vehicleImg)
                    .plateImage(plateImg)
                    .state(logEntity.getState())
                    .date(logEntity.getDate())
                    .licensePlate(logEntity.getLicensePlate())
                    .accuracy(logEntity.getAccuracy() == 0.0 ? "-" : String.valueOf(logEntity.getAccuracy()))
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
        List<LogEntity> logEntities = logRepo.findByDate(startDate, endDate);
        System.out.println("logEntities : " + logEntities);

        // LogEntity -> LogDto 변환
        List<LogDto> list = createLogDto(logEntities);

        return response.success(list);
    }

    // 5. 차량 번호별 로그 조회
    public ResponseEntity<?> searchPlate(String plate) {

        // 로그 조회
        List<LogEntity> logEntities = logRepo.findByPlate(plate);

        // LogEntity -> LogDto 변환
        List<LogDto> list = createLogDto(logEntities);

        return response.success(list);
    }

    // 6. 수정/삭제 기록 조회
    public ResponseEntity<?> getHistory() {
        List<HistoryEntity> entities = histRepo.findAll();
        List<HistoryDto> list = new ArrayList<>();
        for (HistoryEntity entity : entities) {
            list.add(HistoryDto.builder()
                    .id(entity.getId())
                    .logId(entity.getLogId())
                    .userId(entity.getUserId())
                    .workType(entity.getWorkType())
                    .currentText(entity.getCurrentText())
                    .previousText(entity.getPreviousText())
                    .date(entity.getDate())
                    .build());
        }
        return response.success(list);
    }

    // 7. 로그 수정(admin)
    public ResponseEntity<?> updateLog(ArrayList<LogDto> list) throws IOException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        for (LogDto dto : list) {
            Optional<LogEntity> optionalLog = logRepo.findById(dto.getLogId());
            if (!optionalLog.isPresent()) {
                return response.fail("존재하지 않는 기록입니다.", HttpStatus.BAD_REQUEST);
            }

            LogEntity entity = optionalLog.get();

            // history 기록
            histRepo.save(HistoryEntity.builder()
                    .logId(dto.getLogId())
                    .previousText(entity.getLicensePlate())
                    .currentText(dto.getLicensePlate())
                    .workType("update")
                    .userId(userId).build());

            // 수정된 log 기록
            entity.setLicensePlate(dto.getLicensePlate());
            entity.setState("수정 완료");

            logRepo.save(entity);

            dto.setState("수정 완료");
            // AWS S3 파일 이동
            try {
                List<ImageEntity> imgEntities = imgRepo.findByLogId(entity.getLogId());

                for (ImageEntity imageEntity : imgEntities) {
                    String answer = entity.getLicensePlate();
                    String imageTitle = imageEntity.getImageTitle();
                    String imageType = imageEntity.getImageType();

                    Map<String, String> map = fileService.moveFile(imageTitle, imageType, answer);

                    // AWS S3 파일 삭제
                    fileService.deleteFile(imageTitle, imageType);

                    imageEntity.setImageUrl(map.get("url"));
                    imageEntity.setImageTitle(map.get("title"));
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
    public ResponseEntity<?> deleteLog(ArrayList<LogDto> list) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            for (LogDto dto : list) {
                int logId = dto.getLogId();
                System.out.println("logId : " + logId);

                // AWS S3 파일 삭제
                List<ImageEntity> imgEntities = imgRepo.findByLogId(logId);
                for (ImageEntity imgEntity : imgEntities) {
                    String imageTitle = imgEntity.getImageTitle();
                    String imageType = imgEntity.getImageType();
                    fileService.deleteFile(imageTitle, imageType);
                }

                // log 삭제
                logRepo.deleteById(logId);

                // history 기록
                histRepo.save(HistoryEntity.builder()
                        .logId(dto.getLogId())
                        .previousText("delete")
                        .currentText("delete")
                        .workType("delete")
                        .userId(userId).build());
            }
            return response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }
}
