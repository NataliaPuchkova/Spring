package proxy.route.service;

import com.netflix.zuul.context.RequestContext;

public interface APIService {
	void execute(RequestContext ctx);
}
