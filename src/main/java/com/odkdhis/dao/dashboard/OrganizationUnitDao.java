/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.dashboard.OrganizationUnit;
import java.util.List;

/**
 *
 * @author ssvmoh
 */
public interface OrganizationUnitDao {
    
    void save(OrganizationUnit org) throws Exception;

    List<OrganizationUnit> getOrganizationUnits(String uId, String code, String parentId, String countyParent, String subCountyParent);

    OrganizationUnit findById(String id);
    
    OrganizationUnit findNationalOrgUnit();
    
    OrganizationUnit findByUid(String uid);
    
    OrganizationUnit findByOrgunitCode(String code);

    void delete(String id) throws Exception;
    
}
