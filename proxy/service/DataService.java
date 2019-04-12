package proxy.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;

import javax.servlet.http.HttpServletRequest;

public interface DataService {

	Account getByName(String id);

	RoutingInfo getByUri(String id);

	RoutingInfo getEnv(HttpServletRequest request);
}
