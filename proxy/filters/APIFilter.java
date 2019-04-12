package proxy.filters;

import proxy.route.service.APIServiceImpl;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class APIFilter extends ZuulFilter {

	private static final int PRE_DECORATION_FILTER_ORDER = 6;
	private static final String PRE_TYPE = "pre";
	private static final String API_MARKER = "api";
	private static Logger log = LoggerFactory.getLogger(APIFilter.class);
	@Autowired
	APIServiceImpl service;

	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return PRE_DECORATION_FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		if (request.getRequestURI().contains(API_MARKER)) {
			log.info("API request ------------------------------");
			log.info("Will update database here");
			return true;
		}

		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		service.execute(ctx);
		return null;
	}

}
