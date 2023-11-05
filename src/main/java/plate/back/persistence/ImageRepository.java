package plate.back.persistence;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.Image;
import plate.back.entity.Record;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    public ArrayList<Image> findByRecord(Record record);
}
