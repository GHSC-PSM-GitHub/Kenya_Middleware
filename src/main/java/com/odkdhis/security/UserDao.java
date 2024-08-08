/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.security;

import com.odkdhis.model.User;

/**
 *
 * @author Paul Omboi
 */
public interface UserDao {
    
    void save(User user)throws Exception;
    
    void createDefaultUser()throws Exception;
    
    User findById(String id);
    
    User findByUsername(String username);
}
