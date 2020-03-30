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
	
	public User findUser(String userId) {
		
		log.info("BusinessManager:: findUser started");
		
		User user = new User();
		user.setId("12345");
		user.setName("Rakesh");
		return user;
	}
	
	public List<User> findAllUsers(){
		
		log.info("BusinessManager:: findAllUsers started");
		
		List<User> users = new ArrayList<User>();
		
		User user1 = new User();
		user1.setId("12345");
		user1.setName("Rakesh Kumar");
		
		User user2 = new User();
		user2.setId("12335");
		user2.setName("Sudesh Deshpande");
		
		users.add(user1);
		users.add(user2);
		
		return users;
		
	}
	
	
	public User addUser(User user) {
		
		log.info("BusinessManager:: addUser started");
		
		user.setId("12357");
		return user;
		
	}
	
	
	public User updateUser(String userId, String attribute, String attributeValue) {
		
		log.info("BusinessManager:: updateUser started");
		
		User user = new User();
		
		user.setId(userId);
		
		if(attribute.equalsIgnoreCase("name")) {
			user.setName(attributeValue);
		}
		
		return user;
		
	}
	
	
	public void deleteUser(String userId) {
		
		log.info("BusinessManager:: deleteUser started");
		
		return;
		
	}

}
