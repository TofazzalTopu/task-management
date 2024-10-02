package basico.task.management.dto;

import basico.task.management.dto.userprofile.CompanyDto;
import lombok.Data;

import java.time.LocalDate;


@Data
public class TaskCompanyDto {

    private Long taskId;

    private String type;

    private LocalDate createDate;

    private CompanyDto company;

}
