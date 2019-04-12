package proxy.repository;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataRepositoryImpl implements DataRepository {

	private static final Logger log = LoggerFactory.getLogger(DataRepositoryImpl.class);

	// Accounts
	private final String GET_INFO_BY_ACCOUNT_ID = "select * from accounts where id=?";

	// Environment
	private final String GET_LIST_OF_ENV = "select * from routingInfo";
	private final String GET_ENV_BY_URI = "select * from routingInfo where uri like '%?%'";

	@Autowired
	@Qualifier("jdbcH2")
	private JdbcTemplate jdbcTemplate;

	public Account findById(String id) {

		log.info("Retrieve: Information for Account ID :" + id);
		try {
			List<Account> accounts = jdbcTemplate.query(GET_INFO_BY_ACCOUNT_ID, new Object[]{id},
					(rs, rowNum) -> createEntryDataFromResultSet(rs));
			if (accounts.size() > 0) return accounts.get(0);
			else return new Account();
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return new Account();
		}
	}

	private Account createEntryDataFromResultSet(ResultSet rs) throws SQLException {
		log.info("Found account, zone=" + rs.getString("urlzone") + ", account=" + rs.getString("id"));
		return (new Account()
				.setZone(rs.getString("urlzone"))
				.setAccountId(rs.getString("id")));
	}

	public RoutingInfo findByURI(String id) {

		log.info("Retrieve: Information for URI :" + id);
		try {
			List<RoutingInfo> routingInfos = jdbcTemplate.query(GET_ENV_BY_URI, new Object[]{id},
					(rs, rowNum) -> createDataFromResultSet(rs));
			if (routingInfos.size() > 0) return routingInfos.get(0);
			else return new RoutingInfo();
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return new RoutingInfo();
		}
	}

	public Map<String, RoutingInfo> getEnv() {

		log.info("Retrieve: all env ");
		try {
			List<Map<String, Object>> envs = jdbcTemplate.queryForList(GET_LIST_OF_ENV);
			return createMapFromResultSet(envs);
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			return new HashMap<String, RoutingInfo>();
		}
	}

	private RoutingInfo createDataFromResultSet(ResultSet rs) throws SQLException {
		log.info("createEntryDataFromResultSet for RoutingInfo", rs.getString("id"), rs.getString("uri"));
		return ( new RoutingInfo()
				.setUri(rs.getString("uri").toLowerCase())
				.setId(rs.getString("id"))
				.setLocation(rs.getString("location"))
				.setType(rs.getString("type"))
				.setBodyfield(rs.getString("bodyfield"))
				.setUripattern(rs.getString("uripattern")));
	}

	private Map<String, RoutingInfo> createMapFromResultSet(List<Map<String, Object>> rows) {
		log.info("createDataFromResultSet for Envs");
		Map<String, RoutingInfo> map = new HashMap<String, RoutingInfo>();
		rows.forEach(rs->
				map.put(((String) rs.get("uri")).toLowerCase(), new RoutingInfo()
					.setUri(((String) rs.get("uri")).toLowerCase())
					.setId((String) rs.get("id"))
					.setLocation((String) rs.get("location"))
					.setType((String) rs.get("type"))
					.setBodyfield((String) rs.get("bodyfield"))
					.setUripattern((String) rs.get("uripattern"))));
		return map;
	}
}
