package basico.task.management.util;

import basico.task.management.model.primary.Task;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.core.io.FileSystemResource;

import java.io.*;

public class GeneratePdf {

    public FileSystemResource generatePdf(Task task, StringWriter out) {
        String referenceId = task.getTaskId();
        String pdfFileName = referenceId + ".pdf";
        try {
            String value = out.getBuffer().toString();
            Document document = new Document(PageSize.A3, 26, 26, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();
            InputStream in = new ByteArrayInputStream(value.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, in);
            document.close();
            writer.close();
            FileSystemResource file = new FileSystemResource(new File(pdfFileName));
            return file;

        } catch (Exception e) {
            return null;
        }
    }


}
