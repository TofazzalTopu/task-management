package basico.task.management.dto.userprofile;

import basico.task.management.model.primary.UserProfile;
import lombok.Data;

@Data
public class CompanyDto {

    private UserProfile company;

    private long noOfMembers;
}
