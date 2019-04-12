package route.model;

public class FunctionalZone {
	private String uat1Url;
	private String uat2Url;

	public FunctionalZone(String uat1Url, String uat2Url) {
		this.uat1Url = uat1Url;
		this.uat2Url = uat2Url;
	}

	public String getUat1Url() {
		return uat1Url;
	}

	public String getUat2Url() {
		return uat2Url;
	}

}
