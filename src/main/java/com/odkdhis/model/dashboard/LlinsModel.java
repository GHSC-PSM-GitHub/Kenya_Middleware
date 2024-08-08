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
@Table(name = "llins")

public class LlinsModel implements Serializable {

        @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(name = "sub_id")
    private String instanceId;

    @Column(name = "date_collected")
    private LocalDate dateCollected;

    @Column(name = "product_name")
    private String product;

    @Column(name = "org_unit")
    private String orgUnit;
    
    private String stCardAvail;
    private int stCountsDone;
    private int dayOutStock;
    private int cardBalance;
    private int amc;
    private String expires6mo;
    private int actualCount;
    private int cmos;
    private int inventoryscore;
    private int stCardScore;
    private int finalscore;
    private int accountabilityscore;
    private int iDenominator;
    private int iNumerator;
    private String county;
    private String subcounty;
    private String facility;
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    
    
    public LlinsModel(){
        
    }
    
        public LlinsModel(LlinsBuilder builder) {
            
        this.stCardAvail = builder.stCardAvail;
        this.stCountsDone = builder.stCountsDone;
        this.dayOutStock = builder.dayOutStock;
        this.cardBalance = builder.cardBalance;
        this.amc = builder.amc;
        this.expires6mo = builder.expires6mo;
        this.actualCount = builder.actualCount;
        this.cmos = builder.cmos;
        this.inventoryscore = builder.inventoryscore;
        this.stCardScore = builder.stCardScore;
        this.finalscore = builder.finalscore;
        this.accountabilityscore = builder.accountabilityscore;
        this.iDenominator = builder.iDenominator;
        this.iNumerator = builder.iNumerator;
        this.county = builder.county;
        this.subcounty = builder.subcounty;
        this.facility = builder.facility;
        this.id = builder.id;
        this.instanceId = builder.instanceId;
        this.dateCollected = builder.dateCollected;
        this.product = builder.product;
        this.orgUnit = builder.orgUnit;
        
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
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
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
     * @return the stCardAvail
     */
    public String getStCardAvail() {
        return stCardAvail;
    }

    /**
     * @param stCardAvail the stCardAvail to set
     */
    public void setStCardAvail(String stCardAvail) {
        this.stCardAvail = stCardAvail;
    }

    /**
     * @return the stCountsDone
     */
    public int getStCountsDone() {
        return stCountsDone;
    }

    /**
     * @param stCountsDone the stCountsDone to set
     */
    public void setStCountsDone(int stCountsDone) {
        this.stCountsDone = stCountsDone;
    }

    /**
     * @return the dayOutStock
     */
    public int getDayOutStock() {
        return dayOutStock;
    }

    /**
     * @param dayOutStock the dayOutStock to set
     */
    public void setDayOutStock(int dayOutStock) {
        this.dayOutStock = dayOutStock;
    }

    /**
     * @return the cardBalance
     */
    public int getCardBalance() {
        return cardBalance;
    }

    /**
     * @param cardBalance the cardBalance to set
     */
    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    /**
     * @return the amc
     */
    public int getAmc() {
        return amc;
    }

    /**
     * @param amc the amc to set
     */
    public void setAmc(int amc) {
        this.amc = amc;
    }

    /**
     * @return the expires6mo
     */
    public String getExpires6mo() {
        return expires6mo;
    }

    /**
     * @param expires6mo the expires6mo to set
     */
    public void setExpires6mo(String expires6mo) {
        this.expires6mo = expires6mo;
    }

    /**
     * @return the actualCount
     */
    public int getActualCount() {
        return actualCount;
    }

    /**
     * @param actualCount the actualCount to set
     */
    public void setActualCount(int actualCount) {
        this.actualCount = actualCount;
    }

    /**
     * @return the cmos
     */
    public int getCmos() {
        return cmos;
    }

    /**
     * @param cmos the cmos to set
     */
    public void setCmos(int cmos) {
        this.cmos = cmos;
    }

    /**
     * @return the inventoryscore
     */
    public int getInventoryscore() {
        return inventoryscore;
    }

    /**
     * @param inventoryscore the inventoryscore to set
     */
    public void setInventoryscore(int inventoryscore) {
        this.inventoryscore = inventoryscore;
    }

    /**
     * @return the stCardScore
     */
    public int getStCardScore() {
        return stCardScore;
    }

    /**
     * @param stCardScore the stCardScore to set
     */
    public void setStCardScore(int stCardScore) {
        this.stCardScore = stCardScore;
    }

    /**
     * @return the finalscore
     */
    public int getFinalscore() {
        return finalscore;
    }

    /**
     * @param finalscore the finalscore to set
     */
    public void setFinalscore(int finalscore) {
        this.finalscore = finalscore;
    }

    /**
     * @return the accountabilityscore
     */
    public int getAccountabilityscore() {
        return accountabilityscore;
    }

    /**
     * @param accountabilityscore the accountabilityscore to set
     */
    public void setAccountabilityscore(int accountabilityscore) {
        this.accountabilityscore = accountabilityscore;
    }

    /**
     * @return the iDenominator
     */
    public int getiDenominator() {
        return iDenominator;
    }

    /**
     * @param iDenominator the iDenominator to set
     */
    public void setiDenominator(int iDenominator) {
        this.iDenominator = iDenominator;
    }

    /**
     * @return the iNumerator
     */
    public int getiNumerator() {
        return iNumerator;
    }

    /**
     * @param iNumerator the iNumerator to set
     */
    public void setiNumerator(int iNumerator) {
        this.iNumerator = iNumerator;
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
     * @return the facility
     */
    public String getFacility() {
        return facility;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(String facility) {
        this.facility = facility;
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
   
        
    public static class LlinsBuilder {

    private String stCardAvail;
    private int stCountsDone;
    private int dayOutStock;
    private int cardBalance;
    private int amc;
    private String expires6mo;
    private int actualCount;
    private int cmos;
    private int stCardScore;
    private int inventoryscore;
    private int finalscore;
    private int accountabilityscore;
    private int iDenominator;
    private int iNumerator;
    private String county;
    private String subcounty;
    private String facility;
    private String id;
    private String instanceId;
    private LocalDate dateCollected;
    private String product;
    private String orgUnit;

    public LlinsBuilder stCardAvail(String stCardAvail) {
        this.stCardAvail = stCardAvail;
        return this;
    }

    public LlinsBuilder stCountsDone(int stCountsDone) {
        this.stCountsDone = stCountsDone;
        return this;
    }

    public LlinsBuilder dayOutStock(int dayOutStock) {
        this.dayOutStock = dayOutStock;
        return this;
    }

    public LlinsBuilder cardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
        return this;
    }

    public LlinsBuilder amc(int amc) {
        this.amc = amc;
        return this;
    }

    public LlinsBuilder expires6mo(String expires6mo) {
        this.expires6mo = expires6mo;
        return this;
    }

    public LlinsBuilder actualCount(int actualCount) {
        this.actualCount = actualCount;
        return this;
    }
    
    public LlinsBuilder stCardScore(int stCardScore) {
        this.stCardScore = stCardScore;
        return this;
    }
    public LlinsBuilder cmos(int cmos) {
        this.cmos = cmos;
        return this;
    }
    public LlinsBuilder inventoryscore(int inventoryscore) {
        this.inventoryscore = inventoryscore;
        return this;
    }
    public LlinsBuilder accountabilityscore(int accountabilityscore) {
        this.accountabilityscore = accountabilityscore;
        return this;
    }
    public LlinsBuilder finalscore(int finalscore) {
        this.finalscore = finalscore;
        return this;
    }

    public LlinsBuilder iDenominator(int iDenominator) {
        this.iDenominator = iDenominator;
        return this;
    }

    public LlinsBuilder iNumerator(int iNumerator) {
        this.iNumerator = iNumerator;
        return this;
    }

    public LlinsBuilder county(String county) {
        this.county = county;
        return this;
    }
    public LlinsBuilder subcounty(String subcounty) {
        this.subcounty = subcounty;
        return this;
    }
    public LlinsBuilder facility(String facility) {
        this.facility = facility;
        return this;
    }
    

    public LlinsBuilder id(String id) {
        this.id = id;
        return this;
    }

    public LlinsBuilder instanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public LlinsBuilder dateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
        return this;
    }

    public LlinsBuilder product(String product) {
        this.product = product;
        return this;
    }

    public LlinsBuilder orgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
        return this;
    }
   
     public LlinsModel build() {
            LlinsModel llins = new LlinsModel(this);
            return llins;
        }
            
   }
        /**
     * @return the submissionId
     */
    public String getSubmissionId() {
        return instanceId;
    }

    /**
     * @param submissionId the submissionId to set
     */
    public void setSubmissionId(String submissionId) {
        this.instanceId = submissionId;
    }

}
