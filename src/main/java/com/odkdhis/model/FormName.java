/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model;

/**
 *
 * @author ssvmoh
 */
public enum FormName {

    RESOLUTION("Resolution of Previous Action Points", 10),
    STORAGE("Storage of Health Products", 20),
    INVENTORY("Inventory Management", 20),
    AVAILABILITY("Availability & Use of Commodity MIS Tools", 10),
    VERIFICATION("Verification of Commodity Data", 10),
    GUIDELINES("Guidelines & Job Aids", 5),
    LLINS("LLINs Managment & Accountability", 10),
    ACCOUNTABILITY("Accountability for Commodities", 15);
    //INVENTORY_rv("Inventory_Management_combined", 20),
    //ACCOUNTABILITY_rv("supply_chain_audit_slim", 15),as
    
    

    private String name;

    private Integer maxScore;

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the maxScore
     */
    public Integer getMaxScore() {
        return maxScore;
    }

    /**
     * @param maxScore the maxScore to set
     */
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    private FormName(String name, Integer maxScore) {
        this.name = name;
        this.maxScore = maxScore;
    }


}
