package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cartera {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARTERA_SEQ")
    @SequenceGenerator(name="CARTERA_SEQ", sequenceName="CARTERA_SEQ", allocationSize=1)
	@Column(name = "ID")
    private Long id;

	@NotNull
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Column(name = "MANAGEMENT_TYPE")
	private String managementType;

	@Column(name = "PRINEX_ID")
	private String prinexID;

	@Column(name = "CIF")
	private String cif;

	@Column(name = "COMPANY")
	private String company;

	@Column(name = "LOGO")
	private String logo;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "WEBSITE")
	private String website;

	@Column(name = "MANAGER")
	private String manager;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "NO_OF_ASSETS")
	private Integer noOfAssets;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManagementType() {
		return managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public String getPrinexID() {
		return prinexID;
	}

	public void setPrinexID(String prinexID) {
		this.prinexID = prinexID;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getNoOfAssets() {
		return noOfAssets;
	}

	public void setNoOfAssets(Integer noOfAssets) {
		this.noOfAssets = noOfAssets;
	}
	
	

}
