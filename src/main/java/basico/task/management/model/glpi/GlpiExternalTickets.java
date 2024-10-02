package basico.task.management.model.glpi;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "glpi_tickets")
@Data
public class GlpiExternalTickets {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "province")
    private String province;

    @Column(name = "direction")
    private String direction;

    @Column(name = "city")
    private String city;

    @Column(name = "promotionName")
    private String promotionName;


}
