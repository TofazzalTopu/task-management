package basico.task.management.dto;

import basico.task.management.model.primary.Budget;
import basico.task.management.model.primary.Invoice;
import lombok.Data;

import java.util.List;

@Data
public class BudgetResponse {

    private List<Budget> budget;
    private double tax;
    private List<Invoice> invoiceNumber;
}
