package basico.task.management.service;

import basico.task.management.dto.InvoiceDto;
import basico.task.management.dto.TaskDocumentDto;
import basico.task.management.dto.TaskDocumentUpdateDto;
import basico.task.management.model.primary.Document;
import basico.task.management.model.primary.Task;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

public interface DocumentService {

    Document findById(Long id, Locale locale);
    void delete(Long id, Locale locale);
    List<Document> findByTaskId(Long taskId);
    List<Document> saveAll(Long userId, List<TaskDocumentDto> taskDocumentDtoList, Task task, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;
    List<Document> updateDocument(Long userId, List<TaskDocumentUpdateDto> documentUpdateDtoList, Task savedTask, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;
    Document saveDocument(Long userId, InvoiceDto invoiceDto, Task savedTask, String type, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException;
    Document findAcceptedInvoiceOfTask(Long id);
    void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long taskId, String fileId) throws NoSuchAlgorithmException, IOException, KeyManagementException;
}
