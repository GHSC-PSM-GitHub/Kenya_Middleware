/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.dashboard.CurrentActionPointsModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ssvmoh
 */
@Transactional
@Repository
public class CurrentActionPointsDaoImpl extends GenericDaoImpl<CurrentActionPointsModel, String> implements CurrentActionPointsDao{
    
    
        @Override
    public void save(CurrentActionPointsModel currentactionpoints) throws Exception {
        saveBean(currentactionpoints);
    }

    @Override
    public CurrentActionPointsModel findById(String Id) {
        return findBeanById(Id);
    }

    
    public CurrentActionPointsModel getRecordByInstanceId(final String instanceId) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<CurrentActionPointsModel> criteria = builder.createQuery(CurrentActionPointsModel.class);
        Root<CurrentActionPointsModel> root = criteria.from(CurrentActionPointsModel.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("instanceId"), instanceId));
        criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        List<CurrentActionPointsModel> recs = findByCriteria(-1, -1, criteria.select(root));

        return recs.size() < 1 ? null : recs.get(0);
    }
    

    @Override
    public List<CurrentActionPointsModel> getCurrentActionPointsModels(String county, Date from, Date to) {
 
      CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<CurrentActionPointsModel> criteria = builder.createQuery(CurrentActionPointsModel.class);
        Root<CurrentActionPointsModel> root = criteria.from(CurrentActionPointsModel.class);
        
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
    
    
    
    

