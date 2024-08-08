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
@Table(name = "currentactionpoints")

public class CurrentActionPointsModel implements Serializable {

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
    
    
    private String smartactionpoint;
    private String findings;
    private String actionDone;
    private String recommedation;
    private String presponsible;
    private LocalDate duedate;
    
    @Column(name = "programarea")
    private String programarea;
    @Column(name = "commoditya")
    private String commoditya;
    @Column(name = "actionDone2")
    private int actionDone2;
    @Column(name = "otherfinding")
    private String otherfinding;
    @Column(name = "otherintervention")
    private String otherintervention;

    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    public CurrentActionPointsModel(){
        
    }
     public CurrentActionPointsModel(currentActionPointsBuilder builder){
      
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.smartactionpoint = builder.actionpoint;
           this.presponsible = builder.presponsible;
           this.duedate = builder.duedate;
           this.findings=builder.findings;
           this.actionDone=builder.actionDone;
           this.recommedation=builder.recommedation; 
           this.programarea=builder.programarea;
           this.commoditya=builder.commoditya;
           this.actionDone2=builder.actionDone2;
           this.otherfinding=builder.otherfinding;
           this.otherintervention=builder.otherintervention;
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
    public String getSmartActionpoint() {
        return smartactionpoint;
    }

    /**
     * @param actionpoint the actionpoint to set
     */
    public void setSmartActionpoint(String actionpoint) {
        this.smartactionpoint = actionpoint;
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
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    

    /**
     * @return the commoditya
     */
    public String getCommoditya() {
        return commoditya;
    }

    /**
     * @param commoditya the commoditya to set
     */
    public void setCommoditya(String commoditya) {
        this.commoditya = commoditya;
    }

    /**
     * @return the actionDone2
     */
    public int getActionDone2() {
        return actionDone2;
    }

    /**
     * @param actionDone2 the actionDone2 to set
     */
    public void setActionDone2(int actionDone2) {
        this.actionDone2 = actionDone2;
    }

    /**
     * @return the otherfinding
     */
    public String getOtherfinding() {
        return otherfinding;
    }

    /**
     * @param otherfinding the otherfinding to set
     */
    public void setOtherfinding(String otherfinding) {
        this.otherfinding = otherfinding;
    }

    /**
     * @return the otherintervention
     */
    public String getOtherintervention() {
        return otherintervention;
    }

    /**
     * @param otherintervention the otherintervention to set
     */
    public void setOtherintervention(String otherintervention) {
        this.otherintervention = otherintervention;
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
     * @return the findings
     */
    public String getFindings() {
        return findings;
    }

    /**
     * @param findings the findings to set
     */
    public void setFindings(String findings) {
        this.findings = findings;
    }

    /**
     * @return the actionDone
     */
    public String getActionDone() {
        return actionDone;
    }

    /**
     * @param actionDone the actionDone to set
     */
    public void setActionDone(String actionDone) {
        this.actionDone = actionDone;
    }

    /**
     * @return the recommedation
     */
    public String getRecommedation() {
        return recommedation;
    }

    /**
     * @param recommedation the recommedation to set
     */
    public void setRecommedation(String recommedation) {
        this.recommedation = recommedation;
    }
    
    
    public static class currentActionPointsBuilder{
    
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
    private String findings;
    private String actionDone;
    private String recommedation;
    private String programarea;
    private String commoditya;
    private int actionDone2;
    private String otherfinding;
    private String otherintervention;
    
    
    
    
    public CurrentActionPointsModel.currentActionPointsBuilder instanceId(String instanceId) {
      this.instanceId = instanceId;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder county(String county) {
      this.county = county;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder actionpoint(String actionpoint) {
      this.actionpoint = actionpoint;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder presponsible(String presponsible) {
      this.presponsible = presponsible;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder duedate(LocalDate duedate) {
      this.duedate = duedate;
         return this;
    }    

    public CurrentActionPointsModel.currentActionPointsBuilder findings(String findings) {
      this.findings = findings;
         return this;
    } 
    public CurrentActionPointsModel.currentActionPointsBuilder actionDone(String actionDone) {
      this.actionDone = actionDone;
         return this;
    }   
    public CurrentActionPointsModel.currentActionPointsBuilder recommedation(String recommedation) {
      this.recommedation = recommedation;
         return this;
    } 
    public CurrentActionPointsModel.currentActionPointsBuilder programarea(String programarea) {
      this.programarea = programarea;
         return this;
    } 
    public CurrentActionPointsModel.currentActionPointsBuilder commoditya(String commoditya) {
      this.commoditya = commoditya;
         return this;
    } 
    public CurrentActionPointsModel.currentActionPointsBuilder otherfinding(String otherfinding) {
      this.otherfinding = otherfinding;
         return this;
    } 
    public CurrentActionPointsModel.currentActionPointsBuilder otherintervention(String otherintervention) {
      this.otherintervention = otherintervention;
         return this;
    }
    public CurrentActionPointsModel.currentActionPointsBuilder actionDone2(int actionDone2) {
      this.actionDone2 = actionDone2;
         return this;
    } 
    
    public CurrentActionPointsModel build() {
            CurrentActionPointsModel previousPointsReview = new CurrentActionPointsModel(this);
            return previousPointsReview;
        }
    }   
      
}
