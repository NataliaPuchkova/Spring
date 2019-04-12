package proxy.service;

import proxy.model.Account;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StringServiceImpl implements StringService {

	private static Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

	/**
	 * @param uri      from RoutingInfo table,  local database
	 * @param localUri from equest
	 * @return OrderID
	 */
	public String getOrderIdFromUri(String uri, String localUri) {
		if (uri == null || localUri == null || localUri.length() == 0) return "";
		int i = uri.length();
		boolean j = localUri.toLowerCase().startsWith(uri.toLowerCase());
		log.info("RoutingInfo uri=" + uri);
		log.info("Request uri=" + localUri);
		if (j) {
			int k = localUri.toLowerCase().indexOf("/", i);
			if (k == -1) k = localUri.length();
			log.info("substring from " + i + " to " + k);
			log.info(localUri.substring(i, k));
			return localUri.substring(i, k);
		}
		return "";
	}

	/**
	 * @param orderId Name of Key in json file for getting OrderID
	 * @param body    Body of request
	 * @return OrderID
	 */
	public String getOrderIdFromBody(String orderId, String body) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(body.toLowerCase());
			log.info("body=" + body.toLowerCase());
			log.info("order=" + orderId.toLowerCase());
			return jsonObject.get(orderId.toLowerCase()).toString();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param accountId Name of Key in json file for getting AccountID
	 * @param body      Bosy from Request
	 * @return AccountID
	 */
	public String getUserId(String accountId, String body) {

		JSONParser parser = new JSONParser();
		try {
			//DataServiceImpl ps = new DataServiceImpl();

			JSONObject jsonObject = (JSONObject) parser.parse(body);
			return jsonObject.get(accountId).toString();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param str URI we need to clean
	 * @return cleaned URI
	 */
	public String cleanUri(String str) {
		if (str == null || str.length() == 0) return str;
		int index = str.indexOf("?");
		if (index > 0) return str.substring(0, index).replaceAll("/*$", "").replaceAll("\\d*$", "");
		return str.replaceAll("/*$", "").replaceAll("\\d*$", "");
	}

	public Account getAccountFromBody(String body) {
		JSONParser parser = new JSONParser();
		Account acc = new Account();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(body.toLowerCase());
			log.info("body=" + body.toLowerCase());
			log.info("jsonObject.get(\"id\").toString()=" + jsonObject.get("id").toString());
			log.info("jsonObject.get(\"zone\").toString()=" + jsonObject.get("zone").toString());
			acc.setAccountId(jsonObject.get("id").toString());
			acc.setZone(jsonObject.get("zone").toString());
			return acc;

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return acc;
		}
	}
}
