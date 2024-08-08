package com.odkdhis.model;

public enum SingleForm {

    Facility_Profile_Information("Resolution of Previous Action Points", "RESOLUTION", 10),
    Commodity_Data_and_Reporting_Tools("Availability & Use of Commodity MIS Tools", "AVAILABILITY", 10),
    Verification_of_commodity_data("Verification of Commodity Data", "VERIFICATION", 10),
    Job_Aids_for_commodity_management("Guidelines & Job Aids", "GUIDELINES", 5);

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

    private SingleForm(String name, String dataSetId, Integer maxScore) {
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
