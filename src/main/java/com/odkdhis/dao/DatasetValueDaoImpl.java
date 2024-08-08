/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao;

import com.odkdhis.commondao.GenericDaoImpl;
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
 * @author simiyu
 */
@Repository
@Transactional
public class DatasetValueDaoImpl extends GenericDaoImpl<DatasetValue, String> implements DatasetValueDao {

    @Override
    public void save(DatasetValue val) throws Exception {
        saveBean(val);
    }

    @Override
    public List<DatasetValue> getDatasetValues(Date dateCollected, Boolean dataSend) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<DatasetValue> criteria = builder.createQuery(DatasetValue.class);
        Root<DatasetValue> root = criteria.from(DatasetValue.class);
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        predicates.add(builder.equal(root.get("dataSend"), dataSend));
        predicates.add(builder.equal(root.get("dateCollected"), dateCollected));
        List<DatasetValue> dvs = findByCriteria(-1, -1, criteria.select(root));
        //List<DatasetValue> dvs = dataSend != null ? findByCriteria(-1, -1, criteria.select(root).where(builder.equal(root.get("dataSend"), dataSend))) : findAll(-1, -1);
        
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
            return !(dv.getDataGroup().getDateCollected()).equals(todayFormatted);
        }).collect(Collectors.toList());

        return dvs;

    }
    
     @Override
    public List<DatasetValue> getDatasetValues( Boolean dataSend) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<DatasetValue> criteria = builder.createQuery(DatasetValue.class);
        Root<DatasetValue> root = criteria.from(DatasetValue.class);
      
        List<DatasetValue> dvs = dataSend != null ? findByCriteria(-1, -1, criteria.select(root).where(builder.equal(root.get("dataSend"), dataSend))) : findAll(-1, -1);
        
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
            return !(dv.getDataGroup().getDateCollected()).equals(todayFormatted);
        }).collect(Collectors.toList());

        return dvs;

    }

    @Override
    public DatasetValue findById(String id) {
        return findBeanById(id);
    }

    @Override
    public void delete(DatasetValue val) throws Exception {
        deleteBeanById(val.getId());
    }

}
