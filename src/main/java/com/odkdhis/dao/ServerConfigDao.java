package com.odkdhis.dao;

import java.util.List;

import com.odkdhis.model.ServerConfig;

public interface ServerConfigDao {
	
	void save(ServerConfig config) throws Exception;

	ServerConfig getServerConfig();
	
	List<ServerConfig> getServerConfigs();

	ServerConfig findById(String id);

    void delete(ServerConfig config) throws Exception;

}
