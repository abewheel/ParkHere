package test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Lender;
import model.Profile;
import model.User;
import server.DBException;
import server.DatabaseConnector;

public class CreateLenderTest {
	
	DatabaseConnector dbConn;
	User user;
	Boolean isSetUp = false;
	Boolean tornDown = false;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
			dbConn = new DatabaseConnector();
			user = new User();
			user.setEmail(""+UUID.randomUUID());
			user.setName("test_name1");
			user.setPassword("test_password1");
			user = dbConn.createUser(user);
			isSetUp = true;
		}
		
	}
	
	@Test
	public void testCreateLenderNoUserId(){
		try {
			Lender lender = new Lender();
			//lender.setUser_id(user.getUser_id());
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("5305305533");
			lender = dbConn.createLender(lender);
		}
		catch (DBException dbe){
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_LENDER));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_USER_ID));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateLenderAndQueryForLender(){
		
		
		try {
			Lender lender = new Lender();
			lender.setUser_id(user.getUser_id());
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("5305305533");
			lender = dbConn.createLender(lender);
			//lender.setLenderId(newLender.getLenderId());
			
			Assert.assertNotEquals("lender id is not null", -1, lender.getLenderId());
			
			dbConn.getLender(user.getUser_id(), user);
			
			
			Assert.assertTrue("lender ids match", lender.getLenderId() == user.getLender().getLenderId());
			Assert.assertTrue("user ids match", lender.getUser_id() == user.getLender().getUser_id());
			Assert.assertTrue("phone nums match", lender.getProfile().getPhoneNumber().equals(user.getLender().getProfile().getPhoneNumber()));
			Assert.assertTrue("listings match", lender.getListings().equals(user.getLender().getListings()));
			Assert.assertTrue("reservations match", lender.getReservations().equals(user.getLender().getReservations()));
			
			dbConn.deleteLender(lender.getLenderId());
			
			dbConn.getLender(user.getUser_id(), user);
			Assert.assertTrue("lender has been deleted", user.getLender() == null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@After
	public void tearDown() throws Exception {
		if (!tornDown){
			dbConn.deleteUser(user.getUser_id());
			dbConn.close();
			tornDown = true;
		}
		
		
	}

}
