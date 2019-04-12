package proxy.repository;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class APIDataRepositoryImpl {
	private static final Logger log = LoggerFactory.getLogger(DataRepositoryImpl.class);

	private final String LIST_ACCOUNTS = "select * from accounts";
	private final String ADD_ACCOUNTS = "insert into accounts(id,urlzone) values(?,?)";
	private final String UPDATE_ACCOUNTS = "update accounts set urlzone=? where id=?";
	private final String EXISTS_ACCOUNTS = "select * from accounts where id=?";
	private final String DELETE_ACCOUNTS = "delete from accounts where id=?";

	private final String LIST_RoutingInfo = "select * from routingInfo";
	private final String ADD_RoutingInfo = "insert into routingInfo(id	,uri,location,type,bodyfield,uripattern) values(?,?,?,?,?,?)";
	private final String UPDATE_RoutingInfo = "update routingInfo set uri=?,location=?,type=?,bodyfield=?,uripattern=? where id=?";
	private final String EXISTS_RoutingInfo = "select * from routingInfo where id=?";
	private final String DELETE_RoutingInfo = "delete from routingInfo where id=?";

	@Autowired
	@Qualifier("jdbcH2")
	private JdbcTemplate jdbcTemplate;

	public List<RoutingInfo> getRoutingInfo() {

		log.info("Retrieve: all env ");
		try {
			List<Map<String, Object>> routingInfo = jdbcTemplate.queryForList(LIST_RoutingInfo);
			return createDataFromResultSet(routingInfo);
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return new ArrayList<RoutingInfo>();
		}
	}

	private List<RoutingInfo> createDataFromResultSet(List<Map<String, Object>> rows) throws SQLException {

		log.info("createDataFromResultSet for Envs");

		List<RoutingInfo> list = new ArrayList<RoutingInfo>();

		for (Map rs : rows) {
			list.add( new RoutingInfo()
					.setUri((String) rs.get("uri"))
					.setId((String) rs.get("id"))
					.setLocation((String) rs.get("location"))
					.setType((String) rs.get("type"))
					.setBodyfield((String) rs.get("bodyfield"))
					.setUripattern((String) rs.get("uripattern")));
		}

		return list;
	}


	public RoutingInfo addRoutingInfo(RoutingInfo routingInfo) throws JdbcSQLException {

		jdbcTemplate.update(ADD_RoutingInfo, routingInfo.getEnvId(),
				routingInfo.getUri(),
				routingInfo.getLocation(),
				routingInfo.getType(),
				routingInfo.getBodyfield(),
				routingInfo.getUripattern());

		return routingInfo;
	}

	public void addRoutingInfo(final List<RoutingInfo> routingInfos) {

		jdbcTemplate.batchUpdate(ADD_RoutingInfo, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				RoutingInfo routingInfo = routingInfos.get(i);
				ps.setString(1, routingInfo.getEnvId());
				ps.setString(2, routingInfo.getUri());
				ps.setString(3, routingInfo.getLocation());
				ps.setString(4, routingInfo.getType());
				ps.setString(5, routingInfo.getBodyfield());
				ps.setString(6, routingInfo.getUripattern());
			}

			@Override
			public int getBatchSize() {
				return routingInfos.size();
			}
		});
	}

	public RoutingInfo updateRoutingInfo(RoutingInfo routingInfo)  {

		jdbcTemplate.update(UPDATE_RoutingInfo, routingInfo.getUri(),
				routingInfo.getLocation(),
				routingInfo.getType(),
				routingInfo.getBodyfield(),
				routingInfo.getUripattern(),
				routingInfo.getEnvId());

		return routingInfo;
	}

	public RoutingInfo deleteRoutingInfo(RoutingInfo routingInfo) throws JdbcSQLException {

		jdbcTemplate.update(DELETE_RoutingInfo, routingInfo.getEnvId());

		return routingInfo;
	}

	private boolean existsRoutingInfo(RoutingInfo routingInfo) throws JdbcSQLException {

		log.info("Retrieve: Information for id :" + routingInfo.getEnvId());
		try {
			List<RoutingInfo> routingInfos = jdbcTemplate.query(EXISTS_RoutingInfo, new Object[]{routingInfo.getEnvId()},
					(rs, rowNum) -> createDataFromResultSet(rs));
			return routingInfos.size() > 0;
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return false;
		}
	}

	private RoutingInfo createDataFromResultSet(ResultSet rs) throws SQLException {

		log.info("createEntryDataFromResultSet for RoutingInfo", rs.getString("id"), rs.getString("uri"));

		return new RoutingInfo()
				.setUri(rs.getString("uri"))
				.setId(rs.getString("id"))
				.setLocation(rs.getString("location"))
				.setType(rs.getString("type"))
				.setBodyfield(rs.getString("bodyfield"))
				.setUripattern(rs.getString("uripattern"));
	}

	public List<Account> getAccounts() {

		log.info("Retrieve: all users ");

		try {
			List<Map<String, Object>> accounts = jdbcTemplate.queryForList(LIST_ACCOUNTS);
			return createAccountsFromResultSet(accounts);
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return new ArrayList<Account>();
		}

	}

	private Account createAccountsFromResultSet(ResultSet rs) throws SQLException {

		return new Account()
				.setAccountId(rs.getString("id"))
				.setZone(rs.getString("urlzone"));

	}

	private List<Account> createAccountsFromResultSet(List<Map<String, Object>> rows) throws SQLException {
		List<Account> list = new ArrayList<Account>();
		rows.forEach(rs -> {
			list.add( new Account()
					.setAccountId((String) rs.get("id"))
					.setZone((String) rs.get("urlzone")));
		});

		return list;
	}

	public Account addAccount(Account account) throws JdbcSQLException {

		jdbcTemplate.update(ADD_ACCOUNTS, account.getAccountId(), account.getZone());

		return account;

	}

	public void addAccounts(final List<Account> accounts) {

		jdbcTemplate.batchUpdate(ADD_ACCOUNTS, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Account account = accounts.get(i);
				ps.setString(1, account.getAccountId());
				ps.setString(2, account.getZone());
			}

			@Override
			public int getBatchSize() {
				return accounts.size();
			}
		});

	}


	public Account updateAccount(Account account) throws JdbcSQLException {

		jdbcTemplate.update(UPDATE_ACCOUNTS, account.getZone(), account.getAccountId());

		return account;

	}

	public Account deleteAccount(Account account) throws JdbcSQLException {

		jdbcTemplate.update(DELETE_ACCOUNTS, account.getAccountId());

		return account;
	}

	public boolean existsAccount(Account account) throws JdbcSQLException {

		log.info("Retrieve: Information for account id :" + account.getAccountId());

		try {

			List<RoutingInfo> routingInfos = jdbcTemplate.query(EXISTS_ACCOUNTS, new Object[]{account.getAccountId()},
					(rs, rowNum) -> createDataFromResultSet(rs));

			return routingInfos.size() > 0;

		} catch (Exception e) {

			log.error("Error :" + e.getMessage());
			return false;

		}

	}


}
