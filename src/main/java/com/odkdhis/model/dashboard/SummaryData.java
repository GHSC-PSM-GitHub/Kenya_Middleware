/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import com.odkdhis.model.FormName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "summary")
public class SummaryData implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(name = "org_unit_code")
    private String orgUnit;
    
    @Column(name = "form_name")
    @Enumerated(EnumType.STRING)
    private FormName formName;
    
    @Column(name = "date_collected")
    @Temporal(TemporalType.DATE)
    private Date dateCollected;
    
    @Column(name = "numerator")
    private BigDecimal numerator;
    
    @Column(name = "denominator")
    private BigDecimal denominator;
    
    @Column(name = "score")
    private BigDecimal score;
    
    @Transient
    private String orgUnitCode;
    
    @Transient
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;
    
    @Transient
    private String fromDate;
    
    @Transient
    private String toDate;

    public SummaryData() {
    }

    public SummaryData(String orgUnit, FormName formName, Date dateCollected, BigDecimal numerator, BigDecimal denominator, BigDecimal score) {
        this.orgUnit = orgUnit;
        this.formName = formName;
        this.dateCollected = dateCollected;
        this.numerator = numerator;
        this.denominator = denominator;
        this.score = score;
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
     * @return the periodType
     */
    public PeriodType getPeriodType() {
        return periodType;
    }

    /**
     * @param periodType the periodType to set
     */
    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
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
    
}
