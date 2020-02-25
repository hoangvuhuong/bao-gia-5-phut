package adstech.vn.quotationbackoffice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adstech.vn.quotationbackoffice.contract.CustomUserDetail;
import adstech.vn.quotationbackoffice.model.User;
import adstech.vn.quotationbackoffice.service.IUserService;

@RestController
public class UserController {
	@Autowired
	IUserService userService;
	
	@PutMapping("/user/{userId}")
	public ResponseEntity<?> assignRole(@PathVariable Long userId, @RequestParam Long roleId){
		try {
			return new ResponseEntity<Integer>(userService.assignRole(userId, roleId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/user/technicians")
	public ResponseEntity<?> getAllTechnicians(){
		try {
			return new ResponseEntity<List<User>>(userService.getAllTechnician(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/user/profile")
	public ResponseEntity<?> updateProfile(@RequestBody User user){
		try {
			int result = userService.updateProfile(user);
			UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetail userDetail = (CustomUserDetail)authentication.getPrincipal();
			userDetail.setDisplayName(user.getDisplayName());
			userDetail.setUserPhone(user.getUserPhone());
			authentication.setDetails(userDetail);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
