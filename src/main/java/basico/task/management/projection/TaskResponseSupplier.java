package basico.task.management.projection;

import java.util.Date;

public interface TaskResponseSupplier {

    Long getId();
    Long referenceId();
    String getType();
    String getStatus();
    Date getAssignDate();
    Date  getCreatedDate();
    Date  getDeliveryDate();
    String  getComments();
    String  getSupplierName();
    Long  getSupplierId();
    String getSocietyId();
    String getCity();
    String getStorage();
    String getProvince();
    String getPromotionName();
    String getDirection();


}
