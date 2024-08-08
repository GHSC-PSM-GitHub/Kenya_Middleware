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
@Table(name = "previousactionpoints")

public class ActionPointsReviewModel implements Serializable {

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
    private String actionpoint;
    private String presponsible;
    private LocalDate duedate;
    private String status;
    private String inchargeNames;
    private String inchargeEmail;
    private String phonenumber;
    private String gpscordinates;
    
    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    public ActionPointsReviewModel(){
        
    }
     public ActionPointsReviewModel(previousActionPointsBuilder builder){
      
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.actionpoint = builder.actionpoint;
           this.presponsible = builder.presponsible;
           this.duedate = builder.duedate;
           this.status = builder.status;
           this.inchargeNames=builder.inchargeNames;
           this.inchargeEmail=builder.inchargeEmail;
           this.phonenumber=builder.phonenumber;
           this.gpscordinates=builder.gpscordinates;
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
     * @return the actionpoint
     */
    public String getActionpoint() {
        return actionpoint;
    }

    /**
     * @param actionpoint the actionpoint to set
     */
    public void setActionpoint(String actionpoint) {
        this.actionpoint = actionpoint;
    }

    /**
     * @return the presponsible
     */
    public String getPresponsible() {
        return presponsible;
    }

    /**
     * @param presponsible the presponsible to set
     */
    public void setPresponsible(String presponsible) {
        this.presponsible = presponsible;
    }

    
    /**
     * @return the duedate
     */
    public LocalDate getDuedate() {
        return duedate;
    }

    /**
     * @param duedate the duedate to set
     */
    public void setDuedate(LocalDate duedate) {
        this.duedate = duedate;
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
     * @return the inchargeNames
     */
    public String getInchargeNames() {
        return inchargeNames;
    }

    /**
     * @param inchargeNames the inchargeNames to set
     */
    public void setInchargeNames(String inchargeNames) {
        this.inchargeNames = inchargeNames;
    }

    /**
     * @return the inchargeEmail
     */
    public String getInchargeEmail() {
        return inchargeEmail;
    }

    /**
     * @param inchargeEmail the inchargeEmail to set
     */
    public void setInchargeEmail(String inchargeEmail) {
        this.inchargeEmail = inchargeEmail;
    }

    /**
     * @return the phonenumber
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * @param phonenumber the phonenumber to set
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    /**
     * @return the gpscordinates
     */
    public String getGpscordinates() {
        return gpscordinates;
    }

    /**
     * @param gpscordinates the gpscordinates to set
     */
    public void setGpscordinates(String gpscordinates) {
        this.gpscordinates = gpscordinates;
    }

    
    
    
    public static class previousActionPointsBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String flevel;
    private String actionpoint;
    private String presponsible;
    private LocalDate duedate;
    private String status;
    private String inchargeNames;
    private String inchargeEmail;
    private String phonenumber;
    private String gpscordinates;
    
    
    public ActionPointsReviewModel.previousActionPointsBuilder instanceId(String instanceId) {
      this.instanceId = instanceId;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder county(String county) {
      this.county = county;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder actionpoint(String actionpoint) {
      this.actionpoint = actionpoint;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder presponsible(String presponsible) {
      this.presponsible = presponsible;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder duedate(LocalDate duedate) {
      this.duedate = duedate;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder status(String status) {
      this.status = status;
         return this;
    }    
    public ActionPointsReviewModel.previousActionPointsBuilder inchargeNames(String inchargeNames) {
      this.inchargeNames = inchargeNames;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder inchargeEmail(String inchargeEmail) {
      this.inchargeEmail = inchargeEmail;
         return this;
    }
    public ActionPointsReviewModel.previousActionPointsBuilder phonenumber(String phonenumber) {
      this.phonenumber = phonenumber;
         return this; 
    } 
    public ActionPointsReviewModel.previousActionPointsBuilder gpscordinates(String gpscordinates) {
      this.gpscordinates = gpscordinates;
         return this; 
    }
    public ActionPointsReviewModel build() {
            ActionPointsReviewModel previousPointsReview = new ActionPointsReviewModel(this);
            return previousPointsReview;
        }
    }   
     
}
