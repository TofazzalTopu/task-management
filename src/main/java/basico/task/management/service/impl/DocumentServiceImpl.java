package basico.task.management.service.impl;

import basico.task.management.dto.InvoiceDto;
import basico.task.management.dto.TaskDocumentDto;
import basico.task.management.dto.TaskDocumentUpdateDto;
import basico.task.management.enums.DocType;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Document;
import basico.task.management.model.primary.Task;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.repository.primary.DocumentRepository;
import basico.task.management.service.DocumentService;
import basico.task.management.service.FtpService;
import basico.task.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final MessageSource messageSource;
    private final DocumentRepository documentRepository;
    private final UserService userService;
    private final FtpService ftpService;
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;


    @Override
    public Document findById(Long id, Locale locale) {
        return documentRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("document.not.found", null, locale)));
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        documentRepository.deleteById(id);
    }

    @Override
    public List<Document> findByTaskId(Long taskId) {
        return documentRepository.findAllByTaskId(taskId);
    }


    @Override
    public List<Document> saveAll(Long userId, List<TaskDocumentDto> taskDocumentDtoList, Task task, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        UserProfile userProfile = userService.findById(userId, locale);
        List<Document> documentList = new LinkedList<>();
        for (TaskDocumentDto documentDto : taskDocumentDtoList) {
            MultipartFile taskDocument = documentDto.getFile();
            Document document = new Document();
            document.setInitialDoc(documentDto.isInitialDoc());
            document.setDate(new Date());
            document.setFile(taskDocument.getBytes());
            document.setName(taskDocument.getOriginalFilename());
            document.setMimeType(StringUtils.getFilenameExtension(taskDocument.getOriginalFilename()));
            document.setTask(task);
            UUID uuid = UUID.randomUUID();
            String path = ftpService.uploadFile(httpServletRequest, httpServletResponse, taskDocument, taskDocument.getOriginalFilename(), task, uuid.toString());
            document.setFilePath(path + taskDocument.getOriginalFilename());
            document.setFileUUID(uuid.toString());
            document.setDocType(DocType.TASK.name());
            document.setUser(userProfile);
            document.setStatus(task.getStatus());
            documentList.add(document);
        }
        return documentRepository.saveAll(documentList);
    }

    @Override
    public List<Document> updateDocument(Long userId, List<TaskDocumentUpdateDto> documentUpdateDtoList, Task savedTask, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        UserProfile userProfile = userService.findById(userId, locale);

        List<Document> documentList = new LinkedList<>();
        for (TaskDocumentUpdateDto updateDto : documentUpdateDtoList) {
            MultipartFile taskDocument = updateDto.getFile();
            Document document = new Document();
            document.setInitialDoc(updateDto.isInitialDoc());
            document.setDate(new Date());
            document.setFile(taskDocument.getBytes());
            document.setName(taskDocument.getOriginalFilename());
            document.setTask(savedTask);
            document.setUser(userProfile);
            document.setMimeType(StringUtils.getFilenameExtension(taskDocument.getOriginalFilename()));
            document.setDocType(DocType.TASK.name());
            UUID uuid = UUID.randomUUID();
            String path = ftpService.uploadFile(httpServletRequest, httpServletResponse, taskDocument, taskDocument.getOriginalFilename(), savedTask, uuid.toString());
            document.setFilePath(path + taskDocument.getOriginalFilename());
            document.setFileUUID(uuid.toString());
            document.setStatus(savedTask.getStatus());
            documentList.add(document);
        }
        documentRepository.saveAll(documentList);
        return documentRepository.findAllByTaskId(savedTask.getId());
    }


    @Override
    public Document saveDocument(Long userId, InvoiceDto invoiceDto, Task savedTask, String type, Locale locale) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        UserProfile userProfile = userService.findById(userId, locale);
        Document document = new Document();
        document.setInitialDoc(invoiceDto.isInitialDoc());
        document.setDate(new Date());
        document.setFile(invoiceDto.getFile().getBytes());
        document.setName(invoiceDto.getFile().getOriginalFilename());
        document.setMimeType(StringUtils.getFilenameExtension(invoiceDto.getFile().getOriginalFilename()));
        document.setTask(savedTask);
        document.setUser(userProfile);
        UUID uuid = UUID.randomUUID();
        String path = ftpService.uploadFile(httpServletRequest, httpServletResponse, invoiceDto.getFile(), invoiceDto.getFile().getOriginalFilename(), savedTask, uuid.toString());
        document.setFilePath(path + invoiceDto.getFile().getOriginalFilename());
        document.setFileUUID(uuid.toString());
        document.setDocType(type);
        document.setStatus(savedTask.getStatus());
        return documentRepository.save(document);
    }

    @Override
    public Document findAcceptedInvoiceOfTask(Long id) {
        return documentRepository.findFirstByTaskIdAndDocTypeOrderByDateDesc(id, DocType.INVOICE.name());
    }

    @Override
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Long taskId, String fileId) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        Document document = documentRepository.findByTaskIdAndFileUUID(taskId, fileId);
        ftpService.downloadFile(httpServletRequest, httpServletResponse, document.getFilePath());
    }


}
