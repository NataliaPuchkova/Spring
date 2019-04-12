package proxy.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
@Qualifier("secondaryDataSource")
public class HybrisDataRepositoryImpl implements HybrisDataRepository {

	private static final Logger log = LoggerFactory.getLogger(HybrisDataRepositoryImpl.class);

	private static String cartsSql = "SELECT 1 FROM carts WHERE id = ? ";

	@Autowired
	@Qualifier("jdbcOracle")
	private JdbcTemplate jdbcTemplate;

	public boolean findById(String id) {
		log.info("Retrieve: Information for OrderId :" + id);
		try {
			List<Object> orders;
			orders = jdbcTemplate.query(cartsSql, new Object[]{id, id, id},
					(rs, rowNum) -> createDataFromResultSet());
			log.info("Check in  Carts table");
			return orders.size() > 0;
		} catch (Exception e) {
			log.error("Error :" + e.getMessage());
			e.getStackTrace();
			return false;
		}

	}

	private Object createDataFromResultSet() {
		Object obj = new Object();
		return obj;
	}
}

