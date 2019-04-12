package proxy.route.service;

import proxy.model.Account;
import proxy.model.RoutingInfo;
import proxy.repository.APIDataRepositoryImpl;
import proxy.route.model.Request;
import proxy.service.StringServiceImpl;
import com.netflix.zuul.context.RequestContext;
import org.h2.jdbc.JdbcSQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static proxy.route.service.APITask.*;

@Service
public class APIServiceImpl implements APIService {

	private static Logger log = LoggerFactory.getLogger(APIServiceImpl.class);

	@Autowired
	private APIDataRepositoryImpl dataService;

	@Autowired
	private StringServiceImpl stringService;

	@Override
	public void execute(RequestContext ctx) {
		try {
			HttpServletRequest request = ctx.getRequest();
			String uri = request.getRequestURI().toLowerCase();

			log.info(getTask(uri).toString());
			log.info("uri=" + uri);
			String body = (new Request(request)).getBody();

			ctx.setResponseBody(doTask(getTask(uri), body));
			ctx.setSendZuulResponse(false);
			ctx.getResponse().setHeader("Content-Type", "text/plain;charset=" + StandardCharsets.UTF_8.name());
			ctx.setResponseStatusCode(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception ", e);
		}
	}

	//getAlgorithms(){}
	private APITask getTask(String uri) {
		if (uri.toLowerCase().contains("accounts") && uri.contains("get")) return GET_ACCOUNTS;
		if (uri.toLowerCase().contains("accounts") && uri.contains("post")) return POST_ACCOUNTS;
		if (uri.toLowerCase().contains("account") && uri.contains("add")) return POST_ADD_ACCOUNT;
		if (uri.toLowerCase().contains("accounts") && uri.contains("add")) return POST_ADD_ACCOUNTS;
		if (uri.toLowerCase().contains("account") && uri.contains("update")) return POST_UPDATE_ACCOUNT;
		if (uri.toLowerCase().contains("account") && uri.contains("delete")) return POST_DELETE_ACCOUNT;
		if (uri.toLowerCase().contains("envs") && uri.contains("get")) return GET_ENVIRONMENTS;
		if (uri.toLowerCase().contains("envs") && uri.contains("post")) return POST_ENVIRONMENTS;
		if (uri.toLowerCase().contains("env") && uri.contains("add")) return POST_ADD_ENVIRONMENT;
		if (uri.toLowerCase().contains("envs") && uri.contains("add")) return POST_ADD_ENVIRONMENTS;
		if (uri.toLowerCase().contains("env") && uri.contains("update")) return POST_UPDATE_ENVIRONMENT;
		if (uri.toLowerCase().contains("env") && uri.contains("delete")) return POST_DELETE_ENVIRONMENT;
		return OTHER;
	}

	private String doTask(APITask task, String body) {
		switch (task) {
			case GET_ACCOUNTS:
			case POST_ACCOUNTS:
				return get_accounts();
			case POST_ADD_ACCOUNT:
				return add_account(body);
			case POST_UPDATE_ACCOUNT:
				return update_account(body);
			case POST_DELETE_ACCOUNT:
				return delete_account(body);
			case GET_ENVIRONMENTS:
				POST_ENVIRONMENTS:
				return get_environments();
			default:
				return "API ";
		}
	}

	private String get_accounts() {
		log.info("Get accounts");
		List<Account> list = dataService.getAccounts();
		JSONArray jsonArray = new JSONArray();
		list.forEach(x -> {
			JSONObject formDetailsJson1 = new JSONObject();
			formDetailsJson1.put("id", x.getAccountId());
			formDetailsJson1.put("zone", x.getZone());
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("account", formDetailsJson1);
			jsonArray.add(formDetailsJson);
		});
		return jsonArray.toJSONString();
	}

	private String get_environments() {
		log.info("Get envs");
		List<RoutingInfo> list = dataService.getRoutingInfo();
		JSONArray jsonArray = new JSONArray();
		list.forEach(x -> {
			JSONObject formDetailsJson1 = new JSONObject();
			formDetailsJson1.put("id", x.getEnvId());
			formDetailsJson1.put("uri", x.getUri());
			formDetailsJson1.put("bodyfield", x.getBodyfield());
			formDetailsJson1.put("location", x.getLocation());
			formDetailsJson1.put("type", x.getType());
			formDetailsJson1.put("uripattern", x.getUripattern());
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("env", formDetailsJson1);
			jsonArray.add(formDetailsJson);
		});

		return jsonArray.toJSONString();
	}

	private String add_account(String body) {
		Account account = stringService.getAccountFromBody(body);
		try {
			dataService.addAccount(account);
			return "Account added, id=" + account.getAccountId() + ", zone=" + account.getZone();
		} catch (Exception e) {
			e.printStackTrace();
			return "Couldn't add the account, id=" + account.getAccountId() + ", zone=" + account.getZone();
		}

	}

	private String update_account(String body) {

		Account account = stringService.getAccountFromBody(body);
		try {
			dataService.updateAccount(account);
			return "Account updated, id=" + account.getAccountId() + ", zone=" + account.getZone();
		} catch (Exception e) {
			e.printStackTrace();
			return "Couldn't update, id=" + account.getAccountId() + ", zone=" + account.getZone();
		}

	}

	private String delete_account(String body) {

		Account account = stringService.getAccountFromBody(body);
		try {
			dataService.deleteAccount(account);
			return "Account deleted, id=" + account.getAccountId();
		} catch (JdbcSQLException e) {
			e.printStackTrace();
			return "Couldn't delete, id=" + account.getAccountId();
		}

	}
}
