package route.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {

	private static Logger log = LoggerFactory.getLogger(Request.class);

	private String schema;
	private Map<String, String> parameters;
	private String uri;
	private HttpSession session;
	private String query;
	private Cookie[] cookies;
	private Enumeration<String> headerNames;
	private HashMap<String, String> headers;
	private String method;
	private String contentType;
	private String body;
	private byte[] bodyByte;

	public Request(HttpServletRequest request) {
		method = request.getMethod();
		parameters = new HashMap<String, String>();
		String schema = request.getScheme();
		uri = request.getRequestURI();
		session = request.getSession();
		query = request.getQueryString();
		cookies = request.getCookies();
		headerNames = request.getHeaderNames();
		headers = new HashMap<String, String>();
		Enumeration<String> parameterNames = request.getParameterNames();

		try {
			body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			bodyByte = body.getBytes();

		} catch (Exception e) {
			body = "";
		}

		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = request.getParameter(key);
			parameters.put(key, value);
		}

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		contentType = request.getContentType();
	}

	public String getSchema() {
		return this.schema;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public String getUri() {
		return this.uri;
	}

	public HttpSession getSession() {
		return this.session;
	}

	public String getQuery() {
		return this.query;
	}

	public Cookie[] getCookies() {
		return this.cookies;
	}

	public Enumeration<String> getHeaderNames() {
		return this.headerNames;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public String getMethod() {
		return this.method;
	}

	public String getContentType() {
		return this.contentType;
	}

	public String getBody() {
		return this.body;
	}

	public byte[] getBodyByte() {
		return this.bodyByte;
	}

	public String toString() {
		return "Request:\n" +
				"schema=" + schema + "\n" +
				"parameters=" + parameters.toString() + "\n" +
				"uri=" + uri + "\n" +
				"session=" + session + "\n" +
				"query=" + query + "\n" +
				"cookies=" + cookies + "\n" +
				"headerNames=" + headerNames + "\n" +
				"headers=" + headers.toString() + "\n" +
				"method=" + method + "\n" +
				"contentType=" + contentType + "\n" +
				"body=" + body;
	}
}
