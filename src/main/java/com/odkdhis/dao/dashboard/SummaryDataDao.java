/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.DisplayData;
import com.odkdhis.model.dashboard.SummaryData;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ssvmoh
 */
public interface SummaryDataDao {
    
    void save(SummaryData data)throws Exception;
    
    SummaryData findById(String Id);
    
    List<DisplayData> getDisplayDataList(String orgCode, Date from, Date to);
    
    List<SummaryData> getFacilitySummaryData(FormName formName, String orgCode, Date from, Date to);
    
    void delete(String Id)throws Exception;
    
}
