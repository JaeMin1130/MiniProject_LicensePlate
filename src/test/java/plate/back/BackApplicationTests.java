package plate.back;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import plate.back.entity.Record;
import plate.back.persistence.CarInfoRepository;
import plate.back.persistence.RecordRepository;
import plate.back.persistence.PredictLogRepository;

@SpringBootTest
class BackApplicationTests {

    @Autowired
    CarInfoRepository carInfoRepo;
    @Autowired
    RecordRepository logRepo;
    @Autowired
    PredictLogRepository predRepo;

    @Test
    void contextLoads() {
    }

}
