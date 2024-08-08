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
public enum OdkForm {

    Facility_Profile_Information("Resolution of Previous Action Points", "RESOLUTION", 10),
    storage_area_assesment2("Storage of Health Products","STORAGE", 20),
    //storage_area_assesment_org("Storage of Health Products", "STORAGE", 20),
    //Inventory_Management_Addenda("Inventory Management", "INVENTORY", 20),
    //Inventory_Management("Inventory Management", "INVENTORY", 30),
    Inventory_Management_combined("Inventory Management", "INVENTORY", 30),
    Commodity_Data_and_Reporting_Tools("Availability & Use of Commodity MIS Tools", "AVAILABILITY", 10),
    Verification_of_commodity_data("Verification of Commodity Data", "VERIFICATION", 10),
    Job_Aids_for_commodity_management("Guidelines & Job Aids", "GUIDELINES", 5),
    LLINs("LLINs Managment & Accountability", "LLINS",10),
    supply_chain_audit_slim("Accountability for Commodities", "ACCOUNTABILITY", 15);
    //supply_chain_audit_Addenda("Accountability for Commodities", "ACCOUNTABILITY", 15),
    //supply_chain_audit_v2("Accountability for Commodities", "ACCOUNTABILITY", 15),
    //supply_chain_audit_v3("Accountability for Commodities", "ACCOUNTABILITY", 15),
    
    

    private String name;
    
    private String dataSetId;

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

    private OdkForm(String name, String dataSetId, Integer maxScore) {
        this.name = name;
        this.dataSetId = dataSetId;
        this.maxScore = maxScore;
    }

    /**
     * @return the dataSetId
     */
    public String getDataSetId() {
        return dataSetId;
    }

    /**
     * @param dataSetId the dataSetId to set
     */
    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }

}
