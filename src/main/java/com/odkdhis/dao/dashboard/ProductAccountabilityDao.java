/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.ProductAccountability;
import com.odkdhis.model.dashboard.ProductName;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.FacilitiesMeetingSohExpectation;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface ProductAccountabilityDao {
    
    void save(ProductAccountability prod)throws Exception;
    
    ProductAccountability findById(String Id);
    
    //List<DisplayData> getDisplayDataList(String orgCode, Date from, Date to);
    
    List<ProductAccountability> getProductAccountabilities(String submissionId,String productName, String county, LocalDate from, LocalDate to);
    List<ProductsMeetingSohExpectation> getSohExpectedData(String county, Date from, Date to);
    List<FacilitiesMeetingSohExpectation> getSohExpectedDataPerFacility(String county, Date from, Date to);
    
    
    void delete(String Id)throws Exception;
      
}
