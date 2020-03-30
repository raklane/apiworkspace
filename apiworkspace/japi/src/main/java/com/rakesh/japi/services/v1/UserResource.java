package com.rakesh.japi.services.v1;

import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.omg.CORBA.MARSHAL;

import com.rakesh.japi.BusinessManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v1/users")
@Api(value = "/users", description = "Manage Users")

public class UserResource {

	public static final Logger log = Logger.getLogger(UserResource.class.getName());

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find User", notes = "This API returns public information of the user (Private info is returned if this is an auth user)."
			+ "<p><u>Input parameters</u> <ul><li><b>userId</b> is required</li></ul></p>")

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success : {User profile}"),
			@ApiResponse(code = 400, message = "Failed: {\"error\" : \"Error Description\", \"status\":\"fail\"}") })

	public Response getUserById(
			@ApiParam(value = "userId", required = true, defaultValue = "23456", allowableValues = "", allowMultiple = false) @PathParam("userId") String userId) {

		log.info("UserResource :: getUserById started, userId=" + userId);

		if (userId == null) {
			System.out.println("Null user id");
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\" : \"Empty User Id\",\"status\":\"fail\"}").build();
		}

		try {
			User user = BusinessManager.getInstance().findUser(userId);
			return Response.status(Response.Status.OK).entity(user).build();
		} catch (Exception e) {

		}

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\" : \"Could not find user\",\"status\":\"FAIL\"}").build();

	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find all users", notes = "This API retrieves all users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success : {users: []}"),
			@ApiResponse(code = 400, message = "Failed : {\"error\":\"error description\",\"status\":\"FAIL\"}") })

	public Response getAllUsers() {

		try {

			log.info("UserResource :: getAllUsers started");

			UserHolder usersHolder = new UserHolder();
			List<User> users = BusinessManager.getInstance().findAllUsers();

			usersHolder.setUsers(users);

			return Response.status(Response.Status.OK).entity(usersHolder).build();

		} catch (Exception e) {

		}

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\":\"Could not find user\",\"status\":\"FAIL\"}").build();

	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Create a new user", notes = "This API create a new user if it does not exist"
			+ "<p><u>Input parameters</u> <ul><li><b>new user object</b> is required</li></ul></p>")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Created: {New user}"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\",\"status\":\"FAIL\"}") })

	public Response createNewUser(
			@ApiParam(value = "New user", required = true, defaultValue = "{\"name\":\"Rakesh Kumar\"}", allowMultiple = false, allowableValues = "") User user) {

		log.info("UserResource :: createNewUser started");

		User userObj = BusinessManager.getInstance().addUser(user);

		try {
			return Response.status(Response.Status.CREATED).entity(userObj).build();
		} catch (Exception e) {

		}

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\":\"user could not be created\", \"status\":\"FAIL\"}").build();

	}

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Update name of an user", notes = "This API updates the name of an user"
			+ "<p><u>Input parameters</u> <ul><li><b>userId</b> is required</li><li><b>nameInJsonFormat</b> is required</li></ul></p>")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success: {Updated user}"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\",\"status\":\"FAIL\"}") })

	public Response updateUser(
			@PathParam("userId") String userId,
			@ApiParam(value = "jsonString", required = true, defaultValue = "{\"name\":\"Rakesh Kumar\"}", allowableValues = "", allowMultiple = false) String jsonString) {
		
		log.info("UserResource :: updateUser started");
		
		String name;
		
		try {
			Object obj = JSONValue.parse(jsonString);
			JSONObject jsonObject = (JSONObject) obj;
			if(!jsonObject.containsKey("name")) {
				throw new RuntimeException();
			}
			name = (String) jsonObject.get("name");
		}catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"Invalid or missing field error\",\"Status\":\"FAIL\"}").build();
		}
		
		try {
			User updatedUser = BusinessManager.getInstance().updateUser(userId, "name", name);
			return Response.status(Response.Status.OK).entity(updatedUser).build();
		}catch (Exception e) {
			
		}
		
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\":\"Could not update user\",\"Status\":\"FAIL\"}").build();
	}
	
	
	
	
	@DELETE
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Deletes an user", notes = "This API delets the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success: {}"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\",\"status\":\"FAIL\"}") })
	
	public Response deleteUser(@PathParam("userId") String userID) {
		
		log.info("UserResource :: deleteUser started");
		
		try {
			BusinessManager.getInstance().deleteUser(userID);
			return Response.status(Response.Status.OK).entity("{}").build();
		}catch (Exception e) {
			
		}
		
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\":\"Could not delete user\",\"Status\":\"FAIL\"}").build();
		
	}
	

}
