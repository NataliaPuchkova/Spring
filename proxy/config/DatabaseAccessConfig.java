import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseAccessConfig {
	@Bean(name = "h2")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "oracle")
	@ConfigurationProperties(prefix = "oracle.datasource")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcOracle")
	@Autowired
	public JdbcTemplate slaveJdbcTemplate(@Qualifier("oracle") DataSource oracle) {
		return new JdbcTemplate(oracle);
	}

	@Bean(name = "jdbcH2")
	@Autowired
	public JdbcTemplate masterJdbcTemplate(@Qualifier("h2") DataSource h2) {
		return new JdbcTemplate(h2);
	}
}
