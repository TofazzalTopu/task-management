package basico.task.management.repository.plataforma;

import basico.task.management.model.plataforma.PrinexGarage;
import basico.task.management.model.primary.Garaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrinexGarageRepository extends JpaRepository<PrinexGarage,Long> {

}
