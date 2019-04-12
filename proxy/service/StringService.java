package proxy.service;

public interface StringService {
	String getOrderIdFromUri(String uri, String localUri);

	String getOrderIdFromBody(String orderId, String body);

	String getUserId(String accountId, String body);

	String cleanUri(String str);
}
