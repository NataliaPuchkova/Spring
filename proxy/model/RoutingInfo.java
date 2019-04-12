package proxy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routingInfo")
public class RoutingInfo {

	@Id
	@Column(name = "id", length = 500, nullable = false)
	private String envId = "";

	@Column(name = "uri", length = 500, nullable = false)
	private String uri = "";

	@Column(name = "location", length = 50, nullable = false)
	private String location = "";

	@Column(name = "type", length = 100)
	private String type = "";

	@Column(name = "bodyfield", length = 50)
	private String bodyfield = "";

	@Column(name = "uripattern", length = 100)
	private String uripattern = "";

	public String getEnvId() {
		return envId;
	}

	public RoutingInfo setId(String str) {
		envId = str;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public RoutingInfo setLocation(String str) {
		location = str;
		return this;
	}

	public String getType() {
		return type;
	}

	public RoutingInfo setType(String str) {
		type = str;
		return this;
	}

	public String getBodyfield() {
		return bodyfield;
	}

	public RoutingInfo setBodyfield(String str) {
		bodyfield = str;
		return this;
	}

	public String getUripattern() {
		return uripattern;
	}

	public RoutingInfo setUripattern(String str) {
		uripattern = str;
		return this;
	}

	public String getUri() {
		return uri;
	}

	public RoutingInfo setUri(String str) {
		uri = str;
		return this;
	}

	public String toString() {
		return String.format("RoutingInfo [id=%s, \nuri=%s,\nlocation=%s,\ntype=%s,\nbodyfield=%s,\nuripattern=%s ]",
				envId, uri, location, type, bodyfield, uripattern);
	}

}
