package plate.back.persistence;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import plate.back.entity.LogEntity;

public interface LogRepository extends JpaRepository<LogEntity, Integer> {
    // N+1 문제 해결
    // @EntityGraph(attributePaths = { "predList", "historyList", "imageList" },
    // type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT log FROM LogEntity log WHERE log.licensePlate = :plate")
    ArrayList<LogEntity> findByPlate(@Param("plate") String plate);

    @Query("SELECT log FROM LogEntity log WHERE FUNCTION('DATE', log.date) BETWEEN :start AND :end")
    ArrayList<LogEntity> findByDate(@Param("start") Date start, @Param("end") Date end);
}
