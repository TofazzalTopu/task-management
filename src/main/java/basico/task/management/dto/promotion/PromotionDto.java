package basico.task.management.dto.promotion;

import basico.task.management.util.ValidDate;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PromotionDto {

    private String name;
    @NotNull
    private Long portfolioId;
    @NotNull
    private Long carteraId;
    private String society;
    private String grouper;
    private Double LONGITUDE;
    private Double Latitude;
    @ValidDate
    private String promotionDate;
    private Long managmentType;
    private int Code;
    private String refCat;
    private String ccaa;
    private String province;
    private String municipality;
    private String postalCode;
    private String via;
    private String nameVia;
    private String numbers;
    @ValidDate
    private String constructionDate;
    private String households;
    private String parking;
    private String storageRooms;
    private String local;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getCarteraId() {
        return carteraId;
    }

    public void setCarteraId(Long carteraId) {
        this.carteraId = carteraId;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getGrouper() {
        return grouper;
    }

    public void setGrouper(String grouper) {
        this.grouper = grouper;
    }

    public Double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(Double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getPromotionDate() {
        return promotionDate;
    }

    public void setPromotionDate(String promotionDate) {
        this.promotionDate = promotionDate;
    }

    public Long getManagmentType() {
        return managmentType;
    }

    public void setManagmentType(Long managmentType) {
        this.managmentType = managmentType;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getRefCat() {
        return refCat;
    }

    public void setRefCat(String refCat) {
        this.refCat = refCat;
    }

    public String getCcaa() {
        return ccaa;
    }

    public void setCcaa(String ccaa) {
        this.ccaa = ccaa;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getNameVia() {
        return nameVia;
    }

    public void setNameVia(String nameVia) {
        this.nameVia = nameVia;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(String constructionDate) {
        this.constructionDate = constructionDate;
    }

    public String getHouseholds() {
        return households;
    }

    public void setHouseholds(String households) {
        this.households = households;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getStorageRooms() {
        return storageRooms;
    }

    public void setStorageRooms(String storageRooms) {
        this.storageRooms = storageRooms;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
