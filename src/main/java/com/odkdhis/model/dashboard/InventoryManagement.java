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
@Table(name = "inventorymgt")
//@Table(name = "inventorymgt2")

public class InventoryManagement implements Serializable {

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
    private int cmos1;
    private int cmos2;
    private int stCardScore;
    private int stCardsCountScore;
    private int daysofStockScore;
    private int iDenominator;
    private int iNumerator;
    private String county;
    private String subcounty;
    private String facility;
    @Column(name = "numerator")
    private int numerator;
    @Column(name = "denominator")
    private int denominator;
    @Column(name = "score")
    private int score;
    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    @Transient
    private String stockstatus;
    
    
    
    public InventoryManagement(){
        
    }
    
        public InventoryManagement(InventoryManagementBuilder builder) {
            
        this.stCardAvail = builder.stCardAvail;
        this.stCountsDone = builder.stCountsDone;
        this.dayOutStock = builder.dayOutStock;
        this.cardBalance = builder.cardBalance;
        this.amc = builder.amc;
        this.expires6mo = builder.expires6mo;
        this.actualCount = builder.actualCount;
        this.cmos1 = builder.cmos1;
        this.cmos2 = builder.cmos2;
        this.stCardScore = builder.stCardScore;
        this.stCardsCountScore = builder.stCardsCountScore;
        this.daysofStockScore = builder.daysofStockScore;
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
        this.numerator=builder.numerator;
        this.denominator=builder.denominator;
        this.score=builder.score;
        this.stockstatus=builder.getStockstatus();
     
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
     * @return the cmos1
     */
    public int getCmos1() {
        return cmos1;
    }

    /**
     * @param cmos1 the cmos1 to set
     */
    public void setCmos1(int cmos1) {
        this.cmos1 = cmos1;
    }

    /**
     * @return the cmos2
     */
    public int getCmos2() {
        return cmos2;
    }

    /**
     * @param cmos2 the cmos2 to set
     */
    public void setCmos2(int cmos2) {
        this.cmos2 = cmos2;
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
     * @return the stCardsCountScore
     */
    public int getStCardsCountScore() {
        return stCardsCountScore;
    }

    /**
     * @param stCardsCountScore the stCardsCountScore to set
     */
    public void setStCardsCountScore(int stCardsCountScore) {
        this.stCardsCountScore = stCardsCountScore;
    }

    /**
     * @return the daysofStockScore
     */
    public int getDaysofStockScore() {
        return daysofStockScore;
    }

    /**
     * @param daysofStockScore the daysofStockScore to set
     */
    public void setDaysofStockScore(int daysofStockScore) {
        this.daysofStockScore = daysofStockScore;
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
    
         
    public String getStockstatus() {
        return stockstatus;
    }


    public void setStockstatus(String stockstatus) {
        this.stockstatus = stockstatus;
    }
    
        /**
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
    
 
        
    public static class InventoryManagementBuilder {

    private String stCardAvail;
    private int stCountsDone;
    private int dayOutStock;
    private int cardBalance;
    private int amc;
    private String expires6mo;
    private int actualCount;
    private int cmos1;
    private int cmos2;
    private int stCardScore;
    private int stCardsCountScore;
    private int daysofStockScore;
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
    private int numerator;
    private int denominator;
    private int score;
    private String stockstatus;

    public InventoryManagementBuilder stCardAvail(String stCardAvail) {
        this.stCardAvail = stCardAvail;
        return this;
    }

    public InventoryManagementBuilder stCountsDone(int stCountsDone) {
        this.stCountsDone = stCountsDone;
        return this;
    }

    public InventoryManagementBuilder dayOutStock(int dayOutStock) {
        this.dayOutStock = dayOutStock;
        return this;
    }

    public InventoryManagementBuilder cardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
        return this;
    }

    public InventoryManagementBuilder amc(int amc) {
        this.amc = amc;
        return this;
    }

    public InventoryManagementBuilder expires6mo(String expires6mo) {
        this.expires6mo = expires6mo;
        return this;
    }

    public InventoryManagementBuilder actualCount(int actualCount) {
        this.actualCount = actualCount;
        return this;
    }

    public InventoryManagementBuilder cmos1(int cmos1) {
        this.cmos1 = cmos1;
        return this;
    }

    public InventoryManagementBuilder cmos2(int cmos2) {
        this.cmos2 = cmos2;
        return this;
    }

    public InventoryManagementBuilder stCardScore(int stCardScore) {
        this.stCardScore = stCardScore;
        return this;
    }

    public InventoryManagementBuilder stCardsCountScore(int stCardsCountScore) {
        this.stCardsCountScore = stCardsCountScore;
        return this;
    }

    public InventoryManagementBuilder daysofStockScore(int daysofStockScore) {
        this.daysofStockScore = daysofStockScore;
        return this;
    }

    public InventoryManagementBuilder iDenominator(int iDenominator) {
        this.iDenominator = iDenominator;
        return this;
    }

    public InventoryManagementBuilder iNumerator(int iNumerator) {
        this.iNumerator = iNumerator;
        return this;
    }
    public InventoryManagementBuilder numerator(int numerator) {
        this.numerator = numerator;
        return this;
    }
    public InventoryManagementBuilder denominator(int denominator) {
        this.denominator = denominator;
        return this;
    }
    public InventoryManagementBuilder score(int score) {
        this.score = score;
        return this;
    }    
    public InventoryManagementBuilder county(String county) {
        this.county = county;
        return this;
    }
    public InventoryManagementBuilder subcounty(String subcounty) {
        this.subcounty = subcounty;
        return this;
    }
    public InventoryManagementBuilder facility(String facility) {
        this.facility = facility;
        return this;
    }
    

    public InventoryManagementBuilder id(String id) {
        this.id = id;
        return this;
    }

    public InventoryManagementBuilder instanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public InventoryManagementBuilder dateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
        return this;
    }

    public InventoryManagementBuilder product(String product) {
        this.product = product;
        return this;
    }

    public InventoryManagementBuilder orgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
        return this;
    }
    
    public InventoryManagementBuilder stockstatus(String stockstatus) {
        this.setStockstatus(stockstatus);
        return this;
    }
   
     public InventoryManagement build() {
            InventoryManagement inventory = new InventoryManagement(this);
            return inventory;
        }

        /**
         * @return the stockstatus
         */
        public String getStockstatus() {
            return stockstatus;
        }

        /**
         * @param stockstatus the stockstatus to set
         */
        public void setStockstatus(String stockstatus) {
            this.stockstatus = stockstatus;
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
