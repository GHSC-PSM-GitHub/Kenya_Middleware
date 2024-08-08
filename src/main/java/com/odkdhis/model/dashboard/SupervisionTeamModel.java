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
@Table(name = "supervisionteam")

public class SupervisionTeamModel implements Serializable {

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
    private String person;
    private String designation;
    private String phoneNo;
    private String afiliation;
    private String gpscordinates;
    @Column(name = "inchargeName")
    private String inchargeName;
    @Column(name = "inchargeEmail")
    private String inchargeEmail;
    @Column(name = "inchargePhone")
    private String inchargePhone;
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    public SupervisionTeamModel(){
        
    }
     public SupervisionTeamModel(supervisionTeamBuilder builder){
      
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.flevel = builder.flevel;
           this.person = builder.person;
           this.designation = builder.designation;
           this.phoneNo = builder.phoneNo;
           this.afiliation = builder.afiliation;
           this.gpscordinates=builder.gpscordinates;
           this.inchargeName=builder.inchargeName;
           this.inchargeName=builder.inchargeEmail;
           this.inchargeName=builder.inchargePhone;
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
     * @return the person
     */
    public String getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return the afiliation
     */
    public String getAfiliation() {
        return afiliation;
    }

    /**
     * @param afiliation the afiliation to set
     */
    public void setAfiliation(String afiliation) {
        this.afiliation = afiliation;
    }
    
    
    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    /**
     * @return the inchargeName
     */
    public String getInchargeName() {
        return inchargeName;
    }

    /**
     * @param inchargeName the inchargeName to set
     */
    public void setInchargeName(String inchargeName) {
        this.inchargeName = inchargeName;
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
     * @return the inchargePhone
     */
    public String getInchargePhone() {
        return inchargePhone;
    }

    /**
     * @param inchargePhone the inchargePhone to set
     */
    public void setInchargePhone(String inchargePhone) {
        this.inchargePhone = inchargePhone;
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
    
    
    public static class supervisionTeamBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String flevel;
    private String person;
    private String designation;
    private String phoneNo;
    private String afiliation;
    private String gpscordinates;
    private String inchargeName;
    private String inchargeEmail;
    private String inchargePhone;
    
    public SupervisionTeamModel.supervisionTeamBuilder instanceId(String instanceId) {
      this.instanceId = instanceId;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder dateCollected(LocalDate dateCollected) {
      this.dateCollected = dateCollected;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder orgUnit(String orgUnit) {
      this.orgUnit = orgUnit;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder county(String county) {
      this.county = county;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder subcounty(String subcounty) {
      this.subcounty = subcounty;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder facilityname(String facilityname) {
      this.facilityname = facilityname;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder flevel(String flevel) {
      this.flevel = flevel;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder person(String person) {
      this.person = person;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder designation(String designation) {
      this.designation = designation;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder phoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder afiliation(String afiliation) {
      this.afiliation = afiliation;
         return this;
    }
  
    public SupervisionTeamModel.supervisionTeamBuilder gpscordinates(String gpscordinates) {
      this.gpscordinates = gpscordinates;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder inchargeName(String inchargeName) {
      this.inchargeName = inchargeName;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder inchargeEmail(String inchargeEmail) {
      this.inchargeEmail = inchargeEmail;
         return this;
    }
    public SupervisionTeamModel.supervisionTeamBuilder inchargePhone(String inchargePhone) {
      this.inchargePhone = inchargePhone;
         return this;
    }
     
    public SupervisionTeamModel build() {
            SupervisionTeamModel supervisionteam = new SupervisionTeamModel(this);
            return supervisionteam;
        }
    }   
      
}
