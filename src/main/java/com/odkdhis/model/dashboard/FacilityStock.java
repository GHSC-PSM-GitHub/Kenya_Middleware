/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

/**
 *
 * @author Paul Omboi
 */
public class FacilityStock {
    private String county;
    private String facility;
    private String product_name;
    private String stockStatus;
    private String fromDate;
    private String toDate;
   // private String date_collected;

    public FacilityStock(){
        
    }

    public FacilityStock(String county, String facility, String product_name, String stockStatus) {
        this.county = county;
        this.facility = facility;
        this.product_name = product_name;
        this.stockStatus = stockStatus;
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

    /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param product_name the product_name to set
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return the stockStatus
     */
    public String getStockStatus() {
        return stockStatus;
    }

    /**
     * @param stockStatus the stockStatus to set
     */
    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
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
