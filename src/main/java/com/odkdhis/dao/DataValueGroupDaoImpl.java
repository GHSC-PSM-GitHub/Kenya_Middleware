/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.DataValueGroup;
import com.odkdhis.model.DatasetValue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
@Repository
@Transactional
public class DataValueGroupDaoImpl extends GenericDaoImpl<DataValueGroup, String> implements DataValueGroupDao {

    @Override
    public void save(DataValueGroup val) throws Exception {
        saveBean(val);
    }

    @Override
    public List<DataValueGroup> getDatasetValueGroups(Boolean dataSend, Boolean dataSendTosummary) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<DataValueGroup> criteria = builder.createQuery(DataValueGroup.class);
        Root<DataValueGroup> root = criteria.from(DataValueGroup.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
         if (dataSend != null) {
            predicates.add(builder.equal(root.get("dataSend"), dataSend));
        }
        if (dataSendTosummary != null) {
            predicates.add(builder.equal(root.get("dataSendTosummary"), dataSendTosummary));
        }
        
         if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
      
        List<DataValueGroup> dvs = findByCriteria(-1, -1, criteria.select(root));
        
        dvs = dvs.stream().filter(dv -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            Date todayFormatted = null;
            try {
                todayFormatted = format.parse(dateFormat.format(today));
            } catch (ParseException ex) {
                Logger.getLogger(DatasetValueDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return !(dv.getDateCollected()).equals(todayFormatted);
        }).collect(Collectors.toList());

        return dvs;
    }

    @Override
    public DataValueGroup findById(String id) {
        return findBeanById(id);
    }

    @Override
    public void delete(DataValueGroup val) throws Exception {
        deleteBeanById(val.getId());
    }
    
}
