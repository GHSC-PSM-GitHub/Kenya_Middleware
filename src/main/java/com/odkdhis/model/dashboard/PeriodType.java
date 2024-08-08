/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

/**
 *
 * @author ssvmoh
 */
public enum PeriodType {

    THIS_WEEK("This Week", 10),
    LAST_WEEK("Last Week", 20),
    LAST_TWO_WEEKS("Last Two Weeks", 20),
    THIS_MONTH("This Month", 30),
    LAST_MONTH("Last Month", 10),
    THIS_QUARTER("This Quarter", 10),
    LAST_QUARTER("Last Quarter", 5),
    THIS_HALF_OF_THE_YEAR("This half of the year", 20),
    LAST_HALF_OF_THE_YEAR("Last half of the year", 30),
    THIS_YEAR("This Year", 10),
    LAST_YEAR("Last Year", 10);

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

    private PeriodType(String name, Integer maxScore) {
        this.name = name;
        this.maxScore = maxScore;
    }

}
