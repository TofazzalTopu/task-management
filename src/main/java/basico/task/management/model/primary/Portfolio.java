package basico.task.management.model.primary;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PORTFOLIO_SEQ")
    @SequenceGenerator(name = "PORTFOLIO_SEQ", sequenceName = "PORTFOLIO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

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
    @Column(name = "P_DATE")
    private Date productionDate;


    @Column(name = "NO_ASSET")
    private String numberofAsset;


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


    public Date getProductionDate() {
        return productionDate;
    }


    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }


    public String getNumberofAsset() {
        return numberofAsset;
    }


    public void setNumberofAsset(String numberofAsset) {
        this.numberofAsset = numberofAsset;
    }


}
