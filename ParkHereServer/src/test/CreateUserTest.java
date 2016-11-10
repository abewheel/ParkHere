package test;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.User;

import org.junit.*;
import server.DatabaseConnector;

public class CreateUserTest {
	
	DatabaseConnector dbConn;
	Boolean isSetUp = false;
	Boolean tornDown = false;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
			dbConn = new DatabaseConnector();
			isSetUp = true;
		}
	}

	@After
	public void tearDown() throws Exception {
		if (!tornDown){
			dbConn.close();
			tornDown = false;
		}
	}
	
	@Test
	public void testLoginInvalidUsernameInvalidPassword(){
		try{
			Boolean successfulLogin = dbConn.checkUserPasssword("invalid_email", "invalid_pass");
			Assert.assertFalse("testing login with invalid username", successfulLogin);
		}
		catch (SQLException sqle){
			System.out.println("sqle: "+sqle.getMessage());
		}
	}
	
	@Test
	public void testCreateUserAndLoginInvalidPasswordValidUsername(){
		
		try{
			User user = new User();
			user.setEmail("test email");
			user.setPassword("test pass");
			user.setName("test name");
			User createdUser = dbConn.createUser(user);
			
			Assert.assertNotEquals("user has an id", -1, createdUser.getUser_id());
			
			Assert.assertFalse("user cannot login with invalid password", dbConn.checkUserPasssword(user.getEmail(), "invalid_password"));
			
			dbConn.deleteUser(user.getUser_id());
			
			Assert.assertFalse("user is deleted", dbConn.checkUserPasssword(user.getEmail(), user.getPassword()));
		}
		catch (SQLException sqle){
			System.out.println("sqle: "+sqle.getMessage());
		}
		
	}
	
	@Test
	public void testCreateUserAndLoginInvalidUsernameCorrectPassword(){
		
		try{
			User user = new User();
			user.setEmail("test email");
			user.setPassword("test pass");
			user.setName("test name");
			User createdUser = dbConn.createUser(user);
			
			Assert.assertNotEquals("user has an id", -1, createdUser.getUser_id());
			
			Assert.assertFalse("user cannot login with invalid password", dbConn.checkUserPasssword("invalid_email", user.getPassword()));
			
			dbConn.deleteUser(user.getUser_id());
			
			Assert.assertFalse("user is deleted", dbConn.checkUserPasssword(user.getEmail(), user.getPassword()));
		}
		catch (SQLException sqle){
			System.out.println("sqle: "+sqle.getMessage());
		}
		
	}
	
	
	@Test
	public void testCreateUserAndLoginValidUser(){
		try{
			User user = new User();
			user.setEmail("test email");
			user.setPassword("test pass");
			user.setName("test name");
			User createdUser = dbConn.createUser(user);
			
			Assert.assertNotEquals("user has an id", -1, createdUser.getUser_id());
			
			Assert.assertTrue("user cannot login with invalid password", dbConn.checkUserPasssword(user.getEmail(), user.getPassword()));
			
			dbConn.deleteUser(user.getUser_id());
			
			Assert.assertFalse("user is deleted", dbConn.checkUserPasssword(user.getEmail(), user.getPassword()));
		}
		catch (SQLException sqle){
			System.out.println("sqle: "+sqle.getMessage());
		}
	}

}
