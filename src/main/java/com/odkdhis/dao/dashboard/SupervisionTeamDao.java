/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.SupervisionTeamModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface SupervisionTeamDao {
    
    void save(SupervisionTeamModel supervisionteam)throws Exception;
    
    SupervisionTeamModel findById(String Id);
    List<SupervisionTeamModel> getSupervisionTeamModels(String county, Date from, Date to);
    void delete(String Id)throws Exception;
      
}
