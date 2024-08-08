/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.math.BigDecimal;

/**
 *
 * @author Paul Omboi
 */
public class FacilityAnalysis {
    private String county;
    private int facility;
    private String product_name;
    private String status;
    private String fromDate;
    private String toDate;
    private BigDecimal percentage;
    
   
    public FacilityAnalysis(){
    }

    public FacilityAnalysis(String county, int facility, String product_name, String status) {
        this.county = county;
        this.facility = facility;
        this.product_name = product_name;
        this.status = status;
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
    public int getFacility() {
        return facility;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(int facility) {
        this.facility = facility;
    }

    /**
     * @return the prodcut_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param prodcut_name the prodcut_name to set
     */
    public void setProdcut_name(String product_name) {
        this.product_name = product_name;
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
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
