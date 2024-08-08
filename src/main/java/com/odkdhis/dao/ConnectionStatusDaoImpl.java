package com.odkdhis.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.ConnectionStatus;

@Repository
@Transactional
public class ConnectionStatusDaoImpl extends GenericDaoImpl<ConnectionStatus, String> implements ConnectionStatusDao {

	@Override
	public void save(ConnectionStatus conn) throws Exception {
		saveBean(conn);
	}

	@Override
	public ConnectionStatus getConnectionStatus() {
		return findAll(-1, -1).size() < 1 ? null : findAll(-1, -1).get(0);
	}

	@Override
	public ConnectionStatus findById(String id) {
		return findBeanById(id);
	}

	@Override
	public void delete(ConnectionStatus conn) throws Exception {
		deleteBeanById(conn.getId());
	}

	@Override
	public List<ConnectionStatus> getConnectionStatusses() {
		return findAll(-1, -1);
	}

}
