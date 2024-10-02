package basico.task.management.repository.primary;

import basico.task.management.model.primary.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findAllByTaskId(Long taskId);

    Document findFirstByTaskIdAndDocTypeOrderByDateDesc(Long id, String name);

    Document findByTaskIdAndFileUUID(Long taskId, String fileId);
}
