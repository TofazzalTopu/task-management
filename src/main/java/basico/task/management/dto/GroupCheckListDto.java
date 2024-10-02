package basico.task.management.dto;

import lombok.Data;

@Data
public class GroupCheckListDto {
    private String ref;

    private String number;

    private String description;

    private String uniDades;

    private String priceUnit;

    private Long groupId;
}
