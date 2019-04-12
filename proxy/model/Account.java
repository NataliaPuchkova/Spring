package proxy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@Column(name = "id", nullable = false)
	private String accountId = "";

	@Column(name = "urlzone", length = 64, nullable = false)
	private String url = "";

	public String getAccountId() {
		return accountId;
	}

	public Account setAccountId(String str) {
		accountId = str;
		return this;
	}

	public String getZone() {
		return url;
	}

	public Account setZone(String str) {
		url = str;
		return this;
	}

	public String toString() {
		return String.format("Account [id=%s, zone=%s]", accountId, url);
	}

}
