package com.rakesh.japi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.rakesh.japi.services.v1.User;

public class DataManager {
	
	private static DB japiDB;
	private static DBCollection userCollection;
	
	private static DataManager instance;
	
	Logger log = Logger.getLogger(DataManager.class.getName());
	
	public static DataManager getInstance() {
		if(instance == null) {
			instance = new DataManager();
		}
		return instance;
	}
	
	private DataManager() {
		
		try {
			
			MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 23456));
			
			japiDB = mongoClient.getDB("japi");
			
			userCollection = japiDB.getCollection("users");
			
		}catch (Exception e) {
			log.info("Database connection error: "  + e.getMessage());
		}
		
	}
	
	public User insertUser(User user) {
		
		log.info(this.getClass().getSimpleName() +  ": insertUser started");
		
		try {
			BasicDBObject doc = new BasicDBObject();
			
			doc.put("name", user.getName());
			
			userCollection.insert(doc);
			
			user.setId(doc.get("_id").toString());
			
		}catch (Exception e) {
			// TODO: handle exception
			log.info(e.getMessage());
		}

		return user;
		
		
	}
	
	private User mapUserFromDBObject(DBObject object) {
		
		User user = new User();
		user.setId(object.get("_id").toString());
		user.setName(object.get("name").toString());
		return user;
		
	}
	
	
	
	public User findUserById(String userIdString) {
		
		log.info(this.getClass().getSimpleName() +  ": userIdString started");
		
		if(userIdString == null) {
			log.info("findUserById : user id is null");
			return null;
		}
		
		try {
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userIdString));
			
			//DBObject searchById = new BasicDBObject("name", userIdString);
			
			DBObject userObject = userCollection.findOne(searchById);
			
			if(userObject == null) {
				return null;
			}
			
			User user = mapUserFromDBObject(userObject);
			return user;
			
		}catch(Exception e) {
			log.error(this.getClass().getSimpleName() + ": findUserById : Database error: " + e);
		}
		
		return null;
		
	}
	
	
	
	
	public List<User> findAllUsers() {
		
		log.info(this.getClass().getSimpleName() +  ": findAllUsers started");
		
		List<User> users = new ArrayList<User>();
		
		try {
			
			DBCursor cursor = userCollection.find();
			while(cursor.hasNext()) {
				
				BasicDBObject doc = (BasicDBObject) cursor.next();
				
				User item = mapUserFromDBObject(doc);
				
				users.add(item);			
			}
			
			return users;	
			
		}catch(Exception e) {
			log.error(this.getClass().getSimpleName() + ": findAllUsers : Database error: " + e);
		}
		
		return null;
		
	}
	
	
	
	public User updateUser(String userIdString, String attribute, String value) {
		
		log.info(this.getClass().getSimpleName() +  ": updateUser started");
		
		if(userIdString == null) {
			return null;
		}
		
		try {
			
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userIdString));
			
			DBObject userObject = userCollection.findOne(searchById);
			
			if(userObject == null) {
				return null;
			}
			
			userObject.put("name", value);
			
			
			if(attribute.equalsIgnoreCase("name")) {
				userCollection.update(searchById, userObject);
				User user = mapUserFromDBObject(userObject);
				return user;
			}else {
				return null;
			}
			
		}catch(Exception e) {
			log.error(this.getClass().getSimpleName() + ": updateUser : Database error: " + e);
		}
		return null;
	}
	
	
	
public User deleteUser(String userIdString) {
		
		log.info(this.getClass().getSimpleName() +  ": deleteUser started");
		
		if(userIdString == null) {
			return null;
		}
		
		try {
			
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userIdString));
			
			DBObject userObject = userCollection.findOne(searchById);
			
			if(userObject == null) {
				return null;
			}
			
			userCollection.findAndRemove(searchById);
			
			return mapUserFromDBObject(userObject);
			
		}catch(Exception e) {
			log.error(this.getClass().getSimpleName() + ": updateUser : Database error: " + e);
		}
		return null;
	}

}
