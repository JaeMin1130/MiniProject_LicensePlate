package plate.back.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.history.entity.History;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
