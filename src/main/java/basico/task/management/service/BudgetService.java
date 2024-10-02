package basico.task.management.service;

import basico.task.management.dto.BudgetDTO;
import basico.task.management.dto.BudgetResponse;
import basico.task.management.model.primary.Budget;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;

public interface BudgetService {

    List<Budget> save(String token, Long referenceId, String type, List<BudgetDTO> budgetDTOList, Locale locale) throws MessagingException;

    BudgetResponse findByReferenceIdAndType(String token, Long referenceId, String type, Locale locale);

    Budget findById(String token, Long id, Locale locale);

    List<Budget>  findByReferenceId(Long id);
    void delete(String token, Long id, Locale locale);
}
