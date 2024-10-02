package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "society")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Society {

    @Id
    private Long id;

    @Column(name = "SOCIETY_NAME")
    private String societyName;

    @Column(name = "DIRECTION")
    private String direction;

    @Column(name = "IBAN")
    private String iBan;

    @Column(name = "TEL_NUMBER")
    private String telNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CIF")
    private String cif;

    @Column(name = "STATUS")
    private Boolean status=true;

}
