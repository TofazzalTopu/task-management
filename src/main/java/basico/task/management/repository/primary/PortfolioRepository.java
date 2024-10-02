package basico.task.management.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import basico.task.management.model.primary.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>{

}
