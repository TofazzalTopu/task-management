package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "GLPI_TASK")
public class GlpiTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GIPL_TASK_SEQ")
    @SequenceGenerator(name = "GIPL_TASK_SEQ", sequenceName = "GIPL_TASK_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
	private String code;

	@Column(name = "PROVINCE")
	private String province;

	@Column(name = "TICKET_ID")
	private Long ticketId;

	@Column(name = "DIRECTION")
	private String direction;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CITY")
	private String city;

	@Column(name = "EMAIl")
	private String email;

	@Column(name = "USER_GROUP")
	private String userGroup;

	@Column(name = "TICKET_NAME")
	private String ticketName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GARAJE_ID",foreignKey = @ForeignKey(name = "GLPI_GARAJE_ID_FK"))
	private Garaje garajes;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORAGE_ID",foreignKey = @ForeignKey(name = "GLPI_STORAGE_ID_FK"))
	private StorageRoom storageRoom;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID",foreignKey = @ForeignKey(name = "GLPI_SOCIETY_ID_FK"))
	private Location location;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOCIETY_ID",foreignKey = @ForeignKey(name = "GLPI_SOCIETY_ID_FK"))
	private Society society;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMOTION_ID",foreignKey = @ForeignKey(name = "GLPI_PROMOTION_ID_FK"))
	private Promotion promotion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "status_fk"))
	private Status status;


}
