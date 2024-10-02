package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class InvoiceDto {


    @NotNull
    private MultipartFile file;
    @NotNull
    private String invoiceNumber;
    private boolean initialDoc;

}
