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
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User implements Serializable{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Transient
    private String userPassword2;

    @Transient
    private String confirmuserPassword2;

    public User() {
    }

    public User(UserBuilder builder) {
        this.userFullName = builder.userFullName;
        this.username = builder.username;
        this.password = builder.password;
    }


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public void setUserPassword2(String userPassword2) {
        this.userPassword2 = userPassword2;
    }

    /**
     * @return the confirmuserPassword2
     */
    public String getConfirmuserPassword2() {
        return confirmuserPassword2;
    }

    /**
     * @param confirmuserPassword2 the confirmuserPassword2 to set
     */
    public void setConfirmuserPassword2(String confirmuserPassword2) {
        this.confirmuserPassword2 = confirmuserPassword2;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public static class UserBuilder {

        private String userFullName;

        private String username;

        private String password;
        
        public UserBuilder setuserFullName(String fullName){
            this.userFullName = fullName;
            return this;
        }
        
        public UserBuilder setuserName(String useName){
            this.username = useName;
            return this;
        }
        
        public UserBuilder setuserPassword(String password){
            this.password = password;
            return this;
        }
        
        public User build(){
            User u = new User(this);
            return u;
        }

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
}
