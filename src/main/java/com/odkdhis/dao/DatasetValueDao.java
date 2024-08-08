/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao;

import com.odkdhis.model.DatasetValue;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ssvmoh
 */
public interface DatasetValueDao {

    void save(DatasetValue val) throws Exception;

    List<DatasetValue> getDatasetValues(Date dateCollected, Boolean dataSend);
    
    List<DatasetValue> getDatasetValues(Boolean dataSend);

    DatasetValue findById(String id);

    void delete(DatasetValue val) throws Exception;

}
