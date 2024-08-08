/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.dashboard.FacilitiesWithMatchingData;
import com.odkdhis.model.dashboard.FacilityAnalysis;
import com.odkdhis.model.dashboard.FacilityStock;
import com.odkdhis.model.dashboard.InventoryManagement;
import com.odkdhis.model.dashboard.ProductsWithMatchingData;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.FacilitiesMeetingSohExpectation;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface InventoryManagementDao {
    
    void save(InventoryManagement inventory)throws Exception;
    
    InventoryManagement findById(String Id);

    InventoryManagement getRecordByInstanceId(String instanceId);
    
    List<InventoryManagement> getInventoryManagements(String instanceId, String product, String county, LocalDate from, LocalDate to);
    List<FacilityStock>getFacilityStock(String county, Date from, Date to);
    List<FacilityAnalysis>getFacilityAnalysis(String county, Date from, Date to);
    List<ProductsWithMatchingData>getMatchingData(String county, Date from, Date to);
    List<FacilitiesWithMatchingData>getFacilitiesWithMatchingData(String county, Date from, Date to);
    
     
    void delete(String Id)throws Exception;
      
}
