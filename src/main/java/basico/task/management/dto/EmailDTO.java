package basico.task.management.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EmailDTO {

    private String name;
    private String taskId;
    private String from;
    private String to;
    private String header;
    private String subject;
    private String template;
    Map<String, Object> properties;

}
