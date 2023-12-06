package plate.back.domain.record.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.domain.car.service.CarService;
import plate.back.domain.image.entity.Image;
import plate.back.domain.image.service.ImageService;
import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;
import plate.back.domain.predictedPlate.entity.Enrollment;
import plate.back.domain.predictedPlate.entity.ModelPredictResult;
import plate.back.domain.predictedPlate.entity.ModelType;
import plate.back.domain.predictedPlate.service.PredictedPlateService;
import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.domain.record.entity.Record;
import plate.back.domain.record.repository.RecordRepository;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;
import plate.back.global.flask.dto.FlaskResponseDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepo;
    private final CarService carService;
    private final PredictedPlateService predService;
    private final ImageService imageService;

    // 3. 차량 출입 기록 기록
    @Override
    public MultiResponseDto recordLog(FlaskResponseDto flaskResponseDto, Map<String, String> vehicleImgMap,
            Map<String, String> plateImgMap) {

        // 번호판 인식 실패
        if (flaskResponseDto.getStatus() == 500) {
            log.info(flaskResponseDto.toString());

            Record savedRecord = recordRepo.save(Record.builder()
                    .modelType(ModelType.Fail)
                    .licensePlate("인식 실패")
                    .accuracy(0.0)
                    .state("수정 필요")
                    .build());

            Image vehicleImg = imageService.saveVehicleImage(savedRecord, vehicleImgMap);

            RecordResponseDto dto = RecordResponseDto.builder()
                    .recordId(savedRecord.getRecordId())
                    .vehicleImage(vehicleImg.getImageUrl())
                    .plateImage("인식 실패")
                    .state(savedRecord.getState())
                    .modelType(ModelType.Fail)
                    .licensePlate("인식 실패")
                    .accuracy("인식 실패")
                    .build();

            return MultiResponseDto.of(dto, null);
        }

        List<ModelPredictResult> modelPredictResults = flaskResponseDto.getPredictedResults();

        List<PredictedPlateResponseDto> predRespDtoList = new ArrayList<>();

        for (ModelPredictResult predictedResult : modelPredictResults) {
            predRespDtoList.add(PredictedPlateResponseDto.convertIntoDto(predictedResult));
        }

        // 모델이 예측한 번호를 차량 DB에서 조회, 있는 번호 count
        int numOfEnrolledPlate = 0;
        for (PredictedPlateResponseDto predRespDto : predRespDtoList) {
            if (carService.checkEnrollment(predRespDto)) {
                predRespDto.setIsEnrolled(Enrollment.ENROLLED);
                numOfEnrolledPlate++;
            } else {
                predRespDto.setIsEnrolled(Enrollment.UNENROLLED);
            }
        }

        ModelPredictResult bestModelResult = new ModelPredictResult();
        double maxAccuracy = 0;
        for (PredictedPlateResponseDto predRespDto : predRespDtoList) {
            double curAccuracy = predRespDto.getModelPredictResult().getAccuracy();
            if (maxAccuracy < curAccuracy) {
                maxAccuracy = curAccuracy;
                bestModelResult = predRespDto.getModelPredictResult();
            }
        }

        // Record 엔티티 저장
        Record savedRecord = recordRepo.save(Record.builder()
                .modelType(bestModelResult.getModelType())
                .licensePlate(bestModelResult.getPredictedText())
                .accuracy(maxAccuracy)
                .state(numOfEnrolledPlate >= 2 ? "수정 불필요" : "수정 필요")
                .build());

        // Image 엔티티 저장
        Image vehicleImg = imageService.saveVehicleImage(savedRecord, vehicleImgMap);
        Image plateImg = imageService.savePlateImage(savedRecord, plateImgMap);

        // PredictedPlate 엔티티 저장
        for (PredictedPlateResponseDto predRespDto : predRespDtoList) {
            predService.savePredictedPlate(savedRecord, predRespDto);
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

        return MultiResponseDto.of(recordRespDto, predRespDtoList);
    }

    // 4. 날짜별 기록 조회 yy-MM-dd
    @Override
    public List<RecordResponseDto> searchDate(String start, String end) throws ParseException {

        // String -> LocalDateTime 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);

        LocalDateTime startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .withHour(23).withMinute(59).withSecond(59);
        ;

        log.info(String.format("검색 날짜 : %s ~ %s", startDateTime, endDateTime));

        // 기록 조회
        List<Record> recordEntityList = recordRepo.findByModifiedDateBetween(startDateTime, endDateTime);

        log.info("recordEntityList : " + recordEntityList);

        // Record -> RecordResponseDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(recordEntityList);

        return list;
    }

    // 5. 차량 번호별 기록 조회
    @Override
    public List<RecordResponseDto> searchPlate(String plate) {

        // 기록 조회
        List<Record> recordEntityList = recordRepo.findByLicensePlate(plate);

        // Record -> RecordResponseDto 변환
        List<RecordResponseDto> list = createRecordResponseDtos(recordEntityList);

        return list;
    }

    // Record + Image -> RecordResponseDto
    private List<RecordResponseDto> createRecordResponseDtos(List<Record> recordEntityList) {

        List<RecordResponseDto> list = new ArrayList<>();

        for (Record record : recordEntityList) {

            List<Image> imgEntityList = record.getImages();
            String vehicleImg;
            String plateImg;

            if (imgEntityList.size() == 2) {
                vehicleImg = imgEntityList.get(0).getImageUrl();
                plateImg = imgEntityList.get(1).getImageUrl();
            } else if (imgEntityList.size() == 1) {
                vehicleImg = imgEntityList.get(0).getImageUrl();
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

    // 6. 차량 출입 기록 수정(ADMIN)
    @Override
    public String updateRecord(RecordRequestDto.Update resqDto) {

        Record record = recordRepo.findById(resqDto.getRecordId())
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

        String previousText = record.getLicensePlate();

        // 수정된 log 기록
        record.updateLicensePlate(resqDto.getLicensePlate(), "수정 완료");

        recordRepo.save(record);

        imageService.updateImage(record);

        return previousText;
    }

    // 7. 차량 출입 기록 삭제(ADMIN)
    @Override
    public void deleteRecord(RecordRequestDto.Delete resqDto) {

        int recordId = resqDto.getRecordId();

        Record record = recordRepo.findByRecordId(recordId).get();

        // AWS S3 파일 삭제
        imageService.deleteImage(record);

        // record 삭제
        recordRepo.deleteById(recordId);
    }

}