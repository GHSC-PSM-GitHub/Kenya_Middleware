/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.dashboard.OrganizationUnit;
import java.util.ArrayList;
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
@Repository
@Transactional
public class OrganizationUnitDaoImpl extends GenericDaoImpl<OrganizationUnit, String> implements OrganizationUnitDao {

    @Override
    public void save(OrganizationUnit org) throws Exception {
        saveBean(org);
    }

    @Override
    public List<OrganizationUnit> getOrganizationUnits(String uId, String code, String parentId, String countyParent, String subCountyParent) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<OrganizationUnit> criteria = builder.createQuery(OrganizationUnit.class);
        Root<OrganizationUnit> root = criteria.from(OrganizationUnit.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (uId != null) {
            predicates.add(builder.equal(root.get("uid"), uId));
        }
        if (code != null) {
            predicates.add(builder.equal(root.get("code"), code));
        }
        if (parentId != null) {
            predicates.add(builder.equal(root.get("parent").get("id"), parentId));
        }
        if (countyParent != null) {
            predicates.add(builder.equal(root.get("countParent"), countyParent));
        }
        if (subCountyParent != null) {
            predicates.add(builder.equal(root.get("subCountParent"), subCountyParent));
        }
        if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return findByCriteria(-1, -1, criteria.select(root));
    }

    @Override
    public OrganizationUnit findById(String id) {
        return findBeanById(id);
    }
    
    @Override
    public OrganizationUnit findByUid(String uId) {
        return this.getOrganizationUnits(uId, null, null, null, null).size() < 1 ? null : this.getOrganizationUnits(uId, null, null, null, null).get(0);
    }
    
    @Override
    public OrganizationUnit findByOrgunitCode(String code) {
        return this.getOrganizationUnits(null, code, null, null, null).size() < 1 ? null : this.getOrganizationUnits(null, code, null, null, null).get(0);
    }

    @Override
    public void delete(String id) throws Exception {
        deleteBeanById(id);
    }

    @Override
    public OrganizationUnit findNationalOrgUnit() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<OrganizationUnit> criteria = builder.createQuery(OrganizationUnit.class);
        Root<OrganizationUnit> root = criteria.from(OrganizationUnit.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(builder.isNull(root.get("parent").get("id")));
     
        if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return findByCriteria(-1, -1, criteria.select(root)).size() < 1 ? null : findByCriteria(-1, -1, criteria.select(root)).get(0);
    }
    
}
