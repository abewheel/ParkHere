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

public class CreateReservationTest {
	DatabaseConnector dbConn;
	User user1;
	User user2;
	Boolean isSetUp = false;
	Boolean tornDown = false;
	Listing listing;
	Lender lender;
	Seeker seeker;
	ListingAvailibility la1;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
	
			dbConn = new DatabaseConnector();
			user1 = new User();
			user1.setEmail(""+UUID.randomUUID());
			user1.setName("test_name1");
			user1.setPassword("pass834759p");
			user1 = dbConn.createUser(user1);
			user2 = new User();
			user2.setEmail(""+UUID.randomUUID());
			user2.setName("test_name3");
			user2.setPassword("pass!!!!");
			user2 = dbConn.createUser(user2);
			
			seeker = new Seeker();
			lender = new Lender();
			listing = new Listing();
			la1 = new ListingAvailibility();
			ListingAvailibility la2 = new ListingAvailibility();
			
			seeker.setUser_id(user1.getUser_id());
			seeker.setProfile(new Profile());
			seeker.getProfile().setPhoneNumber("908765");
			seeker = dbConn.createSeeker(seeker);
			
			lender.setUser_id(user2.getUser_id());
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("567890");
			lender = dbConn.createLender(lender);
			
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+34890));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+8765));
			
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+87657));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+987654));
			
			Address address = new Address();
			address.setCity("city");
			address.setFirstLine("asdjhfk");
			address.setSecondLine("ihbjgb");
			address.setState("AZ");
			address.setZipCode("98765");
			
			listing.setAddress(address);
			listing.setCancellationPolicy(CancellationPolicy.NO_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			
			listing.setTitle("testing create res title");
			listing.setDescription("testing create res desc");
			listing.setPrice_per_hr(5.50);
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.HANDICAPPED);
			listing.getCategories().add(Category.COVERED);
			
			listing.setLenderId(lender.getLenderId());
	//		listing.se
			dbConn.createListing(listing);
			
			isSetUp = true;
		}
		
	}
	
	@Test
	public void testCreateReservationMissingListing(){
		Reservation reservation = new Reservation();
		reservation.setLenderId(lender.getLenderId());
		//reservation.setListingId(listing.getListingId());
		reservation.setListingAvailibility(la1);
		reservation.setSeekerId(seeker.getSeekerId());
		//reservation.setPricePerHour((int) 5.50);
		try{
			dbConn.createReservation(reservation);
			Assert.assertTrue("should have thrown exception", false);
		}
		catch (DBException dbe){
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_RESERVATION));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_LISTING_ID));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateReservationMissingLender(){
		Reservation reservation = new Reservation();
		//reservation.setLenderId(lender.getLenderId());
		reservation.setListingId(listing.getListingId());
		reservation.setListingAvailibility(la1);
		reservation.setSeekerId(seeker.getSeekerId());
		//reservation.setPricePerHour((int) 5.50);
		try{
			dbConn.createReservation(reservation);
			Assert.assertTrue("should have thrown exception", false);
		}
		catch (DBException dbe){
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_RESERVATION));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_LENDER_ID));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateReservationMissingAvailability(){
		Reservation reservation = new Reservation();
		reservation.setLenderId(lender.getLenderId());
		reservation.setListingId(listing.getListingId());
		//reservation.setListingAvailibility(la1);
		reservation.setSeekerId(seeker.getSeekerId());
		//reservation.setPricePerHour((int) 5.50);
		try{
			dbConn.createReservation(reservation);
			Assert.assertTrue("should have thrown exception", false);
		}
		catch (DBException dbe){
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_RESERVATION));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_AVAILABILITY));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateReservationMissingSeeker(){
		Reservation reservation = new Reservation();
		reservation.setLenderId(lender.getLenderId());
		reservation.setListingId(listing.getListingId());
		reservation.setListingAvailibility(la1);
		//reservation.setSeekerId(seeker.getSeekerId());
		//reservation.setPricePerHour((int) 5.50);
		try{
			dbConn.createReservation(reservation);
			Assert.assertTrue("should have thrown exception", false);
		}
		catch (DBException dbe){
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.CREATE_RESERVATION));
			Assert.assertTrue("exception message is expected", dbe.getMessage().contains(DBException.INVALID_SEEKER_ID));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testCreateReservation(){
		
		try {
			
			
			Reservation reservation = new Reservation();
			reservation.setLenderId(lender.getLenderId());
			reservation.setListingId(listing.getListingId());
			reservation.setListingAvailibility(la1);
			reservation.setSeekerId(seeker.getSeekerId());
			//reservation.setPricePerHour((int) 5.50);
	
			dbConn.createReservation(reservation);
			
			Assert.assertNotEquals("reservation has an id", reservation.getReservationId(), -1);
			
			Map<Long, Reservation> lenderMap = dbConn.getReservations(lender.getLenderId(), true);
			Assert.assertTrue("reservations map for lender is correct size", lenderMap.size()==1);
			
			Map<Long, Reservation> seekerMap = dbConn.getReservations(seeker.getSeekerId(), false);
			Assert.assertTrue("reservations map for seeker is correct size", seekerMap.size()==1);
			
			Reservation lenderReservation = null;
			Reservation seekerReservation = null;
			
			for (Reservation res : lenderMap.values()){
				lenderReservation = res;
			}
			
			for (Reservation res : seekerMap.values()){
				seekerReservation = res;
			}
			
			//Assert.assertTrue("lender and seeker have same reservation", lenderReservation.equals(seekerReservation));
			Assert.assertEquals("lender and seeker have same reservation id", lenderReservation.getReservationId(), seekerReservation.getReservationId());
			Assert.assertEquals("lender and seeker have same listing id", lenderReservation.getListingId(), seekerReservation.getListingId());
			Assert.assertEquals("lender and seeker have same lender id", lenderReservation.getLenderId(), seekerReservation.getLenderId());
			Assert.assertEquals("lender and seeker ahve same seeker id", lenderReservation.getSeekerId(), seekerReservation.getSeekerId());
			Assert.assertEquals("lender and seeker have same availability id", lenderReservation.getListingAvailibility().getAvailabilityId(), 
					seekerReservation.getListingAvailibility().getAvailabilityId());
			Assert.assertTrue("availability is marked as reserved in lender", lenderReservation.getListingAvailibility().getIsReserved());
			Assert.assertTrue("availability is marked as reserved in seeker", seekerReservation.getListingAvailibility().getIsReserved());
			//Assert.assertTrue("reservation);
			
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
			dbConn.deleteUser(user2.getUser_id());
			dbConn.close();
			tornDown = true;
		}
		
		
	}

}
