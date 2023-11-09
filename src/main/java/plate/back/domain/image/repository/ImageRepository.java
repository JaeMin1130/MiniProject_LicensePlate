package plate.back.domain.image.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.image.entity.Image;
import plate.back.domain.record.entity.Record;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    public ArrayList<Image> findByRecord(Record record);
}
