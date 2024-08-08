/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Paul Omboi
 */
@Entity
@Table(name = "quantification")

public class RenalSectionModel implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "sub_id")
    private String instanceId;
    @Column(name = "date_collected")
    private LocalDate dateCollected;
    @Column(name = "org_unit")
    private String orgUnit;
    private String county;
    private String subcounty;
    @Column(name = "facility")        
    private String facilityname;
    private String flevel;
    private String formtype;
    private String essential;
    private String lou;
    private String ven;
    private String funding;
    private String subcategory;
    private String packsize;
    private Integer jandata;
    private Integer febdata;
    private Integer mardata;
    private Integer aprdata;
    private Integer maydata;
    private Integer jundata;
    private Integer juldata;
    private Integer augdata;
    private Integer sepdata;
    private Integer octdata;
    private Integer novdata;
    private Integer decdata;
    private String product_name;
    private Integer price;
    private LocalDate periodfrom;
    private LocalDate periodto;
    private String preparedby;
    private Integer mstock;
    private Integer mcovered;
    private Integer serialno;
    
 
    
    private String status;
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    public RenalSectionModel(){
        
    }
     public RenalSectionModel(RenalSectionPointsBuilder builder){
      
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.product_name=builder.product_name;
           this.price=builder.price;
           this.status = builder.status;
           this.essential=builder.essential;
           this.formtype=builder.formtype;
           this.lou=builder.lou;
           this.ven=builder.ven;
           this.funding=builder.funding;
           this.subcategory=builder.subcategory;
           this.packsize=builder.packsize;
           this.jandata=builder.jandata;
           this.febdata=builder.febdata;
           this.mardata=builder.mardata;
           this.aprdata=builder.aprdata;
           this.maydata=builder.maydata;
           this.jundata=builder.jundata;
           this.juldata=builder.juldata;
           this.augdata=builder.augdata;
           this.sepdata=builder.sepdata;
           this.octdata=builder.octdata;
           this.novdata=builder.novdata;
           this.decdata=builder.decdata;
           this.mcovered=builder.mcovered;
           this.mstock=builder.mstock;
           this.preparedby=builder.preparedby;
           this.periodfrom=builder.periodfrom;
           this.periodto=builder.periodto;
           this.serialno=builder.serialno;
      
    } 

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the instanceId
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId the instanceId to set
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return the dateCollected
     */
    public LocalDate getDateCollected() {
        return dateCollected;
    }

    /**
     * @param dateCollected the dateCollected to set
     */
    public void setDateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
    }

    /**
     * @return the orgUnit
     */
    public String getOrgUnit() {
        return orgUnit;
    }

    /**
     * @param orgUnit the orgUnit to set
     */
    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    /**
     * @return the county
     */
    public String getCounty() {
        return county;
    }

    /**
     * @param county the county to set
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return the subcounty
     */
    public String getSubcounty() {
        return subcounty;
    }

    /**
     * @param subcounty the subcounty to set
     */
    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    /**
     * @return the facilityname
     */
    public String getFacilityname() {
        return facilityname;
    }

    /**
     * @param facilityname the facilityname to set
     */
    public void setFacilityname(String facilityname) {
        this.facilityname = facilityname;
    }

    /**
     * @return the flevel
     */
    public String getFlevel() {
        return flevel;
    }

    /**
     * @param flevel the flevel to set
     */
    public void setFlevel(String flevel) {
        this.flevel = flevel;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
        /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param product_name the product_name to set
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return the price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Integer price) {
        this.price = price;
    }
    
        /**
     * @return the formtype
     */
    public String getFormtype() {
        return formtype;
    }

    /**
     * @param formtype the formtype to set
     */
    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    /**
     * @return the essential
     */
    public String getEssential() {
        return essential;
    }

    /**
     * @param essential the essential to set
     */
    public void setEssential(String essential) {
        this.essential = essential;
    }

    /**
     * @return the lou
     */
    public String getLou() {
        return lou;
    }

    /**
     * @param lou the lou to set
     */
    public void setLou(String lou) {
        this.lou = lou;
    }

    /**
     * @return the ven
     */
    public String getVen() {
        return ven;
    }

    /**
     * @param ven the ven to set
     */
    public void setVen(String ven) {
        this.ven = ven;
    }

    /**
     * @return the funding
     */
    public String getFunding() {
        return funding;
    }

    /**
     * @param funding the funding to set
     */
    public void setFunding(String funding) {
        this.funding = funding;
    }

    /**
     * @return the subcategory
     */
    public String getSubcategory() {
        return subcategory;
    }

    /**
     * @param subcategory the subcategory to set
     */
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    /**
     * @return the packsize
     */
    public String getPacksize() {
        return packsize;
    }

    /**
     * @param packsize the packsize to set
     */
    public void setPacksize(String packsize) {
        this.packsize = packsize;
    }

    /**
     * @return the jandata
     */
    public Integer getJandata() {
        return jandata;
    }

    /**
     * @param jandata the jandata to set
     */
    public void setJandata(Integer jandata) {
        this.jandata = jandata;
    }

    /**
     * @return the febdata
     */
    public Integer getFebdata() {
        return febdata;
    }

    /**
     * @param febdata the febdata to set
     */
    public void setFebdata(Integer febdata) {
        this.febdata = febdata;
    }

    /**
     * @return the mardata
     */
    public Integer getMardata() {
        return mardata;
    }

    /**
     * @param mardata the mardata to set
     */
    public void setMardata(Integer mardata) {
        this.mardata = mardata;
    }

    /**
     * @return the aprdata
     */
    public Integer getAprdata() {
        return aprdata;
    }

    /**
     * @param aprdata the aprdata to set
     */
    public void setAprdata(Integer aprdata) {
        this.aprdata = aprdata;
    }

    /**
     * @return the maydata
     */
    public Integer getMaydata() {
        return maydata;
    }

    /**
     * @param maydata the maydata to set
     */
    public void setMaydata(Integer maydata) {
        this.maydata = maydata;
    }

    /**
     * @return the jundata
     */
    public Integer getJundata() {
        return jundata;
    }

    /**
     * @param jundata the jundata to set
     */
    public void setJundata(Integer jundata) {
        this.jundata = jundata;
    }

    /**
     * @return the juldata
     */
    public Integer getJuldata() {
        return juldata;
    }

    /**
     * @param juldata the juldata to set
     */
    public void setJuldata(Integer juldata) {
        this.juldata = juldata;
    }

    /**
     * @return the augdata
     */
    public Integer getAugdata() {
        return augdata;
    }

    /**
     * @param augdata the augdata to set
     */
    public void setAugdata(Integer augdata) {
        this.augdata = augdata;
    }

    /**
     * @return the sepdata
     */
    public Integer getSepdata() {
        return sepdata;
    }

    /**
     * @param sepdata the sepdata to set
     */
    public void setSepdata(Integer sepdata) {
        this.sepdata = sepdata;
    }

    /**
     * @return the octdata
     */
    public Integer getOctdata() {
        return octdata;
    }

    /**
     * @param octdata the octdata to set
     */
    public void setOctdata(Integer octdata) {
        this.octdata = octdata;
    }

    /**
     * @return the novdata
     */
    public Integer getNovdata() {
        return novdata;
    }

    /**
     * @param novdata the novdata to set
     */
    public void setNovdata(Integer novdata) {
        this.novdata = novdata;
    }

    /**
     * @return the decdata
     */
    public Integer getDecdata() {
        return decdata;
    }

    /**
     * @param decdata the decdata to set
     */
    public void setDecdata(Integer decdata) {
        this.decdata = decdata;
    }
    
       /**
     * @return the periodfrom
     */
    public LocalDate getPeriodfrom() {
        return periodfrom;
    }

    /**
     * @param periodfrom the periodfrom to set
     */
    public void setPeriodfrom(LocalDate periodfrom) {
        this.periodfrom = periodfrom;
    }

    /**
     * @return the periodto
     */
    public LocalDate getPeriodto() {
        return periodto;
    }

    /**
     * @param periodto the periodto to set
     */
    public void setPeriodto(LocalDate periodto) {
        this.periodto = periodto;
    }

    /**
     * @return the preparedby
     */
    public String getPreparedby() {
        return preparedby;
    }

    /**
     * @param preparedby the preparedby to set
     */
    public void setPreparedby(String preparedby) {
        this.preparedby = preparedby;
    }

    /**
     * @return the mstock
     */
    public Integer getMstock() {
        return mstock;
    }

    /**
     * @param mstock the mstock to set
     */
    public void setMstock(Integer mstock) {
        this.mstock = mstock;
    }

    /**
     * @return the mcovered
     */
    public Integer getMcovered() {
        return mcovered;
    }

    /**
     * @param mcovered the mcovered to set
     */
    public void setMcovered(Integer mcovered) {
        this.mcovered = mcovered;
    }
        /**
     * @return the serialno
     */
    public Integer getSerialno() {
        return serialno;
    }

    /**
     * @param serialno the serialno to set
     */
    public void setSerialno(Integer serialno) {
        this.serialno = serialno;
    }
    
      
    public static class RenalSectionPointsBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String flevel;
    private String product_name;
    private Integer price;
    private String status;
    private String formtype;
    private String essential;
    private String lou;
    private String ven;
    private String funding;
    private String subcategory;
    private String packsize;
    private Integer jandata;
    private Integer febdata;
    private Integer mardata;
    private Integer aprdata;
    private Integer maydata;
    private Integer jundata;
    private Integer juldata;
    private Integer augdata;
    private Integer sepdata;
    private Integer octdata;
    private Integer novdata;
    private Integer decdata;
    private LocalDate periodfrom;
    private LocalDate periodto;
    private String preparedby;
    private Integer mstock;
    private Integer mcovered;
    private Integer serialno;
    
    
    public RenalSectionModel.RenalSectionPointsBuilder instanceId(String instanceId) {
      this.instanceId = instanceId;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder county(String county) {
      this.county = county;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder status(String status) {
      this.status = status;
         return this;
    }    
    public RenalSectionModel.RenalSectionPointsBuilder product_name(String product_name) {
      this.product_name = product_name;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder price(int price) {
      this.price = price;
         return this;
    }     
    public RenalSectionModel build() {
            RenalSectionModel renal = new RenalSectionModel(this);
            return renal;
        }
    public RenalSectionModel.RenalSectionPointsBuilder essential(String essential) {
      this.essential = essential;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder formtype(String formtype) {
      this.formtype = formtype;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder lou(String lou) {
      this.lou = lou;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder ven(String ven) {
      this.ven = ven;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder funding(String funding) {
      this.funding = funding;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder subcategory(String subcategory) {
      this.subcategory = subcategory;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder jandata(int jandata) {
      this.jandata = jandata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder febdata(int febdata) {
      this.febdata = febdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder mardata(int mardata) {
      this.mardata = mardata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder aprdata(int aprdata) {
      this.aprdata = aprdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder maydata(int maydata) {
      this.maydata = maydata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder jundata(int jundata) {
      this.jundata = jundata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder juldata(int juldata) {
      this.juldata = juldata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder augdata(int augdata) {
      this.augdata = augdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder sepdata(int sepdata) {
      this.sepdata = sepdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder octdata(int octdata) {
      this.octdata = octdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder novdata(int novdata) {
      this.novdata = novdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder decdata(int decdata) {
      this.decdata = decdata;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder periodfrom(LocalDate periodfrom) {
      this.periodfrom = periodfrom;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder periodto(LocalDate periodto) {
      this.periodto = periodto;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder mstock(int mstock) {
      this.mstock = mstock;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder mcovered(int mcovered) {
      this.mcovered = mcovered;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder preparedby(String preparedby) {
      this.preparedby = preparedby;
         return this;
    }
    public RenalSectionModel.RenalSectionPointsBuilder serialno(int serialno) {
      this.serialno = serialno;
         return this;
    }

        public Object periodfrom(String periodfrom) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
   }   


}
