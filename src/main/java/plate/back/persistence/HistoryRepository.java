package plate.back.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.History;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
