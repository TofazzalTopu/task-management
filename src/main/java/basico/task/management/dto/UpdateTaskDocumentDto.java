package basico.task.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTaskDocumentDto {

    private List<TaskDocumentUpdateDto> dtoList;

}
