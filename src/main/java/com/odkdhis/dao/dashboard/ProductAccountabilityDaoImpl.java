/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.dashboard.ProductAccountability;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.FacilitiesMeetingSohExpectation;
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
public class ProductAccountabilityDaoImpl extends GenericDaoImpl<ProductAccountability, String> implements ProductAccountabilityDao{
    
    
        @Override
    public void save(ProductAccountability prod) throws Exception {
        saveBean(prod);
    }

    @Override
    public ProductAccountability findById(String Id) {
        return findBeanById(Id);
    }


    @Override
    public List<ProductAccountability> getProductAccountabilities(String submissionId, String product, String county, LocalDate from, LocalDate to) {
      CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<ProductAccountability> criteria = builder.createQuery(ProductAccountability.class);
        Root<ProductAccountability> root = criteria.from(ProductAccountability.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (product != null) {
            predicates.add(builder.equal(root.get("product"), product));
        }
        
        if (submissionId != null) {
            predicates.add(builder.equal(root.get("instanceId"), submissionId));
        }
        
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

    @Override
    public List<ProductsMeetingSohExpectation> getSohExpectedData(String county, Date from, Date to) {
       /* String query = "select p.product, \n" +
"(select count(p1.id) from ProductAccountability p1 where p1.sohExpected = 'Yes' and p1.product =p.product ) as yes_count,\n" +
"(select count(p2.id) from ProductAccountability p2 where p2.sohExpected = 'No' and p2.product =p.product) as no_count,\n" +
"(select count(p3.id) from ProductAccountability p3 where p3.product =p.product ) as prod_count\n" +
"from ProductAccountability p GROUP BY product\n" +
"where p.county =:county and p.date_collected between :from and :to";
        
        List<SohExpectedData> sdata = em.createQuery(query, ProductsMeetingSohExpectation.class)
                .setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();
        */
       List<ProductsMeetingSohExpectation> sdata = new ArrayList<>();
        String sql = "select p.product_name, \n" +
"(select count(p1.id) from products_tbl p1 where p1.soh_expected = 'Yes' and p1.product_name =p.product_name and p1.county =:county and p1.date_collected between :from and :to ) as yes_count,\n" +
"(select count(p2.id) from products_tbl p2 where p2.soh_expected = 'No' and p2.product_name =p.product_name and p2.county =:county and p2.date_collected between :from and :to) as no_count,\n" +
"(select count(p3.id) from products_tbl p3 where p3.product_name =p.product_name and p3.county =:county and p3.date_collected between :from and :to ) as prod_count\n" +
"from products_tbl p where p.county =:county and p.date_collected between :from and :to"
                + " GROUP BY product_name";
        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();
                
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            sdata.add(new ProductsMeetingSohExpectation(row[0].toString(), Integer.parseInt(row[1].toString()), Integer.parseInt(row[2].toString()), Integer.parseInt(row[3].toString())));
            });
        
        
        return sdata;
    }
    
    @Override
    public List<FacilitiesMeetingSohExpectation> getSohExpectedDataPerFacility(String county, Date from, Date to) {
        

       List<FacilitiesMeetingSohExpectation> sdata2 = new ArrayList<>();
        String sql = "select p.facility, \n" +
"(select count(p1.id) from products_tbl p1 where p1.soh_expected = 'Yes' and p1.facility =p.facility and p1.county =:county and p1.date_collected between :from and :to ) as yes_count,\n" +
"(select count(p2.id) from products_tbl p2 where p2.soh_expected = 'No' and p2.facility =p.facility and p2.county =:county and p2.date_collected between :from and :to) as no_count,\n" +
"(select count(p3.id) from products_tbl p3 where p3.facility =p.facility and p3.county =:county and p3.date_collected between :from and :to ) as prod_count\n" +
"from products_tbl p where p.county =:county and p.date_collected between :from and :to"
                + " GROUP BY facility";
        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();
                
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            sdata2.add(new FacilitiesMeetingSohExpectation(row[0].toString(), Integer.parseInt(row[1].toString()), Integer.parseInt(row[2].toString()), Integer.parseInt(row[3].toString())));
            });
        
        
        return sdata2;
    }
    
    
    
    
}
