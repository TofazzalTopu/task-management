package basico.task.management.util;

import basico.task.management.dto.InvoiceGenerationDto;
import basico.task.management.model.primary.Task;

import java.io.IOException;
import java.io.StringWriter;

public class CSVGenerator {

    private static String NEXT_LINE = "\n";
    private static String DELIM = ",";


    public String csvGenerator(Task task, InvoiceGenerationDto invoiceGenerationDto) throws IOException {
        StringWriter streamWriter = new StringWriter();
        String headers = "\"TYPE\",\"TASKID\",\"TAX\",\"Sub Total\",\"TOTAL\",\"CREATEDDATE\",\"NIF\",\"Location\",\"Order Number\",\"Company\",\"Address\",";
        streamWriter.append(headers);
        streamWriter.append(NEXT_LINE);
        streamWriter.append("\""+String.valueOf(task.getType())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(task.getTaskId())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getTax())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getSubTotal())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getTotal())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(task.getCreatedDate())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getNif())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getLocation())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getInvoiceNumber())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getSupplierCompanyName())+"\"");
        streamWriter.append(DELIM);
        streamWriter.append("\""+String.valueOf(invoiceGenerationDto.getSupplierAddress())+"\"");
        streamWriter.flush();
        return streamWriter.toString();
    }

}
