/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.ActionPointsReviewModel;
import com.odkdhis.model.dashboard.RenalSectionModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface RenalSectionDao {
    
    void save(RenalSectionModel renalSectionData)throws Exception;
    
    RenalSectionModel findById(String Id);
    List<RenalSectionModel> getRenalSectionModels(String county, Date from, Date to);
    void delete(String Id)throws Exception;
      
}
