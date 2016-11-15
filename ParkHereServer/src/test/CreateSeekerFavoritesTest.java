package test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
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
import model.Reservation;
import model.Seeker;
import model.User;
import server.DBException;
import server.DatabaseConnector;

public class CreateSeekerFavoritesTest {
	
	DatabaseConnector dbConn;
	User user1;
	Boolean isSetUp = false;
	Boolean tornDown = false;
	Listing listing;
	Lender lender;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
	
			dbConn = new DatabaseConnector();
			user1 = new User();
			user1.setEmail(""+UUID.randomUUID());
			user1.setLastName("test_name2541");
			user1.setFirstName("test_name2541");
			user1.setPassword("pass8348764543759p");
			System.out.println("before creating user");
			user1 = dbConn.createUser(user1);
			System.out.println("after crating user");
			
			isSetUp = true;
		}
		
	}
	
	@Test
	public void testCreateSeekerFavorite(){
		
		try {
			System.out.println("getting into test");
			lender = new Lender();
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("987wert65");
			lender.setUser_id(user1.getUser_id());
			lender = dbConn.createLender(lender);
			
			listing= new Listing();
			listing.setTitle("test l3453isting");
			listing.setDescription("test lisytwqting desc");
			listing.setLenderId(lender.getLenderId());
			listing.setAddress(new Address());
			listing.getAddress().setCity("city test564");
			listing.getAddress().setFirstLine("first line te765423st");
			listing.getAddress().setSecondLine("second li6543ne test");
			listing.getAddress().setState("state 46534test");
			listing.getAddress().setZipCode("zip cod6543e test");
			listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.COMPACT);
			listing.getCategories().add(Category.SUV);
			ListingAvailibility la1 = new ListingAvailibility();
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+35500));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1226500));
			ListingAvailibility la2 = new ListingAvailibility();
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+49245600));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+30520));
			ListingAvailibility la3 = new ListingAvailibility();
			la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+89243500));
			la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+80234530));
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			listing.addAvailibility(la3);
			listing.setPrice_per_hr(6.70);
			
			dbConn.createListing(listing);
			
			Seeker seeker = new Seeker();
			seeker.setUser_id(user1.getUser_id());
			seeker.setProfile(new Profile());
			seeker.getProfile().setPhoneNumber("987654d3456");
			dbConn.createSeeker(seeker);
			
			dbConn.createSeekerFavorite(seeker.getSeekerId(), listing.getListingId());
			System.out.println("after creating seeker favorite");
			dbConn.getSeeker(user1.getUser_id(), user1);
			System.out.println("after get seeker from user");
			Seeker dbSeeker = user1.getSeeker();
			
			Map<Long, Listing> favs = dbSeeker.getFavorites();
			
			Assert.assertTrue("list of favorites is correct size of 1", favs.size() == 1);
			Listing favListing = null;
			for (Listing list : favs.values()){
				favListing = list;
			}
			
			Assert.assertTrue("listing has correct id", favListing.getListingId() == listing.getListingId());
			Assert.assertTrue("listing has correct description", favListing.getDescription().equals(listing.getDescription()));
			Assert.assertTrue("listing has correct title", favListing.getTitle().equals(listing.getTitle()));
			
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
			dbConn.deleteUser(user1.getUser_id());
			dbConn.close();
			tornDown = true;
		}
		
		
	}


}
