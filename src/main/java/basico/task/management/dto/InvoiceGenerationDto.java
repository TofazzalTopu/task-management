package basico.task.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceGenerationDto {

    private String assetId;
    private String taskId;
    private String nif;
    private double tax;
    private double total;
    private String invoiceDate;
    private String customerCode;
    private String invoiceNumber;
    private double taxBase;
    private double subTotal;
    private String vatFee;
    private String societyName;
    private String supplierAddress;
    private String supplierCompanyName;
    private String location;
    private String direction;
    private String promotion;
    private Long supplierId;
    private List<InvoiceQuantity> quantities;


}
