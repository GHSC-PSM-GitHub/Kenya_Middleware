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
public class ProductsWithMatchingData {
    private String county;
    private String product_name;
    private int allfacilities;
    private int matching;
    private int notmatching;
    private BigDecimal percentage;
    private String fromDate;
    private String toDate;
    
    public ProductsWithMatchingData(){
    }

    public ProductsWithMatchingData(String county, String product_name, int allfacilities, int matching, int notmatching) {
        this.county = county;
        this.product_name = product_name;
        this.allfacilities = allfacilities;
        this.matching = matching;
        this.notmatching = notmatching;
        
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
     * @return the allfacilities
     */
    public int getAllfacilities() {
        return allfacilities;
    }

    /**
     * @param allfacilities the allfacilities to set
     */
    public void setAllfacilities(int allfacilities) {
        this.allfacilities = allfacilities;
    }

    /**
     * @return the matching
     */
    public int getMatching() {
        return matching;
    }

    /**
     * @param matching the matching to set
     */
    public void setMatching(int matching) {
        this.matching = matching;
    }

    /**
     * @return the notmatching
     */
    public int getNotmatching() {
        return notmatching;
    }

    /**
     * @param notmatching the notmatching to set
     */
    public void setNotmatching(int notmatching) {
        this.notmatching = notmatching;
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