package com.odkdhis.dao;

import java.util.List;

import com.odkdhis.model.ConnectionStatus;

public interface ConnectionStatusDao {
	
	void save(ConnectionStatus conn) throws Exception;

	List<ConnectionStatus> getConnectionStatusses();
	
	ConnectionStatus getConnectionStatus();

    ConnectionStatus findById(String id);

    void delete(ConnectionStatus conn) throws Exception;

}
