package proxy.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;

import java.util.List;

public interface APIDataService {
	List<RoutingInfo> getEnv();

	RoutingInfo addEnv(RoutingInfo routingInfo);

	List<RoutingInfo> addEnvs(List<RoutingInfo> routingInfos);

	RoutingInfo updateEnv(RoutingInfo routingInfo);

	RoutingInfo deleteEnv(RoutingInfo routingInfo);

	List<Account> getAccounts();

	Account addUser(Account account);

	List<Account> addUsers(List<Account> accounts);

	Account updateAccount(Account account);

	Account deleteUser(Account account);
}
