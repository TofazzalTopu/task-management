package basico.task.management.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewOrderAssetsDto {

    private String locationId;

    private String garajeId;

    private String storageRoomId;

    @NotNull
    private Long societyId;

    @NotNull
    private Long promotionId;

    @NotNull
    @NotEmpty
    private String type;

}
