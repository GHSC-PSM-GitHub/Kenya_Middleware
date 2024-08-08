/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.AdministratorReports;
import com.odkdhis.model.dashboard.ProductAccountability;
import com.odkdhis.model.dashboard.ProductName;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.FacilitiesMeetingSohExpectation;
import com.odkdhis.model.dashboard.OnJobTrainingModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface OnJobTrainingDao {
    
    void save(OnJobTrainingModel onjobtraining)throws Exception;
    
    OnJobTrainingModel findById(String Id);
    List<OnJobTrainingModel> getOnJobTrainingModels(String county, Date from, Date to);
    void delete(String Id)throws Exception;
      
}
