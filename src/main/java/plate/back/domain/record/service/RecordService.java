package plate.back.domain.record.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import plate.back.domain.car.repository.CarInfoRepository;
import plate.back.domain.history.dto.HistoryResponseDto;
import plate.back.domain.history.entity.History;
import plate.back.domain.history.repository.HistoryRepository;
import plate.back.domain.image.dto.ImageResponseDto;
import plate.back.domain.image.entity.Image;
import plate.back.domain.image.repository.ImageRepository;
import plate.back.domain.predictedPlate.dto.PredictedPlateDto;
import plate.back.domain.predictedPlate.entity.PredictedPlate;
import plate.back.domain.predictedPlate.repository.PredictedPlateRepository;
import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.domain.record.entity.Record;
import plate.back.domain.record.repository.RecordRepository;
import plate.back.global.flask.FlaskService;
import plate.back.global.flask.dto.FlaskResponseDto;
import plate.back.global.flask.dto.ModelPredictResult;
import plate.back.global.s3.service.FileService;

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

    // 3. 차량 출입 기록 기록
    public MultiResponseDto recordLog(MultipartFile file) throws IOException {

        // flask api 호출
        FlaskResponseDto flaskResponseDto = flaskService.callApi(file).getBody();

        // 번호판 인식 실패
        if (flaskResponseDto.getStatus() == 500) {
            System.out.println(flaskResponseDto.toString());

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

            RecordResponseDto dto = RecordResponseDto.builder()
                    .recordId(savedLog.getRecordId())
                    .vehicleImage(vehicleImg.getImageUrl())
                    .plateImage("인식 실패")
                    .state(savedLog.getState())
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy("인식 실패")
                    .build();

            return MultiResponseDto.of(dto, null, null);
        }

        List<ModelPredictResult> modelPredictResults = flaskResponseDto.getPredictedResults();

        List<PredictedPlateDto> predRespDto = new ArrayList<>();

        for (ModelPredictResult predictedResult : modelPredictResults) {
            predRespDto.add(PredictedPlateDto.convertIntoDto(predictedResult));
        }

        int numOfPresentPlate = 0;
        for (PredictedPlateDto predictedPlateDto : predRespDto) {
            if (carRepo.findByLicensePlate(predictedPlateDto.getModelPredictResult().getPredictedText()).isPresent()) {
                predictedPlateDto.setIsPresent(true);
                numOfPresentPlate++;
            }
        }

        ModelPredictResult bestModelResult = new ModelPredictResult();
        double maxAccuracy = 0;
        for (PredictedPlateDto predictedPlateDto : predRespDto) {
            double curAccuracy = predictedPlateDto.getModelPredictResult().getAccuracy();
            if (maxAccuracy < curAccuracy) {
                maxAccuracy = curAccuracy;
                bestModelResult = predictedPlateDto.getModelPredictResult();
            }
        }

        // Log 엔티티 저장
        Record savedRecord = recordRepo.save(Record.builder()
                .modelType(bestModelResult.getModelType())
                .licensePlate(bestModelResult.getPredictedText())
                .accuracy(maxAccuracy)
                .state(numOfPresentPlate >= 2 ? "수정 불필요" : "수정 필요")
                .build());

        // Image 엔티티 저장
        // 원본 file 업로드(Aws S3)
        String[] vehicleImgArr = fileService.uploadFile(file, 0);

        String vehicleImgUrl = vehicleImgArr[0];
        String vehicleImgTitle = vehicleImgArr[1];
        String plateImgUrl = String.valueOf(flaskResponseDto.getPlateImgUrl());
        String plateImgTitle = String.valueOf(flaskResponseDto.getPlateImgTitle());

        Image vehicleImg = imgRepo.save(Image.builder()
                .record(savedRecord)
                .imageUrl(vehicleImgUrl)
                .imageType("vehicle")
                .imageTitle(vehicleImgTitle).build());

        Image plateImg = imgRepo.save(Image.builder()
                .record(savedRecord)
                .imageUrl(plateImgUrl)
                .imageType("plate")
                .imageTitle(plateImgTitle).build());

        // PredictedPlate 엔티티 저장
        for (PredictedPlateDto predictedPlateDto : predRespDto) {
            predRepo.save(PredictedPlate.builder()
                    .record(savedRecord)
                    .modelPredictResult(predictedPlateDto.getModelPredictResult())
                    .isPresent(predictedPlateDto.isPresent())
                    .build());
        }

        // RecordResponseDto 구성
        RecordResponseDto recordRespDto = RecordResponseDto.builder()
                .recordId(savedRecord.getRecordId())
                .vehicleImage(vehicleImg.getImageUrl())
                .plateImage(plateImg.getImageUrl())
                .state(savedRecord.getState())
                .modelType(savedRecord.getModelType())
                .licensePlate(savedRecord.getLicensePlate())
                .accuracy(savedRecord.getAccuracy() == 0.0 ? "-" : String.valueOf(savedRecord.getAccuracy()))
                .build();

        ImageResponseDto imageRespDto = ImageResponseDto.of(savedRecord.getRecordId(), plateImgUrl, "plate");

        return MultiResponseDto.of(recordRespDto, predRespDto, imageRespDto);
    }

    // Record & Image -> RecordResponseDto
    private List<RecordResponseDto> createRecordResponseDtos(List<Record> logEntities) {
        List<RecordResponseDto> list = new ArrayList<>();
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
            list.add(RecordResponseDto.builder()
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

    // 4. 날짜별 기록 조회 yy-MM-dd
    public List<RecordResponseDto> searchDate(String start, String end) throws ParseException {

        // String -> Date 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        System.out.printf("%s ~ %s", startDate, endDate);

        // 기록 조회
        List<Record> logEntities = recordRepo.findByCreatedDateBetween(startDate, endDate);
        System.out.println("logEntities : " + logEntities);

        // LogEntity -> LogDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(logEntities);

        return list;
    }

    // 5. 차량 번호별 기록 조회
    public List<RecordResponseDto> searchPlate(String plate) {

        // 기록 조회
        List<Record> logEntities = recordRepo.findByLicensePlate(plate);

        // LogEntity -> LogDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(logEntities);

        return list;
    }

    // 6. 수정/삭제 기록 조회
    public List<HistoryResponseDto> getHistory() {
        List<History> entities = histRepo.findAll();
        List<HistoryResponseDto> list = new ArrayList<>();
        for (History entity : entities) {
            list.add(HistoryResponseDto.builder()
                    .id(entity.getId())
                    .recordId(entity.getRecordId())
                    .memberId(entity.getMemberId())
                    .workType(entity.getTaskType())
                    .currentText(entity.getCurrentText())
                    .previousText(entity.getPreviousText())
                    .createdDate(entity.getCreatedDate())
                    .build());
        }
        return list;
    }

    // 7. 기록 수정(admin)
    public void updateLog(ArrayList<RecordResponseDto> list) throws IOException {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        for (RecordResponseDto dto : list) {
            Optional<Record> optionalLog = recordRepo.findById(dto.getRecordId());
            // if (!optionalLog.isPresent()) {
            // return response.fail("존재하지 않는 기록입니다.", HttpStatus.BAD_REQUEST);
            // }

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

                // } catch (AmazonS3Exception s3Exception) {
                // continue;
            } catch (Exception e) {
                e.printStackTrace();
                // return response.fail(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
    }

    // 8. 기록 삭제(admin)
    public void deleteLog(ArrayList<RecordResponseDto> list) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            for (RecordResponseDto dto : list) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
