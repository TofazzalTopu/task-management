package basico.task.management.dto;

import basico.task.management.util.ValidDate;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class TaskUpdateDto {


    @NotNull
    @NotEmpty
    private String type;

    private Long supplierId;

    @NotNull
    private String assetId;

    @NotNull
    private Long createdById;

    private Long referenceId;

    private Long technicalAssignee;

    private String location;

    private String promotionName;

    private String garaje;

    @ValidDate
    private String deliveryDate;

    private String storageRoom;

    private String code;

    private String province;

    private String direction;

    private String city;

    private Long societyId;

    private Long promotionId;

    private String arrOperacion;

    @Temporal(TemporalType.DATE)
    private Date dateExit;

    private String dateSign;


    private String comments;

    @Max(value = 365)
    @Min(value = 1)
    private Integer noOfDays;

    private List<TaskDocumentUpdateDto> documentUpdateDtoList;

    private boolean capex;

    private boolean opex;


}
