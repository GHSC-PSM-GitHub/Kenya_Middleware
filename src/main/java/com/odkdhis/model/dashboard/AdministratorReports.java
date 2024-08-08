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
@Table(name = "adminreports")

public class AdministratorReports implements Serializable {

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
    private String facilitytype;
    private String department;
    private String otherdepartment;
    private String commoditytype;
    private String commodity;
    private int qtyinstock;
    private int qtyexpired;
    private int qtyexpringin6months; 
    private LocalDate earliestexpirydate ;
    private String commodityremarks;
    private String pname;
    private String phonenumber;    
    private String agecategory;
    private String personcounty;
    private String psubcounty;
    private String emailaddress;
    private String generalremarks;
    
    @Transient
    private String fromDate;
    @Transient
    private String toDate;
    
    
    
    public AdministratorReports(){
        
    }
     public AdministratorReports(AdministratorReportsBuilder builder){
           
           this.instanceId = builder.instanceId;
           this.dateCollected = builder.dateCollected;
           this.orgUnit = builder.orgUnit;
           this.county = builder.county;
           this.subcounty = builder.subcounty;
           this.facilityname = builder.facilityname;
           this.facilitytype = builder.facilitytype;
           this.department = builder.department;
           this.otherdepartment = builder.otherdepartment;
           this.commoditytype = builder.commoditytype;
           this.commoditytype = builder.commoditytype;
           this.commodity = builder.commodity;
           this.qtyinstock = builder.qtyinstock;
           this.qtyexpired = builder.qtyexpired;
           this.qtyexpringin6months = builder.qtyexpringin6months;
           this.earliestexpirydate = builder.earliestexpirydate;
           this.commodityremarks = builder.commodityremarks;
           this.pname = builder.pname;
           this.phonenumber = builder.phonenumber;
           this.agecategory = builder.agecategory;
           this.personcounty = builder.personcounty;
           this.psubcounty = builder.psubcounty;
           this.emailaddress = builder.emailaddress;
           this.generalremarks = builder.generalremarks;
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
     * @return the facilitytype
     */
    public String getFacilitytype() {
        return facilitytype;
    }

    /**
     * @param facilitytype the facilitytype to set
     */
    public void setFacilitytype(String facilitytype) {
        this.facilitytype = facilitytype;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the otherdepartment
     */
    public String getOtherdepartment() {
        return otherdepartment;
    }

    /**
     * @param otherdepartment the otherdepartment to set
     */
    public void setOtherdepartment(String otherdepartment) {
        this.otherdepartment = otherdepartment;
    }

    /**
     * @return the commoditytype
     */
    public String getCommoditytype() {
        return commoditytype;
    }

    /**
     * @param commoditytype the commoditytype to set
     */
    public void setCommoditytype(String commoditytype) {
        this.commoditytype = commoditytype;
    }

    /**
     * @return the commodity
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * @param commodity the commodity to set
     */
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    /**
     * @return the qtyinstock
     */
    public Integer getQtyinstock() {
        return qtyinstock;
    }

    /**
     * @param qtyinstock the qtyinstock to set
     */
    public void setQtyinstock(Integer qtyinstock) {
        this.qtyinstock = qtyinstock;
    }

    /**
     * @return the qtyexpired
     */
    public Integer getQtyexpired() {
        return qtyexpired;
    }

    /**
     * @param qtyexpired the qtyexpired to set
     */
    public void setQtyexpired(Integer qtyexpired) {
        this.qtyexpired = qtyexpired;
    }

    /**
     * @return the qtyexpringin6months
     */
    public Integer getQtyexpringin6months() {
        return qtyexpringin6months;
    }

    /**
     * @param qtyexpringin6months the qtyexpringin6months to set
     */
    public void setQtyexpringin6months(Integer qtyexpringin6months) {
        this.qtyexpringin6months = qtyexpringin6months;
    }

    /**
     * @return the earliestexpirydate
     */
    public LocalDate getEarliestexpirydate() {
        return earliestexpirydate;
    }

    /**
     * @param earliestexpirydate the earliestexpirydate to set
     */
    public void setEarliestexpirydate(LocalDate earliestexpirydate) {
        this.earliestexpirydate = earliestexpirydate;
    }

    /**
     * @return the commodityremarks
     */
    public String getCommodityremarks() {
        return commodityremarks;
    }

    /**
     * @param commodityremarks the commodityremarks to set
     */
    public void setCommodityremarks(String commodityremarks) {
        this.commodityremarks = commodityremarks;
    }

    /**
     * @return the pname
     */
    public String getPname() {
        return pname;
    }

    /**
     * @param pname the pname to set
     */
    public void setPname(String pname) {
        this.pname = pname;
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
     * @return the agecategory
     */
    public String getAgecategory() {
        return agecategory;
    }

    /**
     * @param agecategory the agecategory to set
     */
    public void setAgecategory(String agecategory) {
        this.agecategory = agecategory;
    }

    /**
     * @return the personcounty
     */
    public String getPersoncounty() {
        return personcounty;
    }

    /**
     * @param personcounty the personcounty to set
     */
    public void setPersoncounty(String personcounty) {
        this.personcounty = personcounty;
    }

    /**
     * @return the psubcounty
     */
    public String getPsubcounty() {
        return psubcounty;
    }

    /**
     * @param psubcounty the psubcounty to set
     */
    public void setPsubcounty(String psubcounty) {
        this.psubcounty = psubcounty;
    }

    /**
     * @return the emailaddress
     */
    public String getEmailaddress() {
        return emailaddress;
    }

    /**
     * @param emailaddress the emailaddress to set
     */
    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
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
    
    public static class AdministratorReportsBuilder{
    
    private String instanceId;
    private LocalDate dateCollected;
    private String orgUnit;
    private String county;
    private String subcounty;       
    private String facilityname;
    private String facilitytype;
    private String department;
    private String otherdepartment;
    private String commoditytype;
    private String commodity;
    private int qtyinstock;
    private int qtyexpired;
    private int qtyexpringin6months; 
    private LocalDate earliestexpirydate ;
    private String commodityremarks;
    private String pname;
    private String phonenumber;    
    private String agecategory;
    private String personcounty;
    private String psubcounty;
    private String emailaddress;
    private String generalremarks;
    
    
    public AdministratorReportsBuilder instanceId(String instanceId) {
       this.instanceId = instanceId;
         return this;
        }
    public AdministratorReportsBuilder dateCollected(LocalDate dateCollected) {
       this.dateCollected = dateCollected;
         return this;
        }    
    public AdministratorReportsBuilder orgUnit(String orgUnit) {
       this.orgUnit = orgUnit;
         return this;
        }
    public AdministratorReportsBuilder county(String county) {
       this.county = county;
         return this;
        }    
    public AdministratorReportsBuilder subcounty(String subcounty) {
       this.subcounty = subcounty;
         return this;
        }    
    public AdministratorReportsBuilder facilityname(String facilityname) {
       this.facilityname = facilityname;
         return this;
        }    
    public AdministratorReportsBuilder facilitytype(String facilitytype) {
       this.facilitytype = facilitytype;
         return this;
        }    
    public AdministratorReportsBuilder department(String department) {
       this.department = department;
         return this;
        }
    public AdministratorReportsBuilder otherdepartment(String otherdepartment) {
       this.otherdepartment = otherdepartment;
         return this;
        }
    public AdministratorReportsBuilder commoditytype(String commoditytype) {
       this.commoditytype = commoditytype;
         return this;
        }

    public AdministratorReportsBuilder commodity(String commodity) {
       this.commodity = commodity;
         return this;
        }
    public AdministratorReportsBuilder qtyinstock(int qtyinstock) {
       this.qtyinstock = qtyinstock;
         return this;
        }
     public AdministratorReportsBuilder qtyexpired(int qtyexpired) {
       this.qtyexpired = qtyexpired;
         return this;
        }   
    public AdministratorReportsBuilder qtyexpringin6months (int qtyexpringin6months ) {
       this.qtyexpringin6months  = qtyexpringin6months ;
         return this;
        }  
    
    public AdministratorReportsBuilder earliestexpirydate (LocalDate earliestexpirydate ) {
       this.earliestexpirydate  = earliestexpirydate;
         return this;
        }
    public AdministratorReportsBuilder commodityremarks(String commodityremarks) {
       this.commodityremarks = commodityremarks;
         return this;
        }
    public AdministratorReportsBuilder pname(String pname) {
       this.pname = pname;
         return this;
        }
    public AdministratorReportsBuilder phonenumber(String phonenumber) {
       this.phonenumber = phonenumber;
         return this;
        }
     public AdministratorReportsBuilder agecategory(String agecategory) {
       this.agecategory = agecategory;
         return this;
        }   
     public AdministratorReportsBuilder personcounty(String personcounty) {
       this.personcounty = personcounty;
         return this;
        } 
     public AdministratorReportsBuilder psubcounty(String psubcounty) {
       this.psubcounty = psubcounty;
         return this;
        }      
     public AdministratorReportsBuilder emailaddress(String emailaddress) {
       this.emailaddress = emailaddress;
         return this;
        }      
    public AdministratorReportsBuilder generalremarks(String generalremarks) {
       this.generalremarks = generalremarks;
         return this;
        }     
    public AdministratorReports build() {
            AdministratorReports adminRpt = new AdministratorReports(this);
            return adminRpt;
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
         * @return the facilitytype
         */
        public String getFacilitytype() {
            return facilitytype;
        }

        /**
         * @param facilitytype the facilitytype to set
         */
        public void setFacilitytype(String facilitytype) {
            this.facilitytype = facilitytype;
        }

        /**
         * @return the department
         */
        public String getDepartment() {
            return department;
        }

        /**
         * @param department the department to set
         */
        public void setDepartment(String department) {
            this.department = department;
        }

        /**
         * @return the otherdepartment
         */
        public String getOtherdepartment() {
            return otherdepartment;
        }

        /**
         * @param otherdepartment the otherdepartment to set
         */
        public void setOtherdepartment(String otherdepartment) {
            this.otherdepartment = otherdepartment;
        }

        /**
         * @return the commoditytype
         */
        public String getCommoditytype() {
            return commoditytype;
        }

        /**
         * @param commoditytype the commoditytype to set
         */
        public void setCommoditytype(String commoditytype) {
            this.commoditytype = commoditytype;
        }

        /**
         * @return the commodity
         */
        public String getCommodity() {
            return commodity;
        }

        /**
         * @param commodity the commodity to set
         */
        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        /**
         * @return the qtyinstock
         */
        public Integer getQtyinstock() {
            return qtyinstock;
        }

        /**
         * @param qtyinstock the qtyinstock to set
         */
        public void setQtyinstock(Integer qtyinstock) {
            this.qtyinstock = qtyinstock;
        }

        /**
         * @return the qtyexpired
         */
        public Integer getQtyexpired() {
            return qtyexpired;
        }

        /**
         * @param qtyexpired the qtyexpired to set
         */
        public void setQtyexpired(Integer qtyexpired) {
            this.qtyexpired = qtyexpired;
        }

        /**
         * @return the qtyexpringin6months
         */
        public Integer getQtyexpringin6months() {
            return qtyexpringin6months;
        }

        /**
         * @param qtyexpringin6months the qtyexpringin6months to set
         */
        public void setQtyexpringin6months(Integer qtyexpringin6months) {
            this.qtyexpringin6months = qtyexpringin6months;
        }

        /**
         * @return the earliestexpirydate
         */
        public LocalDate getEarliestexpirydate() {
            return earliestexpirydate;
        }

        /**
         * @param earliestexpirydate the earliestexpirydate to set
         */
        public void setEarliestexpirydate(LocalDate earliestexpirydate) {
            this.earliestexpirydate = earliestexpirydate;
        }

        /**
         * @return the commodityremarks
         */
        public String getCommodityremarks() {
            return commodityremarks;
        }

        /**
         * @param commodityremarks the commodityremarks to set
         */
        public void setCommodityremarks(String commodityremarks) {
            this.commodityremarks = commodityremarks;
        }

        /**
         * @return the pname
         */
        public String getPname() {
            return pname;
        }

        /**
         * @param pname the pname to set
         */
        public void setPname(String pname) {
            this.pname = pname;
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
         * @return the agecategory
         */
        public String getAgecategory() {
            return agecategory;
        }

        /**
         * @param agecategory the agecategory to set
         */
        public void setAgecategory(String agecategory) {
            this.agecategory = agecategory;
        }

        /**
         * @return the personcounty
         */
        public String getPersoncounty() {
            return personcounty;
        }

        /**
         * @param personcounty the personcounty to set
         */
        public void setPersoncounty(String personcounty) {
            this.personcounty = personcounty;
        }

        /**
         * @return the psubcounty
         */
        public String getPsubcounty() {
            return psubcounty;
        }

        /**
         * @param psubcounty the psubcounty to set
         */
        public void setPsubcounty(String psubcounty) {
            this.psubcounty = psubcounty;
        }

        /**
         * @return the emailaddress
         */
        public String getEmailaddress() {
            return emailaddress;
        }

        /**
         * @param emailaddress the emailaddress to set
         */
        public void setEmailaddress(String emailaddress) {
            this.emailaddress = emailaddress;
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
        
          
    }
      
}
