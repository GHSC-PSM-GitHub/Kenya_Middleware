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
@Table(name = "storageassement")

public class StorageAssessmentModel implements Serializable {

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
    
    private String storeType;
    private int storeCleaning;
    private int dSunlight;
    private int vermin;
    private int shelvelPallets;
    private int lighting;
    private int functioningThermomenter;
    private int tempchart;
    private int functioningThermomenter2;
    private int tempchart2;
    private int arrangement;
    private int hazardoursMaterial;
    private int burglarproof;
    private int expiredseparated;
    private int f058;
    private int bucket;
    private int numerator;
    private int denominator;
    private int score;
    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    public StorageAssessmentModel(){
        
    }
     public StorageAssessmentModel(storageAssessmentsBuilder builder){
      
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.storeType=builder.storeType;
           this.storeCleaning = builder.storeCleaning;
           this.dSunlight= builder.dSunlight;
           this.vermin= builder.vermin;
           this.shelvelPallets= builder.shelvelPallets;
           this.lighting= builder.lighting;
           this.functioningThermomenter = builder.functioningThermomenter;
           this.tempchart=builder.tempchart;
           this.functioningThermomenter2= builder.functioningThermomenter2;
           this.tempchart2= builder.tempchart2;
           this.arrangement= builder.arrangement;
           this.hazardoursMaterial=builder.hazardoursMaterial;
           this.burglarproof= builder.burglarproof;
           this.expiredseparated= builder.expiredseparated;
           this.f058= builder.f058;
           this.bucket= builder.bucket;
           this.numerator= builder.numerator;
           this.denominator= builder.denominator;
           this.score= builder.score;

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
    
    
    public static class storageAssessmentsBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String flevel;
    private String storeType;
    private int storeCleaning;
    private int dSunlight;
    private int vermin;
    private int shelvelPallets;
    private int lighting;
    private int functioningThermomenter;
    private int tempchart;
    private int functioningThermomenter2;
    private int tempchart2;
    private int arrangement;
    private int hazardoursMaterial;
    private int burglarproof;
    private int expiredseparated;
    private int f058;
    private int bucket;
    private int numerator;
    private int denominator;
    private int score;
    

    
    public StorageAssessmentModel.storageAssessmentsBuilder instanceId(String instanceId) {
      this.instanceId = instanceId;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder county(String county) {
      this.county = county;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    } 
    public StorageAssessmentModel.storageAssessmentsBuilder storeType(String storeType) {
      this.storeType = storeType;
         return this;
    } 
    public StorageAssessmentModel.storageAssessmentsBuilder storeCleaning(int storeCleaning) {
      this.storeCleaning = storeCleaning;
         return this;
    }    
    public StorageAssessmentModel.storageAssessmentsBuilder dSunlight(int dSunlight) {
      this.dSunlight = dSunlight;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder vermin(int vermin) {
      this.vermin = vermin;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder shelvelPallets(int shelvelPallets) {
      this.shelvelPallets = shelvelPallets;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder lighting(int lighting) {
      this.lighting = lighting;
         return this;
    }
    public StorageAssessmentModel.storageAssessmentsBuilder functioningThermomenter(int functioningThermomenter) {
      this.functioningThermomenter = functioningThermomenter;
         return this;
    }   
    public StorageAssessmentModel.storageAssessmentsBuilder tempchart(int tempchart) {
      this.tempchart = tempchart;
         return this;
    }    
    public StorageAssessmentModel.storageAssessmentsBuilder functioningThermomenter2(int functioningThermomenter2) {
      this.functioningThermomenter2 = functioningThermomenter2;
         return this;
    }
     public StorageAssessmentModel.storageAssessmentsBuilder tempchart2(int tempchart2) {
      this.tempchart2 = tempchart2;
         return this;
    }   
    public StorageAssessmentModel.storageAssessmentsBuilder arrangement(int arrangement) {
      this.arrangement = arrangement;
         return this;
    }    
    public StorageAssessmentModel.storageAssessmentsBuilder hazardoursMaterial(int hazardoursMaterial) {
      this.hazardoursMaterial = hazardoursMaterial;
         return this;
    }    
    public StorageAssessmentModel.storageAssessmentsBuilder burglarproof(int burglarproof) {
      this.burglarproof = burglarproof;
         return this;
    }    
    public StorageAssessmentModel.storageAssessmentsBuilder expiredseparated(int expiredseparated) {
      this.expiredseparated = expiredseparated;
         return this;
    }      
    public StorageAssessmentModel.storageAssessmentsBuilder f058(int f058) {
      this.f058 = f058;
         return this;
    }      
     public StorageAssessmentModel.storageAssessmentsBuilder bucket(int bucket) {
      this.bucket = bucket;
         return this;
    }     
    public StorageAssessmentModel.storageAssessmentsBuilder numerator(int numerator) {
      this.numerator = numerator;
         return this;
    }      
    public StorageAssessmentModel.storageAssessmentsBuilder denominator(int denominator) {
      this.denominator = denominator;
         return this;
    }     
    public StorageAssessmentModel.storageAssessmentsBuilder score(int score) {
      this.score = score;
         return this;
    }        
    
    public StorageAssessmentModel build() {
            StorageAssessmentModel storageAssessments = new StorageAssessmentModel(this);
            return storageAssessments;
        }
    }   

    /**
     * @return the storeType
     */
    public String getStoreType() {
        return storeType;
    }

    /**
     * @param storeType the storeType to set
     */
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    /**
     * @return the storeCleaning
     */
    public int getStoreCleaning() {
        return storeCleaning;
    }

    /**
     * @param storeCleaning the storeCleaning to set
     */
    public void setStoreCleaning(int storeCleaning) {
        this.storeCleaning = storeCleaning;
    }

    /**
     * @return the dSunlight
     */
    public int getdSunlight() {
        return dSunlight;
    }

    /**
     * @param dSunlight the dSunlight to set
     */
    public void setdSunlight(int dSunlight) {
        this.dSunlight = dSunlight;
    }

    /**
     * @return the vermin
     */
    public int getVermin() {
        return vermin;
    }

    /**
     * @param vermin the vermin to set
     */
    public void setVermin(int vermin) {
        this.vermin = vermin;
    }

    /**
     * @return the shelvelPallets
     */
    public int getShelvelPallets() {
        return shelvelPallets;
    }

    /**
     * @param shelvelPallets the shelvelPallets to set
     */
    public void setShelvelPallets(int shelvelPallets) {
        this.shelvelPallets = shelvelPallets;
    }

    /**
     * @return the lighting
     */
    public int getLighting() {
        return lighting;
    }

    /**
     * @param lighting the lighting to set
     */
    public void setLighting(int lighting) {
        this.lighting = lighting;
    }

    /**
     * @return the functioningThermomenter
     */
    public int getFunctioningThermomenter() {
        return functioningThermomenter;
    }

    /**
     * @param functioningThermomenter the functioningThermomenter to set
     */
    public void setFunctioningThermomenter(int functioningThermomenter) {
        this.functioningThermomenter = functioningThermomenter;
    }

    /**
     * @return the tempchart
     */
    public int getTempchart() {
        return tempchart;
    }

    /**
     * @param tempchart the tempchart to set
     */
    public void setTempchart(int tempchart) {
        this.tempchart = tempchart;
    }

    /**
     * @return the functioningThermomenter2
     */
    public int getFunctioningThermomenter2() {
        return functioningThermomenter2;
    }

    /**
     * @param functioningThermomenter2 the functioningThermomenter2 to set
     */
    public void setFunctioningThermomenter2(int functioningThermomenter2) {
        this.functioningThermomenter2 = functioningThermomenter2;
    }

    /**
     * @return the tempchart2
     */
    public int getTempchart2() {
        return tempchart2;
    }

    /**
     * @param tempchart2 the tempchart2 to set
     */
    public void setTempchart2(int tempchart2) {
        this.tempchart2 = tempchart2;
    }

    /**
     * @return the arrangement
     */
    public int getArrangement() {
        return arrangement;
    }

    /**
     * @param arrangement the arrangement to set
     */
    
    public void setArrangement(int arrangement) {
        this.arrangement = arrangement;
    } 

    /**
     * @return the hazardoursMaterial
     */
    public int getHazardoursMaterial() {
        return hazardoursMaterial;
    }

    /**
     * @param hazardoursMaterial the hazardoursMaterial to set
     */
    public void setHazardoursMaterial(int hazardoursMaterial) {
        this.hazardoursMaterial = hazardoursMaterial;
    }

    /**
     * @return the burglarproof
     */
    public int getBurglarproof() {
        return burglarproof;
    }

    /**
     * @param burglarproof the burglarproof to set
     */
    public void setBurglarproof(int burglarproof) {
        this.burglarproof = burglarproof;
    }

    /**
     * @return the expiredseparated
     */
    public int getExpiredseparated() {
        return expiredseparated;
    }

    /**
     * @param expiredseparated the expiredseparated to set
     */
    public void setExpiredseparated(int expiredseparated) {
        this.expiredseparated = expiredseparated;
    }

    /**
     * @return the f058
     */
    public int getF058() {
        return f058;
    }

    /**
     * @param f058 the f058 to set
     */
    public void setF058(int f058) {
        this.f058 = f058;
    }

    /**
     * @return the bucket
     */
    public int getBucket() {
        return bucket;
    }

    /**
     * @param bucket the bucket to set
     */
    public void setBucket(int bucket) {
        this.bucket = bucket;
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
      
}
