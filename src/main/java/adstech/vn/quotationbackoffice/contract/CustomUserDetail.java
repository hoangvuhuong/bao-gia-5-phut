package adstech.vn.quotationbackoffice.contract;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetail extends User {
	private static final long serialVersionUID = 1L;
	
	private String displayName;
	private String userPhone;
	private String ggAccessToken;

	public CustomUserDetail(String username, String password, 
			Collection<? extends GrantedAuthority> authorities, String displayName, String userPhone) {
		super(username, password, authorities);
		this.setDisplayName(displayName);
		this.setUserPhone(userPhone);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getGgAccessToken() {
		return ggAccessToken;
	}

	public void setGgAccessToken(String ggAccessToken) {
		this.ggAccessToken = ggAccessToken;
	}

}
