package basico.task.management.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AssignSupplierDto {

    @NotNull
    private String type;

    @NotNull
    private Long supplierId;

    @NotNull
    private Long referenceId;

    private boolean capex;

    private boolean opex;

}
