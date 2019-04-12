package proxy.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import proxy.repository.DataRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@EnableAutoConfiguration
@Service
public class DataServiceImpl implements DataService {
	private static Logger log = LoggerFactory.getLogger(DataServiceImpl.class);
	@Autowired
	StringServiceImpl stringServiceImpl;
	@Autowired
	private DataRepositoryImpl repo;
	private Map<String, RoutingInfo> envs;

	/**
	 * @param id AccountID
	 * @return Account object
	 */
	public Account getByName(String id) {
		if (id != null && !id.equals(""))
			return repo.findById(id);
		else
			return new Account();
	}

	/**
	 * @param id Uri for search in RoutingInfo table
	 * @return RoutingInfo object
	 */
	public RoutingInfo getByUri(String id) {
		if (id != null && !id.equals(""))
			return repo.findByURI(id);
		else
			return new RoutingInfo();
	}

	/**
	 * @return Map of environment rows, key is URO, value is RoutingInfo object
	 */
	public Map<String, RoutingInfo> getEnv() {
		return repo.getEnv();
	}

	/**
	 * @param request Request
	 * @return RoutingInfo object
	 */
	public RoutingInfo getEnv(HttpServletRequest request) {
		if (this.envs == null) {
			envs = getEnv();
			log.info("Take info about environments");
		}
		RoutingInfo routingInfo = null;
		String cleanedStr = stringServiceImpl.cleanUri(request.getRequestURI().toLowerCase());
		log.info("Cleaned uri=" + cleanedStr);
		if (envs != null) routingInfo = envs.get(cleanedStr);
		return routingInfo;
	}
}
