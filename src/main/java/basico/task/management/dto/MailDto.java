package basico.task.management.dto;

import lombok.Data;

@Data
public class MailDto {

    private String subject;

    private String to;

    private String link;

    private String type;

}
