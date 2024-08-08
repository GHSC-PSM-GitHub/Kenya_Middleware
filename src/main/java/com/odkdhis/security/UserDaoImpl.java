/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.security;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDaoImpl extends GenericDaoImpl<User, String> implements UserDao{
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) throws Exception {
        if(user.getUserPassword2() != null){
            user.setPassword(bCryptPasswordEncoder.encode(user.getUserPassword2()));
        }
        saveBean(user);
    }

    @Override
    public User findById(String id) {
        return findBeanById(id);
    }

    @Override
    public User findByUsername(String username) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        List<User> users = findByCriteria(-1, -1, criteria.select(root).where(cb.equal(root.get("username"), username)));
        return users.size() < 1 ? null : users.get(0);
    }

    @Override
    public void createDefaultUser() throws Exception {
        User adminUser = this.findByUsername("admin");
        if(adminUser == null){
            adminUser = new User.UserBuilder().setuserFullName("Admin User").setuserName("admin").setuserPassword(bCryptPasswordEncoder.encode("admin123")).build();
            this.save(adminUser);
        }
    }
    
}
