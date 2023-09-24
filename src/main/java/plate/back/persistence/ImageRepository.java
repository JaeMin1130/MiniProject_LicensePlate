package plate.back.persistence;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import plate.back.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    @Query("select img from ImageEntity img where img.logEntity.logId in (select log.logId from LogEntity log where log.logId = :num)")
    public ArrayList<ImageEntity> findByLogId(@Param("num") Integer num);
}
