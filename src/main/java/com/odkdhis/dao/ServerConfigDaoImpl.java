package com.odkdhis.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.ServerConfig;

@Repository
@Transactional
public class ServerConfigDaoImpl extends GenericDaoImpl<ServerConfig, String> implements ServerConfigDao {

	@Override
	public void save(ServerConfig config) throws Exception {
		saveBean(config);
	}

	@Override
	public ServerConfig getServerConfig() {
		return findAll(-1, -1).size() < 1 ? null : findAll(-1, -1).get(0);
	}

	@Override
	public ServerConfig findById(String id) {
		return findBeanById(id);
	}

	@Override
	public void delete(ServerConfig config) throws Exception {
		deleteBeanById(config.getId());
	}

	@Override
	public List<ServerConfig> getServerConfigs() {
		return findAll(-1, -1);
	}

}
