/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "data_value")
public class DatasetValue implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "data_element")
    private String dataElement;

    @Column(name = "data_value")
    private String value;

    @ManyToOne
    private DataValueGroup dataGroup;

    public DatasetValue() {
    }

    public DatasetValue(String dataElement, String value) {
        this.dataElement = dataElement;
        this.value = value;
    }

    public String toGridJson() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"dataElement\":").append("\"").append(getDataElement()).append("\",")
                .append("\"value\":\"").append(getValue()).append("\"},")
                .append("\n");
        return sb.toString();
    }

    public String toGridJson2() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"dataElement\":").append("\"").append(getDataElement()).append("\",")
                .append("\"period\":\"").append(getDataGroup().getPeriod()).append("\",")
                .append("\"orgUnit\":\"").append(getDataGroup().getOrgUnit()).append("\",")
                .append("\"value\":\"").append(getValue()).append("\"},")
                .append("\n");
        return sb.toString();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the dataElement
     */
    public String getDataElement() {
        return dataElement;
    }

    /**
     * @param dataElement the dataElement to set
     */
    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the dataGroup
     */
    public DataValueGroup getDataGroup() {
        return dataGroup;
    }

    /**
     * @param dataGroup the dataGroup to set
     */
    public void setDataGroup(DataValueGroup dataGroup) {
        this.dataGroup = dataGroup;
    }

}
