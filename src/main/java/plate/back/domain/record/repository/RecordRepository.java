package plate.back.domain.record.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.record.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    Optional<Record> findByRecordId(Integer recordId);

    ArrayList<Record> findByLicensePlate(String licensePlate);

    ArrayList<Record> findByCreatedDateBetween(Date start, Date end);
}
