/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.dashboard.FacilitiesWithMatchingData;
import com.odkdhis.model.dashboard.FacilityAnalysis;
import com.odkdhis.model.dashboard.FacilityStock;
import com.odkdhis.model.dashboard.InventoryManagement;
import com.odkdhis.model.dashboard.ProductsWithMatchingData;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class InventoryManagementDaoImpl extends GenericDaoImpl<InventoryManagement, String> implements InventoryManagementDao{
    
    
        @Override
    public void save(InventoryManagement inventory) throws Exception {
        saveBean(inventory);
    }

    @Override
    public InventoryManagement findById(String Id) {
        return findBeanById(Id);
    }

    @Override
    public InventoryManagement getRecordByInstanceId(final String instanceId) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<InventoryManagement> criteria = builder.createQuery(InventoryManagement.class);
        Root<InventoryManagement> root = criteria.from(InventoryManagement.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("instanceId"), instanceId));
        criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        List<InventoryManagement> recs = findByCriteria(-1, -1, criteria.select(root));

        return recs.size() < 1 ? null : recs.get(0);
    }

    @Override
    public List<InventoryManagement> getInventoryManagements(String instanceId, String product, String county, LocalDate from, LocalDate to) {
      CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<InventoryManagement> criteria = builder.createQuery(InventoryManagement.class);
        Root<InventoryManagement> root = criteria.from(InventoryManagement.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (product != null) {
            predicates.add(builder.equal(root.get("product"), product));
        }
        
        if (instanceId != null) {
            predicates.add(builder.equal(root.get("instanceId"), instanceId));
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
    public List<FacilityStock> getFacilityStock(String county, Date from, Date to) {
        
        List<FacilityStock> sdata2 = new ArrayList<>();    
        String sql="select county,facility, product_name, \n" +
"        case \n" +
"        when stockstatus is null then 'NoData'\n" +
"        when stockstatus=0 then 'Out of Stock'\n" +
"        when stockstatus<3 then 'Understocked'\n" +
"        when stockstatus<6 then 'Stocked According to Plan' \n" +
"        when stockstatus>6 then 'Overstocked'\n" +
"        else 'NoData'\n" +
"        end as status\n" +
"        from(select county, facility, product_name, amc, actual_count, (actual_count/NULLIF(amc, 0)) as stockstatus from inventorymgt "
                + "where county =:county and date_collected between :from and :to)t \n" +
"                GROUP BY county, facility, product_name, stockstatus";
        
        String sql2="select county, facility as facility, product_name,\n" +
"        case \n" +
"        when stockstatus is null then 'NoData'\n" +
"        when stockstatus=0 then 'Out of Stock'\n" +
"        when stockstatus<3 then 'Understocked'\n" +
"        when stockstatus<6 then 'Stocked According to Plan' \n" +
"        when stockstatus>6 then 'Overstocked'\n" +
"        end as status\n" +
"        from(select county, facility, product_name, amc, actual_count, \n" +
"			 (actual_count/NULLIF(amc, 0)) as stockstatus from inventorymgt "
                + "where county =:county and date_collected between :from and :to)t\n" +
"             GROUP BY county, facility, product_name, status";
        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();               
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            sdata2.add(new FacilityStock(row[0].toString(),row[1].toString(), row[2].toString(), row[3].toString()));
            });
        
        return sdata2;
        
    }

    @Override
    public List<FacilityAnalysis> getFacilityAnalysis(String county, Date from, Date to) {
     
        List<FacilityAnalysis> sdata2 = new ArrayList<>(); 
        String sql = "select county,facility, product_name,\n" +
"        case \n" +
"        when stockstatus is null then 'NoData'\n" +
"        when stockstatus=0 then 'Out of Stock'\n" +
"        when stockstatus<3 then 'Understocked'\n" +
"        when stockstatus<6 then 'Stocked According to Plan' \n" +
"        when stockstatus>6 then 'Overstocked'\n" +
"		else 'NoData'\n" +
"        end as status\n" +
"from\n" +
"(select county, COUNT(facility) as facility,product_name, (actual_count/NULLIF(amc, 0)) as stockstatus \n" +
"from inventorymgt where county =:county and date_collected between :from and :to GROUP BY county,product_name, stockstatus)t\n" +
"GROUP BY county,facility,status,product_name";
        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();
                
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            sdata2.add(new FacilityAnalysis(row[0].toString(), Integer.parseInt(row[1].toString()), row[2].toString(),row[3].toString()));
            });
        Integer totalFacilities = sdata2.stream().map(f -> f.getFacility()).reduce(0, (a,b) -> a + b);
        sdata2.forEach(f -> f.setPercentage(getPercentage(f.getFacility(), new BigDecimal(totalFacilities))));
        
        return sdata2;
    }
        
       private BigDecimal getPercentage(Integer rec, BigDecimal total){
           BigDecimal f = (new BigDecimal(rec)).divide(total, 10, RoundingMode.CEILING);
           return f.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
       }

    @Override
    public List<ProductsWithMatchingData> getMatchingData(String county, Date from, Date to) {
                List<ProductsWithMatchingData> sdata2 = new ArrayList<>(); 
        String sql = "(select v.county, v.product_name,\n" +
"(select count(v3.facility) from inventorymgt v3 where v3.product_name =v.product_name) as Total,\n" +
"(select count(v1.facility) from inventorymgt v1 where v1.card_balance=v1.actual_count and v1.product_name =v.product_name) as Matching,\n" +
"(select count(v2.facility) from inventorymgt v2 where v2.card_balance!=v2.actual_count and v2.product_name =v.product_name) as Not_Matching\n" +
"from inventorymgt v where county =:county and date_collected between :from and :to GROUP BY v.product_name, v.county)";
        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();
                
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            sdata2.add(new ProductsWithMatchingData(row[0].toString(), row[1].toString(), Integer.parseInt(row[2].toString()), Integer.parseInt(row[3].toString()), Integer.parseInt(row[4].toString())));
            });
        
         sdata2.forEach(f -> f.setPercentage(getPercentage(f.getMatching(), new BigDecimal(f.getAllfacilities()))));
        return sdata2;
    }

    @Override
    public List<FacilitiesWithMatchingData> getFacilitiesWithMatchingData(String county, Date from, Date to) {
               
        List<FacilitiesWithMatchingData> sdata2 = new ArrayList<>();    
        String sql="(select v.county, v.product_name,\n" +
"(select count(v3.facility) from inventorymgt v3 where v3.product_name =v.product_name and v3.county =:county and v3.date_collected between :from and :to) as Total,\n" +
"(select count(v1.facility) from inventorymgt v1 where v1.card_balance=v1.actual_count and v1.product_name =v.product_name and v1.county =:county and v1.date_collected between :from and :to) as Matching,\n" +
"(select count(v2.facility) from inventorymgt v2 where v2.card_balance!=v2.actual_count and v2.product_name =v.product_name and v2.county =:county and v2.date_collected between :from and :to ) as Not_Matching\n" +
"from inventorymgt v where v.county =:county and v.date_collected between :from and :to GROUP BY v.product_name, v.county)";

        
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("county", county).setParameter("from", from).setParameter("to", to)
                .getResultList();               
        List<Object[]> rows = query.list();
        rows.forEach(row -> {
            
            sdata2.add(new FacilitiesWithMatchingData(row[0].toString(),row[1].toString(),Integer.parseInt(row[2].toString()),Integer.parseInt(row[3].toString()),Integer.parseInt(row[4].toString())));
            });
        sdata2.forEach(f -> f.setPercentage(getPercentage(f.getMatching(), new BigDecimal(f.getAllfacilities()))));
        
        return sdata2;
    }
        
   }
    

