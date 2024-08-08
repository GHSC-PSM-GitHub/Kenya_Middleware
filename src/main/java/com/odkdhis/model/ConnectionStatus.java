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
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "con_status")
public class ConnectionStatus implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "odk_con")
    private String odkConnection;

    @Column(name = "dhis_con")
    private String dhisConnection;

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
     * @return the odkConnection
     */
    public String getOdkConnection() {
        return odkConnection;
    }

    /**
     * @param odkConnection the odkConnection to set
     */
    public void setOdkConnection(String odkConnection) {
        this.odkConnection = odkConnection;
    }

    /**
     * @return the dhisConnection
     */
    public String getDhisConnection() {
        return dhisConnection;
    }

    /**
     * @param dhisConnection the dhisConnection to set
     */
    public void setDhisConnection(String dhisConnection) {
        this.dhisConnection = dhisConnection;
    }

}
