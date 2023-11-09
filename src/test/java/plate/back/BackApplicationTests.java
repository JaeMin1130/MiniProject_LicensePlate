package plate.back;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import plate.back.domain.car.repository.CarInfoRepository;
import plate.back.domain.predictedPlate.repository.PredictedPlateRepository;
import plate.back.domain.record.entity.Record;
import plate.back.domain.record.repository.RecordRepository;

@SpringBootTest
class BackApplicationTests {

    @Autowired
    CarInfoRepository carInfoRepo;
    @Autowired
    RecordRepository logRepo;
    @Autowired
    PredictedPlateRepository predRepo;

    @Test
    void contextLoads() {
    }

}
