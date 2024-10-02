package basico.task.management.model.assets;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CL3349.fuhrent")
@Data
public class AssetsExternal {

	@Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;
	
	@Column(name = "URALIA")
	private String location;
		
	@Column(name = "URCODPOSTAL")
	private String province;

	@Column(name = "URPROM")
	private String poromottionCode;

	@Column(name = "URDEES")
	private String status;

	@Column(name = "URSOCI")
	private String socityId;

	@Column(name = "URPERR")
	private String promotionDescription;

	@Column(name = "UREDIT")
	private String assetId;

	@Column(name = "URPOBLACION ")
	private String city;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPoromottionCode() {
		return poromottionCode;
	}

	public void setPoromottionCode(String poromottionCode) {
		this.poromottionCode = poromottionCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSocityId() {
		return socityId;
	}

	public void setSocityId(String socityId) {
		this.socityId = socityId;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
