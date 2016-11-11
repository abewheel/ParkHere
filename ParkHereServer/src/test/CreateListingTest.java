package test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Address;
import model.CancellationPolicy;
import model.Category;
import model.Lender;
import model.Listing;
import model.ListingAvailibility;
import model.Profile;
import model.User;
import server.DBException;
import server.DatabaseConnector;

public class CreateListingTest {
	
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
	public void testCreateListingAndQueryForListing() throws DBException{
		
		try {
			Lender lender;
			lender = new Lender();
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("98765");
			lender.setUser_id(user.getUser_id());
			lender = dbConn.createLender(lender);
			
			Listing listing= new Listing();
			listing.setTitle("test listing");
			listing.setDescription("test listing desc");
			listing.setLenderId(lender.getLenderId());
			listing.setAddress(new Address());
			listing.getAddress().setCity("city test");
			listing.getAddress().setFirstLine("first line test");
			listing.getAddress().setSecondLine("second line test");
			listing.getAddress().setState("state test");
			listing.getAddress().setZipCode("zip code test");
			listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.COMPACT);
			listing.getCategories().add(Category.COVERED);
			ListingAvailibility la1 = new ListingAvailibility();
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+3500));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1200));
			ListingAvailibility la2 = new ListingAvailibility();
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+4900));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+300));
			ListingAvailibility la3 = new ListingAvailibility();
			la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+8900));
			la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+800));
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			listing.addAvailibility(la3);
			listing.setPrice_per_hr(6.70);
			
			dbConn.createListing(listing);
			
			Assert.assertNotEquals("got a valid id after listing creation", -1, listing.getListingId());
			
			lender.setListings(dbConn.getLenderListings(lender.getLenderId()));
			
			Assert.assertEquals("lender should have one listing: "+lender.getListings().size(), lender.getListings().size(), 1);
			
			Listing dbListing = null;
			for (Listing list : lender.getListings().values()) dbListing = list;

			Assert.assertTrue("correct cancellation policy", dbListing.getCancellationPolicy().equals(listing.getCancellationPolicy()));
			Assert.assertTrue("correct title", dbListing.getTitle().equals(listing.getTitle()));
			Assert.assertTrue("correct description", dbListing.getDescription().equals(listing.getDescription()));
			Assert.assertTrue("correct listing id", dbListing.getListingId() == listing.getListingId());
			Assert.assertTrue("correct lender id", dbListing.getLenderId() == listing.getLenderId());
			
			Assert.assertTrue("same size categories", dbListing.getCategories().size() == listing.getCategories().size());
			
			for (String cat : listing.getCategories()){
				Assert.assertTrue(dbListing.getCategories().contains(cat));
			}
			
			Assert.assertTrue("correct size of availabilities", dbListing.getAvailabilityList().size() == listing.getAvailabilityList().size());

			dbConn.removeListing(listing.getListingId());
			
			lender.setListings(dbConn.getLenderListings(lender.getLenderId()));
			Assert.assertTrue("empty listings", lender.getListings().isEmpty());
			
			dbConn.deleteLender(lender.getLenderId());
			
			dbConn.getLender(user.getUser_id(), user);
			Assert.assertTrue("lender has been deleted", user.getLender() == null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateListingNoAddress(){
		Lender lender;
		lender = new Lender();
		lender.setProfile(new Profile());
		lender.getProfile().setPhoneNumber("98765");
		lender.setUser_id(user.getUser_id());
		try {
			lender = dbConn.createLender(lender);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Listing listing= new Listing();
		listing.setTitle("test listing");
		listing.setDescription("test listing desc");
		listing.setLenderId(lender.getLenderId());
		listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
		listing.setAvailabilityList(new ArrayList<>());
		listing.setCategories(new ArrayList<>());
		listing.getCategories().add(Category.COMPACT);
		listing.getCategories().add(Category.COVERED);
		ListingAvailibility la1 = new ListingAvailibility();
		la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+3500));
		la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1200));
		ListingAvailibility la2 = new ListingAvailibility();
		la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+4900));
		la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+300));
		ListingAvailibility la3 = new ListingAvailibility();
		la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+8900));
		la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+800));
		listing.addAvailibility(la1);
		listing.addAvailibility(la2);
		listing.addAvailibility(la3);
		listing.setPrice_per_hr(6.70);
		
		try {
			dbConn.createListing(listing);
		} catch (SQLException | DBException dbe) {
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_LISTING));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_ADDRESS));
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
