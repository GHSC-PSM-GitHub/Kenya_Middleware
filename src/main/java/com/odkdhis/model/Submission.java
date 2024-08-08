package com.odkdhis.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "submission")
public class Submission implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "form_id")
    private String formId;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "date_submitted")
    @Temporal(TemporalType.DATE)
    private Date dateSubmitted;

    @Column(name = "date_collected")
    @Temporal(TemporalType.DATE)
    private Date dateCollected;

    @Column(name = "data_period")
    private String period;

    @Column(name = "org_unit")
    private String orgUnit;
    
    @Column(name = "org_unit_code")
    private String orgUnitCode;

    @Column(name = "numerator")
    private String numerator;

    @Column(name = "numerator_score")
    private String numeratorScore;

    @Column(name = "denominator")
    private String denominator;

    @Column(name = "score")
    private String score;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "max_score")
    private String maxScore;

    @Column(name = "processed")
    private Boolean processed;
    
    @Column(name = "den_code")
    private String denCode;

    @Column(name = "nume_code")
    private String numeCode;

    @Column(name = "max_code")
    private String maxCode;

    @Column(name = "score_code")
    private String scoreCode;

    @Column(name = "form_name_code")
    private String formNameCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getNumerator() {
        return numerator;
    }

    public void setNumerator(String numerator) {
        this.numerator = numerator;
    }

    public String getDenominator() {
        return denominator;
    }

    public void setDenominator(String denominator) {
        this.denominator = denominator;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * @return the denCode
     */
    public String getDenCode() {
        return denCode;
    }

    /**
     * @param denCode the denCode to set
     */
    public void setDenCode(String denCode) {
        this.denCode = denCode;
    }

    /**
     * @return the numeCode
     */
    public String getNumeCode() {
        return numeCode;
    }

    /**
     * @param numeCode the numeCode to set
     */
    public void setNumeCode(String numeCode) {
        this.numeCode = numeCode;
    }

    /**
     * @return the maxCode
     */
    public String getMaxCode() {
        return maxCode;
    }

    /**
     * @param maxCode the maxCode to set
     */
    public void setMaxCode(String maxCode) {
        this.maxCode = maxCode;
    }

    /**
     * @return the scoreCode
     */
    public String getScoreCode() {
        return scoreCode;
    }

    /**
     * @param scoreCode the scoreCode to set
     */
    public void setScoreCode(String scoreCode) {
        this.scoreCode = scoreCode;
    }

    /**
     * @return the formNameCode
     */
    public String getFormNameCode() {
        return formNameCode;
    }

    /**
     * @param formNameCode the formNameCode to set
     */
    public void setFormNameCode(String formNameCode) {
        this.formNameCode = formNameCode;
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

    public String getNumeratorScore() {
        return numeratorScore;
    }

    public void setNumeratorScore(String numeratorScore) {
        this.numeratorScore = numeratorScore;
    }

}
