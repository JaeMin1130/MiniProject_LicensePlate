package plate.back.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.HistoryEntity;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
}
