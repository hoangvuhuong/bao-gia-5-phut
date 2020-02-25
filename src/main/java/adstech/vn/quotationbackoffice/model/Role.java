package adstech.vn.quotationbackoffice.model;

import java.io.Serializable;

public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long roleId;
	private String roleName;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
