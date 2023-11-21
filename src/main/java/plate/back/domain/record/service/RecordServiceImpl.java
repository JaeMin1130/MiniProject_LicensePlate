package plate.back.domain.record.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.domain.car.repository.CarInfoRepository;
import plate.back.domain.image.entity.Image;
import plate.back.domain.image.repository.ImageRepository;
import plate.back.domain.predictedPlate.dto.PredictedPlateDto;
import plate.back.domain.predictedPlate.entity.PredictedPlate;
import plate.back.domain.predictedPlate.repository.PredictedPlateRepository;
import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.domain.record.entity.Record;
import plate.back.domain.record.repository.RecordRepository;
import plate.back.global.flask.dto.FlaskResponseDto;
import plate.back.global.flask.dto.ModelPredictResult;
import plate.back.global.s3.service.FileService;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final CarInfoRepository carRepo;
    private final RecordRepository recordRepo;
    private final ImageRepository imgRepo;
    private final PredictedPlateRepository predRepo;
    private final FileService fileService;

    // 3. 차량 출입 기록 기록
    public MultiResponseDto recordLog(FlaskResponseDto flaskResponseDto, String[] vehicleImgArr) {

        // 번호판 인식 실패
        if (flaskResponseDto.getStatus() == 500) {
            log.info(flaskResponseDto.toString());

            Record savedLog = recordRepo.save(Record.builder()
                    .modelType("인식 실패")
                    .licensePlate("인식 실패")
                    .accuracy(0.0)
                    .state("수정 필요")
                    .build());

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

            return MultiResponseDto.of(dto, null);
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

        return MultiResponseDto.of(recordRespDto, predRespDto);
    }

    // Record & Image -> RecordResponseDto
    private List<RecordResponseDto> createRecordResponseDtos(List<Record> recordEntityList) {
        List<RecordResponseDto> list = new ArrayList<>();
        for (Record record : recordEntityList) {
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

        // String -> LocalDateTime 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);

        LocalDateTime startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .withHour(23).withMinute(59).withSecond(59);
        ;

        log.info(String.format("%s ~ %s", startDateTime, endDateTime));

        // 기록 조회
        List<Record> recordEntityList = recordRepo.findByModifiedDateBetween(startDateTime, endDateTime);

        log.info("recordEntityList : " + recordEntityList);

        // LogEntity -> LogDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(recordEntityList);

        return list;
    }

    // 5. 차량 번호별 기록 조회
    public List<RecordResponseDto> searchPlate(String plate) {

        // 기록 조회
        List<Record> recordEntityList = recordRepo.findByLicensePlate(plate);

        // LogEntity -> LogDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(recordEntityList);

        return list;
    }

    // 7. 기록 수정(admin)
    public String updateRecord(RecordRequestDto.Update resqDto) {

        Optional<Record> optionalLog = recordRepo.findById(resqDto.getRecordId());
        // if (!optionalLog.isPresent()) {
        // return response.fail("존재하지 않는 기록입니다.", HttpStatus.BAD_REQUEST);
        // }

        Record record = optionalLog.get();
        String previousText = record.getLicensePlate();
        // 수정된 log 기록
        record.updateLicensePlate(resqDto.getLicensePlate(), "수정 완료");

        recordRepo.save(record);

        // AWS S3 파일 이동
        try {
            List<Image> imgEntities = imgRepo.findByRecord(record);

            for (Image imageEntity : imgEntities) {
                String answer = record.getLicensePlate();
                String imageTitle = imageEntity.getImageTitle();
                String imageType = imageEntity.getImageType();

                Map<String, String> map = fileService.moveFile(imageTitle, imageType,
                        answer);

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
        return previousText;
    }

    // 8. 기록 삭제(admin)
    public void deleteRecord(RecordRequestDto.Delete resqDto) {

        int recordId = resqDto.getRecordId();

        // AWS S3 파일 삭제
        List<Image> imgEntities = imgRepo.findByRecord(recordRepo.findByRecordId(recordId).get());
        for (Image imgEntity : imgEntities) {
            String imageTitle = imgEntity.getImageTitle();
            String imageType = imgEntity.getImageType();
            fileService.deleteFile(imageTitle, imageType);
        }

        // log 삭제
        recordRepo.deleteById(recordId);
    }

}