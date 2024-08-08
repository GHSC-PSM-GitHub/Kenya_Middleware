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
public enum DataElement {
    
    Facility_Profile_Information("Resolution of Previous Action Points", "RESOLUTION"),
    storage_area_assesment2("Storage of Health Products","STORAGE"),
    storage_area_assesment_org("Storage of Health Products", "STORAGE"),
    Inventory_Management_Addenda("Inventory Management", "INVENTORY"),
    Inventory_Management("Inventory Management", "INVENTORY"),
    Commodity_Data_and_Reporting_Tools("Availability & Use of Commodity MIS Tools", "AVAILABILITY"),
    Verification_of_commodity_data("Verification of Commodity Data", "VERIFICATION"),
    Job_Aids_for_commodity_management("Guidelines & Job Aids", "GUIDELINES"),
    supply_chain_audit_Addenda("Accountability for Commodities", "ACCOUNTABILITY"),
    supply_chain_audit_v2("Accountability for Commodities", "ACCOUNTABILITY"),
    supply_chain_audit_v3("Accountability for Commodities", "ACCOUNTABILITY"),
    supply_chain_audit_slim("Accountability for Commodities", "ACCOUNTABILITY");

    private String name;
    
    private String dataElementSuffix;

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    private DataElement(String name, String dataElementSuffix) {
        this.name = name;
        this.dataElementSuffix = dataElementSuffix;
    }


    /**
     * @return the dataElementSuffix
     */
    public String getDataElementSuffix() {
        return dataElementSuffix;
    }

    /**
     * @param dataElementSuffix the dataElementSuffix to set
     */
    public void setDataElementSuffix(String dataElementSuffix) {
        this.dataElementSuffix = dataElementSuffix;
    }

    
}
