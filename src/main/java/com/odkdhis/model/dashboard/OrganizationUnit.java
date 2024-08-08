/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "org_unit")
public class OrganizationUnit implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "org_name")
    private String name;

    @Column(name = "org_code")
    private String code;

    @Column(name = "org_uid")
    private String uid;

    @Column(name = "seq")
    private Integer seq;

    @Column(name = "count_parent")
    private String countParent;

    @Column(name = "sub_count_parent")
    private String subCountParent;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private OrganizationUnit parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<OrganizationUnit> children = new ArrayList<OrganizationUnit>();
    
    @Transient
    private MultipartFile uploadpath;

    public OrganizationUnit() {
    }

    public OrganizationUnit(String name, String code, String uid, Integer seq, String countParent, String subCountParent, OrganizationUnit parent) {
        this.name = name;
        this.code = code;
        this.uid = uid;
        this.seq = seq;
        this.countParent = countParent;
        this.subCountParent = subCountParent;
        this.parent = parent;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the seq
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * @return the countParent
     */
    public String getCountParent() {
        return countParent;
    }

    /**
     * @param countParent the countParent to set
     */
    public void setCountParent(String countParent) {
        this.countParent = countParent;
    }

    /**
     * @return the subCountParent
     */
    public String getSubCountParent() {
        return subCountParent;
    }

    /**
     * @param subCountParent the subCountParent to set
     */
    public void setSubCountParent(String subCountParent) {
        this.subCountParent = subCountParent;
    }

    /**
     * @return the parent
     */
    public OrganizationUnit getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(OrganizationUnit parent) {
        this.parent = parent;
    }

    /**
     * @return the children
     */
    public List<OrganizationUnit> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<OrganizationUnit> children) {
        this.children = children;
    }

    public void addChild(OrganizationUnit ap) {
        ap.setParent(this);
        getChildren().add(ap);
    }

    public void addChildren(List<OrganizationUnit> aps) {
        for (OrganizationUnit ap : aps) {
            ap.setParent(this);
            getChildren().add(ap);
        }
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the uploadpath
     */
    public MultipartFile getUploadpath() {
        return uploadpath;
    }

    /**
     * @param uploadpath the uploadpath to set
     */
    public void setUploadpath(MultipartFile uploadpath) {
        this.uploadpath = uploadpath;
    }

}
