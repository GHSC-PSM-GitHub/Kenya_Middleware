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
@Table(name = "server_config")
public class ServerConfig implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "odk_url")
    private String odkUrl;

    @Column(name = "odk_auth_user")
    private String odkAuthUser;

    @Column(name = "odk_auth_pass")
    private String odkAuthPass;

    @Column(name = "dhis_url")
    private String dhisUrl;

    @Column(name = "dhis_auth_user")
    private String dhisAuthUser;

    @Column(name = "dhis_auth_pass")
    private String dhisAuthPass;

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
     * @return the odkUrl
     */
    public String getOdkUrl() {
        return odkUrl;
    }

    /**
     * @param odkUrl the odkUrl to set
     */
    public void setOdkUrl(String odkUrl) {
        this.odkUrl = odkUrl;
    }

    /**
     * @return the odkAuthUser
     */
    public String getOdkAuthUser() {
        return odkAuthUser;
    }

    /**
     * @param odkAuthUser the odkAuthUser to set
     */
    public void setOdkAuthUser(String odkAuthUser) {
        this.odkAuthUser = odkAuthUser;
    }

    /**
     * @return the odkAuthPass
     */
    public String getOdkAuthPass() {
        return odkAuthPass;
    }

    /**
     * @param odkAuthPass the odkAuthPass to set
     */
    public void setOdkAuthPass(String odkAuthPass) {
        this.odkAuthPass = odkAuthPass;
    }

    /**
     * @return the dhisUrl
     */
    public String getDhisUrl() {
        return dhisUrl;
    }

    /**
     * @param dhisUrl the dhisUrl to set
     */
    public void setDhisUrl(String dhisUrl) {
        this.dhisUrl = dhisUrl;
    }

    /**
     * @return the dhisAuthUser
     */
    public String getDhisAuthUser() {
        return dhisAuthUser;
    }

    /**
     * @param dhisAuthUser the dhisAuthUser to set
     */
    public void setDhisAuthUser(String dhisAuthUser) {
        this.dhisAuthUser = dhisAuthUser;
    }

    /**
     * @return the dhisAuthPass
     */
    public String getDhisAuthPass() {
        return dhisAuthPass;
    }

    /**
     * @param dhisAuthPass the dhisAuthPass to set
     */
    public void setDhisAuthPass(String dhisAuthPass) {
        this.dhisAuthPass = dhisAuthPass;
    }

}
