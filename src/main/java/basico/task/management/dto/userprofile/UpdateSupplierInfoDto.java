package basico.task.management.dto.userprofile;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateSupplierInfoDto {

    private String nif;

    private String address;

    private String phoneNumber;

    private String position;

}
