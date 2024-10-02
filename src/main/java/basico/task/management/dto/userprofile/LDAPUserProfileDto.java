package basico.task.management.dto.userprofile;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LDAPUserProfileDto {

    @NotNull
    @NotEmpty
    private String userName;

    private String userGroup;

    private String password;

}
