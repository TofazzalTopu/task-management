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
public class Assets {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="asset_SEQ")
    @SequenceGenerator(name="asset_SEQ", sequenceName="asset_SEQ", allocationSize=1)
	@Column(name = "ID")
    private Long id;

	@Column(name = "ARR_ID")
	private Long arrId;
	
	@Column(name = "COMMUNITY")
	private String community;


	@Column(name = "PROVINCE")
	private String province;


	@Column(name = "Municipality")
	private String municipality;

	@Column(name = "TYPE")
	private String type;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GARAJE_ID",foreignKey = @ForeignKey(name = "ASSET_GARAJE_ID_FK"))
	private Garaje garajes;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORAGE_ID",foreignKey = @ForeignKey(name = "ASSET_STORAGE_ID_FK"))
	private StorageRoom storageRoom;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID",foreignKey = @ForeignKey(name = "ASSET_SOCIETY_ID_FK"))
	private Location location;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOCIETY_ID",foreignKey = @ForeignKey(name = "ASSET_SOCIETY_ID_FK"))
	private Society society;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMOTION_ID",foreignKey = @ForeignKey(name = "ASSET_PROMOTION_ID_FK"))
	private Promotion promotion;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "status_fk"))
	private Status taskStatus;

}
