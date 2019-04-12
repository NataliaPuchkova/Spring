package proxy.repository;

import proxy.model.Account;
import proxy.model.RoutingInfo;

import java.util.List;

public interface APIRepository {
	List<RoutingInfo> getEnvs();

	RoutingInfo addEnv(RoutingInfo routingInfo);

	RoutingInfo updateEnv(RoutingInfo routingInfo);

	RoutingInfo deleteEnv(RoutingInfo routingInfo);

	void addEnvs(final List<RoutingInfo> routingInfos);

	List<Account> getAccounts();

	Account addAccount(Account account);

	Account updateAccount(Account account);

	Account deleteAccunt(Account account);

	void addAccounts(final List<Account> accounts);

}
