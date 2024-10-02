package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "GROUP_CHECKLIST")
public class GroupCheckList {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="asset_SEQ")
    @SequenceGenerator(name="asset_SEQ", sequenceName="asset_SEQ", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REF")
    private String ref;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UNIDADES")
    private String uniDades;

    @Column(name = "PRICE_UNIT")
    private String priceUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID",nullable = false, foreignKey = @ForeignKey(name = "ID"))
    private Group group;



}
