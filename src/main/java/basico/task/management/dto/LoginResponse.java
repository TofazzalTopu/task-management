package basico.task.management.dto;


import basico.task.management.model.primary.Role;
import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private String userName;

    private String companyName;

    private Long userId;

    private Role role;

    private boolean isSupplier;

    boolean supplierInfoIsUpdated;

    boolean isMember;

}
