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
//@Table(name="capacitybuilding")
@Table(name="onjobtraining")

public class OnJobTrainingModel implements Serializable {

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
    private String secondname;
    private String firstname;
    private int idnumber;
    private String cadre;
    private String genders;
    private String position;
    private String phonenumber;
    private String generalremarks;
    private String ojttopic;
    private String othertopic;
    @Column(name = "mtrained")
    private int mtrained;
    @Column(name = "ftrained")
    private int ftrained;
    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
  
    public OnJobTrainingModel(){
        
    }
     public OnJobTrainingModel(OnJobTrainingBuilder builder){
           
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.secondname = builder.secondname;
           this.firstname = builder.firstname;
           this.idnumber = builder.idnumber;
           this.cadre = builder.cadre;
           this.genders = builder.genders;
           this.position = builder.position;
           this.phonenumber = builder.phonenumber;  
           this.generalremarks = builder.generalremarks;
           this.ojttopic=builder.ojttopic;
           this.othertopic=builder.othertopic;
           this.mtrained=builder.mtrained;
           this.ftrained=builder.ftrained;
           
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
     * @return the secondname
     */
    public String getSecondname() {
        return secondname;
    }

    /**
     * @param secondname the secondname to set
     */
    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the idnumber
     */
    public int getIdnumber() {
        return idnumber;
    }

    /**
     * @param idnumber the idnumber to set
     */
    public void setIdnumber(int idnumber) {
        this.idnumber = idnumber;
    }

    /**
     * @return the cadre
     */
    public String getCadre() {
        return cadre;
    }

    /**
     * @param cadre the cadre to set
     */
    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    /**
     * @return the genders
     */
    public String getGenders() {
        return genders;
    }

    /**
     * @param genders the genders to set
     */
    public void setGenders(String genders) {
        this.genders = genders;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
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
     * @return the generalremarks
     */
    public String getGeneralremarks() {
        return generalremarks;
    }

    /**
     * @param generalremarks the generalremarks to set
     */
    public void setGeneralremarks(String generalremarks) {
        this.generalremarks = generalremarks;
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
     * @return the ojttopic
     */
    public String getOjttopic() {
        return ojttopic;
    }

    /**
     * @param ojttopic the ojttopic to set
     */
    public void setOjttopic(String ojttopic) {
        this.ojttopic = ojttopic;
    }

    /**
     * @return the othertopic
     */
    public String getOthertopic() {
        return othertopic;
    }

    /**
     * @param othertopic the othertopic to set
     */
    public void setOthertopic(String othertopic) {
        this.othertopic = othertopic;
    }
    
     
     
    public static class OnJobTrainingBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String flevel;
    private String secondname;
    private String firstname;
    private int idnumber;
    private String cadre;
    private String genders;
    private String position;
    private String phonenumber;
    private String generalremarks;
    private String ojttopic;
    private String othertopic;
    private int mtrained;
    private int ftrained;
    

    public OnJobTrainingBuilder instanceId(String instanceId) {
       this.instanceId = instanceId;
         return this;
        } 
    public OnJobTrainingBuilder othertopic(String othertopic) {
       this.othertopic = othertopic;
      return this;
        }
    
    public OnJobTrainingBuilder ftrained(int ftrained) {
       this.ftrained = ftrained;
      return this;
        }
    public OnJobTrainingBuilder mtrained(int mtrained) {
       this.mtrained = mtrained;
      return this;
        }


    public OnJobTrainingBuilder ojttopic(String ojttopic) {
       this.ojttopic = ojttopic;
         return this;
    } 
        
    public OnJobTrainingModel.OnJobTrainingBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    
    public OnJobTrainingModel.OnJobTrainingBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder county(String county) {
      this.county = county;
         return this;
    }   

    public OnJobTrainingModel.OnJobTrainingBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }    
    
     public OnJobTrainingModel.OnJobTrainingBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    }   

    public OnJobTrainingModel.OnJobTrainingBuilder secondname(String secondname) {
      this.secondname = secondname;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder firstname(String firstname) {
      this.firstname = firstname;
         return this;
    }
     public OnJobTrainingModel.OnJobTrainingBuilder idnumber(int idnumber) {
      this.idnumber = idnumber;
         return this;
    }
      public OnJobTrainingModel.OnJobTrainingBuilder cadre(String cadre) {
      this.cadre = cadre;
         return this;
    }   

    public OnJobTrainingModel.OnJobTrainingBuilder genders(String genders) {
      this.genders = genders;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder position(String position) {
      this.position = position;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder phonenumber(String phonenumber) {
      this.phonenumber = phonenumber;
         return this;
    }
    public OnJobTrainingModel.OnJobTrainingBuilder generalremarks(String generalremarks) {
      this.generalremarks = generalremarks;
         return this;
    }
    
    public OnJobTrainingModel build() {
            OnJobTrainingModel onJobTraining = new OnJobTrainingModel(this);
            return onJobTraining;
        }
    
    }

    /**
     * @return the mtrained
     */
    public int getMtrained() {
        return mtrained;
    }

    /**
     * @param mtrained the mtrained to set
     */
    public void setMtrained(int mtrained) {
        this.mtrained = mtrained;
    }

    /**
     * @return the ftrained
     */
    public int getFtrained() {
        return ftrained;
    }

    /**
     * @param ftrained the ftrained to set
     */
    public void setFtrained(int ftrained) {
        this.ftrained = ftrained;
    }
   
}
