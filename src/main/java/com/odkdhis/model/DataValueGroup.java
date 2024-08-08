/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "dv_group")
public class DataValueGroup implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "data_set")
    private String dataSet;

    @Column(name = "data_period")
    private String period;

    @Column(name = "org_unit")
    private String orgUnit;

    @Column(name = "data_send")
    private Boolean dataSend;

    @Column(name = "date_collected")
    @Temporal(TemporalType.DATE)
    private Date dateCollected;

    @OneToMany(mappedBy = "dataGroup", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<DatasetValue> datavalues = new ArrayList<DatasetValue>();
    
    //For Dashboard data
    @Column(name = "org_unit_code")
    private String orgUnitCode;
    
    @Column(name = "form_name")
    @Enumerated(EnumType.STRING)
    private FormName formName;
    
     @Column(name = "numerator")
    private BigDecimal numerator;
    
    @Column(name = "denominator")
    private BigDecimal denominator;
    
    @Column(name = "score")
    private BigDecimal score;
    
    @Column(name = "data_send_to_summary")
    private Boolean dataSendTosummary;
    
    @Column(name = "retries")
    private Integer retries;
    

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
     * @return the dataSet
     */
    public String getDataSet() {
        return dataSet;
    }

    /**
     * @param dataSet the dataSet to set
     */
    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
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
     * @return the dataSend
     */
    public Boolean getDataSend() {
        return dataSend;
    }

    /**
     * @param dataSend the dataSend to set
     */
    public void setDataSend(Boolean dataSend) {
        this.dataSend = dataSend;
    }

    /**
     * @return the dateCollected
     */
    public Date getDateCollected() {
        return dateCollected;
    }

    /**
     * @param dateCollected the dateCollected to set
     */
    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    /**
     * @return the datavalues
     */
    public List<DatasetValue> getDatavalues() {
        return datavalues;
    }

    /**
     * @param datavalues the datavalues to set
     */
    public void setDatavalues(List<DatasetValue> datavalues) {
        this.datavalues = datavalues;
    }

    public void addDatavalues(DatasetValue dv) {
        dv.setDataGroup(this);
        getDatavalues().add(dv);
    }

    public void addDatavalues(List<DatasetValue> dvs) {
        dvs.stream().map((d) -> {
            d.setDataGroup(this);
            return d;
        }).forEachOrdered((dv) -> {
            getDatavalues().add(dv);
        });
    }

    /**
     * @return the orgUnitCode
     */
    public String getOrgUnitCode() {
        return orgUnitCode;
    }

    /**
     * @param orgUnitCode the orgUnitCode to set
     */
    public void setOrgUnitCode(String orgUnitCode) {
        this.orgUnitCode = orgUnitCode;
    }

    /**
     * @return the formName
     */
    public FormName getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(FormName formName) {
        this.formName = formName;
    }

    /**
     * @return the numerator
     */
    public BigDecimal getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(BigDecimal numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public BigDecimal getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(BigDecimal denominator) {
        this.denominator = denominator;
    }

    /**
     * @return the score
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * @return the dataSendTosummary
     */
    public Boolean getDataSendTosummary() {
        return dataSendTosummary;
    }

    /**
     * @param dataSendTosummary the dataSendTosummary to set
     */
    public void setDataSendTosummary(Boolean dataSendTosummary) {
        this.dataSendTosummary = dataSendTosummary;
    }

    /**
     * @return the retries
     */
    public Integer getRetries() {
        return retries;
    }

    /**
     * @param retries the retries to set
     */
    public void setRetries(Integer retries) {
        this.retries = retries;
    }

}
