package proxy.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PostFilter extends ZuulFilter {
	private static final int POST_DECORATION_FILTER_ORDER = 10;
	private static final String POST_TYPE = "post";
	private static final Logger log = LoggerFactory.getLogger(PostFilter.class);

	@Override
	public String filterType() {
		return POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return POST_DECORATION_FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		if (!request.getRequestURI().contains("api")) {
			log.info("Request post" + request.getRequestURI());
			return true;
		}
		return false;
	}

	@Override
	public Object run() {
		// Read the compressed response
		RequestContext ctx = RequestContext.getCurrentContext();
		try (final InputStream responseDataStream = ctx.getResponseDataStream()) {
			final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, StandardCharsets.UTF_8));
			log.info("Host:" + ctx.getRouteHost().getHost());
			log.info("Response:" + responseData);
			ctx.setResponseBody(responseData);
		} catch (IOException e) {
			log.error("Error reading body", e);
		}
		return null;
	}
}
