/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.dashboard.LlinsModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface LlinsDao {
    
    void save(LlinsModel llins)throws Exception;
    
    LlinsModel findById(String Id);
    List<LlinsModel> getLlinsModels(String county, Date from, Date to);
    void delete(String Id)throws Exception;
      
}
