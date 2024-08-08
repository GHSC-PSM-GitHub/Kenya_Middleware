/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.model.dashboard.VerificationDataModel;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Omboi
 */
public interface VerificationDataDao {
    
    void save(VerificationDataModel verificationData)throws Exception;
    
    VerificationDataModel findById(String Id);
    List<VerificationDataModel> getVerificationDataModels(String county, Date from, Date to);
    void delete(String Id)throws Exception;
      
}
