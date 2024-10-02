package basico.task.management.repository.primary;

import basico.task.management.model.primary.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("SELECT coalesce(max(id), 0) FROM Invoice")
    long findMaxId();

    Invoice findByInvoiceNumber(String invoiceNumber);
}
