package proxy.route.service;

import com.netflix.zuul.context.RequestContext;

public interface RouteService {
	void route(RequestContext ctx);
}
