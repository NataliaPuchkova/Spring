package proxy.route.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import proxy.route.model.FunctionalZone;
import proxy.route.model.Request;
import proxy.service.DataServiceImpl;
import proxy.service.OrderService;
import proxy.service.StringServiceImpl;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class RouteServiceImpl implements RouteService {

	private static Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);

	@Autowired
	private DataServiceImpl dataService;
	@Autowired
	private StringServiceImpl stringService;
	@Autowired
	private OrderService orderService;

	@Autowired
	@Qualifier("zone1")
	private FunctionalZone zone1;

	@Autowired
	@Qualifier("zone2")
	private FunctionalZone zone2;

	public void route(RequestContext ctx) {

		RoutingInfo routingInfo = dataService.getEnv(ctx.getRequest());
		String routeUrl = determineRouteUrl(routingInfo, ctx.getRequest());
		log.info("routeUrl="+routeUrl);
		if (!routeUrl.equals("")) {
			try {
				ctx.set("requestURI", ctx.getRequest().getRequestURI());
				ctx.setRouteHost(new URL(routeUrl));
			} catch (MalformedURLException e) {
				log.error("Error when setup Url " + e.getMessage(), e);
			}
		}
	}

	private String determineRouteUrl(RoutingInfo routingInfo, HttpServletRequest request) {
		if (determineSchema(routingInfo, request) == 1) {
			return getZone(request).getUat1Url();
		} else {
			return getZone(request).getUat2Url();
		}
	}

	private FunctionalZone getZone(HttpServletRequest request) {
		if (request.getServerName().toLowerCase().contains("uat2")) {

			log.info("zone2="+zone2.toString());
			return zone2;
		}
		log.info("zone1="+zone1.toString());
		return zone1;
	}

	private int determineSchema(RoutingInfo routingInfo, HttpServletRequest request) {

		log.info("Algorithm");
		if (routingInfo == null) {
			log.info("RoutingInfo=null");
			return 1;
		}
		if (routingInfo.getUripattern().toLowerCase().equals("accountid")) {
			log.info("Algorithm=accountId");
			return schemaDeterminationByAccount(routingInfo, new Request(request));
		} else if (routingInfo.getUripattern().toLowerCase().equals("orderid")) {
			log.info("Algorithm=orderId");
			return schemaDeterminationByOrder(routingInfo, new Request(request));
		} else {
			log.info("Algorithm=???");
			return 1;
		}
	}

	private int schemaDeterminationByAccount(RoutingInfo routingInfo, Request req) {
		String accountId = stringService.getUserId(routingInfo.getBodyfield(), req.getBody());
		log.info("accountId:" + accountId);
		Account account = dataService.getByName(accountId);
		if (account != null && account.getZone().equals("2")) {
			log.info("Zone 2");
			return 2;
		} else {
			log.info("Default");
			return 1;
		}
	}

	private int schemaDeterminationByOrder(RoutingInfo routingInfo, Request req) {
		String orderId;
		if (routingInfo.getLocation().toLowerCase().equals("body")) {
			orderId = stringService.getOrderIdFromBody(routingInfo.getBodyfield(), req.getBody());
		} else {
			orderId = stringService.getOrderIdFromUri(routingInfo.getUri(), req.getUri());
		}
		if (orderId != null && orderId.length() != 0) {
			log.info("Found orderId: " + orderId);
			if (orderService.existsOrder(orderId)) {
				log.info("Order Exists: true");
				log.info("Zone 1");
				return 1;
			} else {
				log.info("Order Exists: false");
				log.info("Zone 2");
				return 2;
			}
		} else
			log.info("Didn't find orderId, default");
		return 1;
	}

}
