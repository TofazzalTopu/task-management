package basico.task.management.dto.userprofile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangePasswordDto {


    @NotNull
    @NotEmpty
    private String newPassword;
    @NotNull
    @NotEmpty
    private String cofrimPassword;
    @NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCofrimPassword() {
        return cofrimPassword;
    }

    public void setCofrimPassword(String cofrimPassword) {
        this.cofrimPassword = cofrimPassword;
    }


}
