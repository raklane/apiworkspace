package com.rakesh.japi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rakesh.japi.services.v1.User;

public class BusinessManager {
	
	private static final Logger log = Logger.getLogger(BusinessManager.class.getName());
	
	private static BusinessManager businessManager = null;
	
	private BusinessManager() {
		
	}
	
	public static BusinessManager getInstance() {
		if(businessManager == null) {
			businessManager = new BusinessManager();
		}
		return businessManager;
	}
	
	public User findUser(String userId) throws Exception {
		
		log.info("BusinessManager:: findUser started");
		
		User user = DataManager.getInstance().findUserById(userId);
		
		if(user == null) {
			throw new Exception("User not found");
		}
		
		return user;
	}
	
	public List<User> findAllUsers(){
		
		log.info("BusinessManager:: findAllUsers started");
		
		List<User> users = DataManager.getInstance().findAllUsers();
		
		return users;
		
	}
	
	
	public User addUser(User user) {
		
		log.info("BusinessManager:: addUser started");
		
		User newUser = DataManager.getInstance().insertUser(user);

		return newUser;
		
	}
	
	
	public User updateUser(String userId, String attribute, String attributeValue) throws Exception {
		
		log.info("BusinessManager:: updateUser started");
		
		User user = DataManager.getInstance().updateUser(userId, attribute, attributeValue);
		
		if(user == null) {
			throw new Exception("User not found.");
		}
		
		return user;
		
	}
	
	
	public void deleteUser(String userId) throws Exception {
		
		log.info("BusinessManager:: deleteUser started");
		
		User user = DataManager.getInstance().deleteUser(userId);
		
		if(user == null) {
			throw new Exception("User not found.");
		}
		
		return;
		
	}

}
