package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROMOTION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Promotion {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "PROMOTION_NAME")
    private String promotionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCIETYID")
    private Society society;

    @Column(name = "COMERCIAL")
    private String comercial;



}
