package proxy.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import proxy.repository.APIDataRepositoryImpl;
import proxy.route.service.APIServiceImpl;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableAutoConfiguration
@Service
public class APIDataServiceImpl {

	private static Logger log = LoggerFactory.getLogger(APIServiceImpl.class);

	@Autowired
	private APIDataRepositoryImpl repo;


	public List<RoutingInfo> getEnv() {
		return repo.getRoutingInfo();
	}

	public RoutingInfo addEnv(RoutingInfo routingInfo) throws JdbcSQLException {
		return repo.addRoutingInfo(routingInfo);
	}

	public List<RoutingInfo> addEnvs(List<RoutingInfo> routingInfos) throws JdbcSQLException {
		repo.addRoutingInfo(routingInfos);
		return routingInfos;
	}

	public RoutingInfo updateEnv(RoutingInfo routingInfo) throws JdbcSQLException {
		return repo.updateRoutingInfo(routingInfo);
	}

	public RoutingInfo deleteEnv(RoutingInfo routingInfo) throws JdbcSQLException {
		return repo.deleteRoutingInfo(routingInfo);
	}


	public List<Account> getAccounts() {
		return repo.getAccounts();
	}

	public Account addUser(Account account) throws JdbcSQLException {
		return repo.addAccount(account);
	}

	public List<Account> addUsers(List<Account> accounts) throws JdbcSQLException {
		repo.addAccounts(accounts);
		return accounts;
	}

	public Account updateAccount(Account account) throws JdbcSQLException {
		log.info(account.toString());
		if (existsAccount(account)) {
			log.info("Update account");
			return repo.updateAccount(account);
		} else {
			log.info("Add account");
			return repo.addAccount(account);
		}
	}

	public Account deleteAccount(Account account) throws JdbcSQLException {
		return repo.deleteAccount(account);
	}

	private boolean existsAccount(Account account) throws JdbcSQLException {
		return repo.existsAccount(account);

	}
}
