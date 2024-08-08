/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.dashboard.AdministratorReports;
import com.odkdhis.model.dashboard.ProductAccountability;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.FacilitiesMeetingSohExpectation;
import com.odkdhis.model.dashboard.InventoryManagement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ssvmoh
 */
@Transactional
@Repository
public class AdministratorReportsDaoImpl extends GenericDaoImpl<AdministratorReports, String> implements AdministratorReportsDao{
    
    
        @Override
    public void save(AdministratorReports adminReport) throws Exception {
        saveBean(adminReport);
    }

    @Override
    public AdministratorReports findById(String Id) {
        return findBeanById(Id);
    }

    
    public AdministratorReports getRecordByInstanceId(final String instanceId) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<AdministratorReports> criteria = builder.createQuery(AdministratorReports.class);
        Root<AdministratorReports> root = criteria.from(AdministratorReports.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("instanceId"), instanceId));
        criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        List<AdministratorReports> recs = findByCriteria(-1, -1, criteria.select(root));

        return recs.size() < 1 ? null : recs.get(0);
    }
    

    @Override
    public List<AdministratorReports> getAdministratorReports(String county, Date from, Date to) {
 
      CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<AdministratorReports> criteria = builder.createQuery(AdministratorReports.class);
        Root<AdministratorReports> root = criteria.from(AdministratorReports.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (county != null) {
            predicates.add(builder.equal(root.get("county"), county));
        }
        if (from != null && to != null) {
            predicates.add(builder.between(root.get("dateCollected"), from, to));
        }
        if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return findByCriteria(-1, -1, criteria.select(root));
    }

    
    @Override
    public void delete(String Id) throws Exception {
        deleteBeanById(Id);
    }

    }
    
    
    
    

