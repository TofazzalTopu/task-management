package basico.task.management.projection;


import java.util.Date;

public interface TaskResponse {

    Long getId();
    String getStatus();
    Date  getAssignDate();
    Date  getCreatedDate();
    Date  getDeliveryDate();
    String  getComments();
    String  getSupplierName();
    Long  getSupplierId();
    Long  getAssetsId();
    String  getProvince();
    Long  getSocietyId();
    String  getLocation();
    String  getAssetType();
    String  getPromotionCode();
    String  getCity();
    String  getPromotionDescription();


}
