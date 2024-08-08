/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Transient;

/**
 *
 * @author Paul Omboi
 */
public class FacilitiesMeetingSohExpectation {
    
    private String facility;
    private int expected_yes;
    private int expected_no;
    private int total_expected;
    private BigDecimal percentage_yes;
    private BigDecimal percentage_no;
    private String fromDate;
    private String toDate;
    private String county;
    

    public FacilitiesMeetingSohExpectation() {
    }

    public FacilitiesMeetingSohExpectation(String facility, int expected_yes, int expected_no, int total_expected) {
        this.facility=facility;       
        this.expected_yes = expected_yes;
        this.expected_no = expected_no;
        this.total_expected = total_expected;
    }
    

    /**
     * @return the expected_yes
     */
    public int getExpected_yes() {
        return expected_yes;
    }

    /**
     * @param expected_yes the expected_yes to set
     */
    public void setExpected_yes(int expected_yes) {
        this.expected_yes = expected_yes;
    }

    /**
     * @return the expected_no
     */
    public int getExpected_no() {
        return expected_no;
    }

    /**
     * @param expected_no the expected_no to set
     */
    public void setExpected_no(int expected_no) {
        this.expected_no = expected_no;
    }

    /**
     * @return the total_expected
     */
    public int getTotal_expected() {
        return total_expected;
    }

    /**
     * @param total_expected the total_expected to set
     */
    public void setTotal_expected(int total_expected) {
        this.total_expected = total_expected;
    }

    /**
     * @return the percentage_yes
     */
    public BigDecimal getPercentage_yes() {
        BigDecimal factor = (new BigDecimal(expected_yes)).divide(new BigDecimal(total_expected),10,RoundingMode.HALF_EVEN);
        return (factor.multiply(new BigDecimal(100))).setScale(0, RoundingMode.HALF_EVEN);
    }

    /**
     * @param percentage_yes the percentage_yes to set
     */
    public void setPercentage_yes(BigDecimal percentage_yes) {
        this.percentage_yes = percentage_yes;
    }

    /**
     * @return the percentage_no
     */
    public BigDecimal getPercentage_no() {
        BigDecimal factor = (new BigDecimal(expected_no)).divide(new BigDecimal(total_expected),10,RoundingMode.HALF_EVEN);
        return (factor.multiply(new BigDecimal(100))).setScale(0, RoundingMode.HALF_EVEN);
    }

    /**
     * @param percentage_no the percentage_no to set
     */
    public void setPercentage_no(BigDecimal percentage_no) {
        this.percentage_no = percentage_no;
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
   
    
}
