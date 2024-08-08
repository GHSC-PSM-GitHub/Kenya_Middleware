/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.io.Serializable;

/**
 *
 * @author ssvmoh
 */
public class DisplayData implements Serializable {
    
    private String formName;
    
    private String maxScore;
    
    private String numerator;
    
    private String denominator;
    
    private String score;
    
    private String performanceCategory;

    public DisplayData() {
    }

    public DisplayData(String formName, String maxScore, String numerator, String denominator, String score, String performanceCategory) {
        this.formName = formName;
        this.maxScore = maxScore;
        this.numerator = numerator;
        this.denominator = denominator;
        this.score = score;
        this.performanceCategory = performanceCategory;
    }
    
    

    /**
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * @return the maxScore
     */
    public String getMaxScore() {
        return maxScore;
    }

    /**
     * @param maxScore the maxScore to set
     */
    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * @return the numerator
     */
    public String getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(String numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public String getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(String denominator) {
        this.denominator = denominator;
    }

    /**
     * @return the score
     */
    public String getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * @return the performanceCategory
     */
    public String getPerformanceCategory() {
        return performanceCategory;
    }

    /**
     * @param performanceCategory the performanceCategory to set
     */
    public void setPerformanceCategory(String performanceCategory) {
        this.performanceCategory = performanceCategory;
    }
    
    
    
}
