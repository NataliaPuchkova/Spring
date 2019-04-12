package proxy.repository;

import proxy.model.Account;
import proxy.model.RoutingInfo;

import java.util.Map;

public interface DataRepository {
	Account findById(String id);
	RoutingInfo findByURI(String id);
	Map<String, RoutingInfo> getEnv();
}
