package test;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Profile;
import model.Seeker;
import model.User;
import server.DatabaseConnector;

public class CreateSeekerTest {


	DatabaseConnector dbConn;
	User user;
	Boolean isSetUp = false;
	Boolean tornDown = false;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
			dbConn = new DatabaseConnector();
			user = new User();
			user.setEmail("test_email1");
			user.setName("test_name1");
			user.setPassword("test_password1");
			user = dbConn.createUser(user);
			isSetUp = true;
		}
		
	}
	
	@Test
	public void testCreateSeekerAndQueryForSeeker(){
		
		try {
			Seeker seeker= new Seeker();
			seeker.setUser_id(user.getUser_id());
			seeker.setProfile(new Profile());
			seeker.getProfile().setPhoneNumber("5305305533");
			seeker = dbConn.createSeeker(seeker);
			//lender.setLenderId(newLender.getLenderId());
			
			Assert.assertNotEquals("seeker id is not null", -1, seeker.getSeekerId());
		//	System.out.println("user id : "+user.getUser_id());
			dbConn.getSeeker(user.getUser_id(), user);
			
			//lender.setListings(new HashMap<>());
		//	lender.setReservations(new HashMap<>());
		//	System.out.println("original lender");
			//System.out.println(lender.toString());
			//System.out.println("db lender");
			//System.out.println(user.getLender().toString());
			Assert.assertTrue("seeker ids match", seeker.getSeekerId() == user.getSeeker().getSeekerId());
			Assert.assertTrue("user ids match", seeker.getUser_id() == user.getSeeker().getUser_id());
			Assert.assertTrue("phone nums match", seeker.getProfile().getPhoneNumber().equals(user.getSeeker().getProfile().getPhoneNumber()));
			Assert.assertTrue("favorites match", seeker.getFavorites().equals(user.getSeeker().getFavorites()));
			Assert.assertTrue("reservations match", seeker.getReservations().equals(user.getSeeker().getReservations()));
			
			dbConn.deleteSeeker(seeker.getSeekerId());
			
			dbConn.getSeeker(user.getUser_id(), user);
			Assert.assertTrue("lender has been deleted", user.getSeeker() == null);
			
		} catch (SQLException e) {
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
