package proxy.config;

import proxy.filters.APIFilter;
import proxy.filters.PostFilter;
import proxy.filters.PreFilter;
import proxy.route.model.FunctionalZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties
@Configuration
public class FilterConfig {
	@Value("${environments.urlz1}")
	private String URL1;
	@Value("${environments.urlz2.1}")
	private String URL21;
	@Value("${environments.urlz2.2}")
	private String URL22;

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public APIFilter apiFilter() {
		return new APIFilter();
	}

	@Bean(name = "zone1")
	public FunctionalZone beanZone1() {
		return new FunctionalZone("", URL1);
	}

	@Bean(name = "zone2")
	public FunctionalZone beanZone2() {
		return new FunctionalZone(URL21, URL22);
	}

}
