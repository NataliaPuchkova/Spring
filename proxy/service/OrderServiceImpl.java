public class package proxy.service;

import proxy.repository.HybrisDataRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

@EnableAutoConfiguration
@Service
public class OrderServiceImpl implements OrderService {
	private static Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

	@Autowired
	private HybrisDataRepositoryImpl repo;

	@Override
	public boolean existsOrder(String orderId) {
		return repo.findById(orderId);
	}
} implements OrderService {
	private static Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

	@Autowired
	private HybrisDataRepositoryImpl repo;

	@Override
	public boolean existsOrder(String orderId) {
		return repo.findById(orderId);
	}
}
