/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao;

import com.odkdhis.model.DataValueGroup;
import java.util.List;

/**
 *
 * @author ssvmoh
 */
public interface DataValueGroupDao {
    
    void save(DataValueGroup val) throws Exception;
    
    List<DataValueGroup> getDatasetValueGroups(Boolean dataSend, Boolean dataSendTosummary);

    DataValueGroup findById(String id);

    void delete(DataValueGroup val) throws Exception;
    
}
