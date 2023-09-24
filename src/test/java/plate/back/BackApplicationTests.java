package plate.back;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import plate.back.entity.LogEntity;
import plate.back.persistence.CarInfoRepository;
import plate.back.persistence.LogRepository;
import plate.back.persistence.PredictLogRepository;

@SpringBootTest
class BackApplicationTests {

    @Autowired
    CarInfoRepository carInfoRepo;
    @Autowired
    LogRepository logRepo;
    @Autowired
    PredictLogRepository predRepo;

    @Test
    void contextLoads() {
        List<LogEntity> logEntities = logRepo.findByDate(new Date(), new Date());
        System.out.println(logEntities.get(0).getLogId());
    }

}
