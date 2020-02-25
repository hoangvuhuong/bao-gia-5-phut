package adstech.vn.quotationbackoffice.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import adstech.vn.quotationbackoffice.contract.CustomUserDetail;
import adstech.vn.quotationbackoffice.model.Role;
import adstech.vn.quotationbackoffice.model.User;
import adstech.vn.quotationbackoffice.repository.RoleRepository;
import adstech.vn.quotationbackoffice.repository.UserRepository;

@Service
public class UserService implements UserDetailsService, IUserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.get(username);
		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		return new CustomUserDetail(user.getUserLogin(), user.getUserPass(),
				Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole())), user.getDisplayName(), user.getUserPhone());
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.getByEmail(email);
	}

	@Override
	public int createUser(User user) {
		return userRepository.addUser(user);
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}

	@Override
	public List<Role> getAllRole() {
		return roleRepository.getAllRole();
	}

	@Override
	public int assignRole(Long userId, Long roleId) {
		return userRepository.assignRole(userId, roleId);
	}

	@Override
	public int updateProfile(User user) {
		return userRepository.updateProfile(user);
	}
	
	@Override
	public List<User> getAllTechnician() {
		return userRepository.getAllTechnician();
	}

}
