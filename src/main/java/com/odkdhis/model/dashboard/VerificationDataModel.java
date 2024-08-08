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
@Table(name = "verificationdata")

public class VerificationDataModel implements Serializable {

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
    private String county;
    private String subcounty;
    private String facility;
    
    private String stocked;
    private int qtyDardispense;
    private int qtycdrrdispensed;
    private int qtydhisdispensed;
    private int physicalCountCdrr;
    private int physicalCountdhis;
    private int varianceDarMsf;
    private int varianceMsfDhis;
    private int varianceCdrrDhis;
    private int numerator;
    private int denominator;
    private int score;  

    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    
    
    public VerificationDataModel(){
        
    }
    
        public VerificationDataModel(VerificationDataBuilder builder) {
        this.county = builder.county;
        this.subcounty = builder.subcounty;
        this.facility = builder.facility;
        this.id = builder.id;
        this.instanceId = builder.instanceId;
        this.dateCollected = builder.dateCollected;
        this.product = builder.product;
        this.orgUnit = builder.orgUnit;
        this.stocked=builder.stocked;
        this.qtyDardispense=builder.qtyDardispense;
        this.qtycdrrdispensed=builder.qtycdrrdispensed;
        this.qtydhisdispensed=builder.qtydhisdispensed;
        this.physicalCountCdrr=builder.physicalCountCdrr;
        this.physicalCountdhis=builder.physicalCountdhis;
        this.varianceDarMsf=builder.varianceDarMsf;
        this.varianceMsfDhis=builder.varianceMsfDhis;
        this.varianceCdrrDhis=builder.varianceCdrrDhis;
        this.numerator=builder.numerator;
        this.denominator=builder.denominator;
        this.score=builder.score;
        
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

    /**
     * @return the stocked
     */
    public String getStocked() {
        return stocked;
    }

    /**
     * @param stocked the stocked to set
     */
    public void setStocked(String stocked) {
        this.stocked = stocked;
    }

    /**
     * @return the qtyDardispense
     */
    public int getQtyDardispense() {
        return qtyDardispense;
    }

    /**
     * @param qtyDardispense the qtyDardispense to set
     */
    public void setQtyDardispense(int qtyDardispense) {
        this.qtyDardispense = qtyDardispense;
    }

    /**
     * @return the qtycdrrdispensed
     */
    public int getQtycdrrdispensed() {
        return qtycdrrdispensed;
    }

    /**
     * @param qtycdrrdispensed the qtycdrrdispensed to set
     */
    public void setQtycdrrdispensed(int qtycdrrdispensed) {
        this.qtycdrrdispensed = qtycdrrdispensed;
    }

    /**
     * @return the qtydhisdispensed
     */
    public int getQtydhisdispensed() {
        return qtydhisdispensed;
    }

    /**
     * @param qtydhisdispensed the qtydhisdispensed to set
     */
    public void setQtydhisdispensed(int qtydhisdispensed) {
        this.qtydhisdispensed = qtydhisdispensed;
    }

    /**
     * @return the physicalCountCdrr
     */
    public int getPhysicalCountCdrr() {
        return physicalCountCdrr;
    }

    /**
     * @param physicalCountCdrr the physicalCountCdrr to set
     */
    public void setPhysicalCountCdrr(int physicalCountCdrr) {
        this.physicalCountCdrr = physicalCountCdrr;
    }

    /**
     * @return the physicalCountdhis
     */
    public int getPhysicalCountdhis() {
        return physicalCountdhis;
    }

    /**
     * @param physicalCountdhis the physicalCountdhis to set
     */
    public void setPhysicalCountdhis(int physicalCountdhis) {
        this.physicalCountdhis = physicalCountdhis;
    }

    /**
     * @return the varianceDarMsf
     */
    public int getVarianceDarMsf() {
        return varianceDarMsf;
    }

    /**
     * @param varianceDarMsf the varianceDarMsf to set
     */
    public void setVarianceDarMsf(int varianceDarMsf) {
        this.varianceDarMsf = varianceDarMsf;
    }

    /**
     * @return the varianceMsfDhis
     */
    public int getVarianceMsfDhis() {
        return varianceMsfDhis;
    }

    /**
     * @param varianceMsfDhis the varianceMsfDhis to set
     */
    public void setVarianceMsfDhis(int varianceMsfDhis) {
        this.varianceMsfDhis = varianceMsfDhis;
    }

    /**
     * @return the varianceCdrrDhis
     */
    public int getVarianceCdrrDhis() {
        return varianceCdrrDhis;
    }

    /**
     * @param varianceCdrrDhis the varianceCdrrDhis to set
     */
    public void setVarianceCdrrDhis(int varianceCdrrDhis) {
        this.varianceCdrrDhis = varianceCdrrDhis;
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
   
        
    public static class VerificationDataBuilder {
    private String county;
    private String subcounty;
    private String facility;
    private String id;
    private String instanceId;
    private LocalDate dateCollected;
    private String product;
    private String orgUnit;
    
    private String stocked;
    private int qtyDardispense;
    private int qtycdrrdispensed;
    private int qtydhisdispensed;
    private int physicalCountCdrr;
    private int physicalCountdhis;
    private int varianceDarMsf;
    private int varianceMsfDhis;
    private int varianceCdrrDhis;
    private int numerator;
    private int denominator;
    private int score;
    
    

    public VerificationDataBuilder county(String county) {
        this.county = county;
        return this;
    }
    public VerificationDataBuilder subcounty(String subcounty) {
        this.subcounty = subcounty;
        return this;
    }
    public VerificationDataBuilder facility(String facility) {
        this.facility = facility;
        return this;
    }
    

    public VerificationDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public VerificationDataBuilder instanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public VerificationDataBuilder dateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
        return this;
    }

    public VerificationDataBuilder product(String product) {
        this.product = product;
        return this;
    }

    public VerificationDataBuilder orgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
        return this;
    }
    public VerificationDataBuilder stocked(String stocked) {
        this.stocked = stocked;
        return this;
    }
    public VerificationDataBuilder qtyDardispense(int qtyDardispense) {
        this.qtyDardispense = qtyDardispense;
        return this;
    }
    public VerificationDataBuilder qtycdrrdispensed(int qtycdrrdispensed) {
        this.qtycdrrdispensed = qtycdrrdispensed;
        return this;
    }    
    public VerificationDataBuilder qtydhisdispensed(int qtydhisdispensed) {
        this.qtydhisdispensed = qtydhisdispensed;
        return this;
    }    
    public VerificationDataBuilder physicalCountCdrr(int physicalCountCdrr) {
        this.physicalCountCdrr = physicalCountCdrr;
        return this;
    }    
    public VerificationDataBuilder physicalCountdhis(int physicalCountdhis) {
        this.physicalCountdhis = physicalCountdhis;
        return this;
    }    
    public VerificationDataBuilder varianceDarMsf(int varianceDarMsf) {
        this.varianceDarMsf = varianceDarMsf;
        return this;
    }    
    public VerificationDataBuilder varianceMsfDhis(int varianceMsfDhis) {
        this.varianceMsfDhis = varianceMsfDhis;
        return this;
    }    
    public VerificationDataBuilder varianceCdrrDhis(int varianceCdrrDhis) {
        this.varianceCdrrDhis = varianceCdrrDhis;
        return this;
    }    
    public VerificationDataBuilder numerator(int numerator) {
        this.numerator = numerator;
        return this;
    }    
    public VerificationDataBuilder denominator(int denominator) {
        this.denominator = denominator;
        return this;
    }    
    public VerificationDataBuilder score(int score) {
        this.score = score;
        return this;
    }        
    
     
    
     public VerificationDataModel build() {
            VerificationDataModel verificationData = new VerificationDataModel(this);
            return verificationData;
        }
            
   }
 
}
