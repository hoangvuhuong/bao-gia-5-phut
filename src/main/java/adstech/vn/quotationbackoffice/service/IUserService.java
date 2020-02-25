package adstech.vn.quotationbackoffice.service;

import java.util.List;

import adstech.vn.quotationbackoffice.model.Role;
import adstech.vn.quotationbackoffice.model.User;

public interface IUserService {

	public User getUserByEmail(String email);
	
	public int createUser(User user);
	
	public List<User> getAllUser();
	
	public List<Role> getAllRole();
	
	public int assignRole(Long userId, Long roleId);
	
	public int updateProfile(User user);
	
	public List<User> getAllTechnician();
	
}
