package test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.*;
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
import server.DatabaseConnector;

public class SearchListingsTest {
	
	DatabaseConnector dbConn;
	User user;
	Lender lender;
	Boolean isSetUp = false;
	Boolean tornDown = false;

	@Before
	public void setUp() throws Exception {
		if (!isSetUp){
			dbConn = new DatabaseConnector();
			user = new User();
			user.setEmail("test_email2");
			user.setName("test_name2");
			user.setPassword("test_password2");
			user = dbConn.createUser(user);
			isSetUp = true;
			lender = new Lender();
			lender.setProfile(new Profile());
			lender.getProfile().setPhoneNumber("98765");
			lender.setUser_id(user.getUser_id());
			lender = dbConn.createLender(lender);
		}
		
	}
	
	@Test
	public void createManyListingsAndSearchByZipCode(){
		
		List<Long> davisListings = new ArrayList<>();
		List<Long> laListings = new ArrayList<>();
		
		for (int i =1; i<11; i++){
			
			Listing listing= new Listing();
			listing.setTitle("test listing"+i);
			listing.setDescription("test listing desc"+i);
			listing.setLenderId(lender.getLenderId());
			listing.setAddress(new Address());
			listing.getAddress().setCity("city test"+i);
			listing.getAddress().setFirstLine("first line test"+i);
			listing.getAddress().setSecondLine("second line test"+i);
			listing.getAddress().setState("state test"+i);
			listing.getAddress().setZipCode(i<6 ? "22222" : "11111");
			listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.COMPACT);
			listing.getCategories().add(Category.COVERED);
			ListingAvailibility la1 = new ListingAvailibility();
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+3500*i));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1200*i));
			ListingAvailibility la2 = new ListingAvailibility();
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+4900*i));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+300*i));
			ListingAvailibility la3 = new ListingAvailibility();
			la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+8900));
			la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+800));
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			listing.addAvailibility(la3);
			listing.setPrice_per_hr(i*2);
			
			try {
				dbConn.createListing(listing);
				if (i<6){
					davisListings.add(listing.getListingId());
				}
				else{
					laListings.add(listing.getListingId());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		try {
			Map<Long, Listing> listingsDavis = dbConn.search("22222");
			Map<Long, Listing> listingsLA = dbConn.search("11111");
			System.out.println("listing davis size: "+listingsDavis.size());
			System.out.println("listing la size: "+listingsLA.size());
			Assert.assertEquals("davis listings size correct: "+listingsDavis.size(), listingsDavis.size(), davisListings.size());
			Assert.assertEquals("la listings size correct: "+listingsLA.size(), listingsLA.size(), laListings.size());
			
			for (Long id : listingsDavis.keySet()){
				Assert.assertTrue("davis db listings contain correct zip", davisListings.contains(id));
			}
			
			for (Long id : listingsLA.keySet()){
				Assert.assertTrue("la db listings contain correct zip", laListings.contains(id));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				for (Long id : davisListings){
					dbConn.removeListing(id);
				}
				
				for (Long id : laListings){
					dbConn.removeListing(id);
				}
			}
			catch (Exception e){
				System.out.println("exception");
			}
			
		}
		
	}
	
	@Test
	public void createManyListingsAndSearchByCategories(){
		
		List<Long> davisListings = new ArrayList<>();
		//List<Long> laListings = new ArrayList<>();
		
		for (int i =1; i<4; i++){
			
			Listing listing= new Listing();
			listing.setTitle("test listing"+i);
			listing.setDescription("test listing desc"+i);
			listing.setLenderId(lender.getLenderId());
			listing.setAddress(new Address());
			listing.getAddress().setCity("city test"+i);
			listing.getAddress().setFirstLine("first line test"+i);
			listing.getAddress().setSecondLine("second line test"+i);
			listing.getAddress().setState("state test"+i);
			listing.getAddress().setZipCode(i<6 ? "22222" : "11111");
			listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.COMPACT);
			listing.getCategories().add(Category.COVERED);
			ListingAvailibility la1 = new ListingAvailibility();
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+3500*i));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1200*i));
			ListingAvailibility la2 = new ListingAvailibility();
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+4900*i));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+300*i));
			ListingAvailibility la3 = new ListingAvailibility();
			la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+8900));
			la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+800));
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			listing.addAvailibility(la3);
			listing.setPrice_per_hr(i*2);
			
			try {
				dbConn.createListing(listing);
				//if (i<6){
					davisListings.add(listing.getListingId());
			//	}
				//else{
					//laListings.add(listing.getListingId());
				//}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		for (int i =1; i<4; i++){
			
			Listing listing= new Listing();
			listing.setTitle("test listing"+i);
			listing.setDescription("test listing desc"+i);
			listing.setLenderId(lender.getLenderId());
			listing.setAddress(new Address());
			listing.getAddress().setCity("city test"+i);
			listing.getAddress().setFirstLine("first line test"+i);
			listing.getAddress().setSecondLine("second line test"+i);
			listing.getAddress().setState("state test"+i);
			listing.getAddress().setZipCode(i<6 ? "22222" : "11111");
			listing.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
			listing.setAvailabilityList(new ArrayList<>());
			listing.setCategories(new ArrayList<>());
			listing.getCategories().add(Category.SUV);
			listing.getCategories().add(Category.HANDICAPPED);
			ListingAvailibility la1 = new ListingAvailibility();
			la1.setBeginDateTime(new Timestamp(System.currentTimeMillis()+3500*i));
			la1.setEndDateTime(new Timestamp(System.currentTimeMillis()+1200*i));
			ListingAvailibility la2 = new ListingAvailibility();
			la2.setBeginDateTime(new Timestamp(System.currentTimeMillis()+4900*i));
			la2.setEndDateTime(new Timestamp(System.currentTimeMillis()+300*i));
			ListingAvailibility la3 = new ListingAvailibility();
			la3.setBeginDateTime(new Timestamp(System.currentTimeMillis()+8900));
			la3.setEndDateTime(new Timestamp(System.currentTimeMillis()+800));
			listing.addAvailibility(la1);
			listing.addAvailibility(la2);
			listing.addAvailibility(la3);
			listing.setPrice_per_hr(i*2);
			
			try {
				dbConn.createListing(listing);
				//if (i<6){
					davisListings.add(listing.getListingId());
			//	}
				//else{
					//laListings.add(listing.getListingId());
				//}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		try {
			List<String> firstCats = new ArrayList<>();
			List<String> secondCats = new ArrayList<>();
			firstCats.add(Category.COMPACT);
			firstCats.add(Category.COVERED);
			secondCats.add(Category.HANDICAPPED);
			Map<Long, Listing> listingsDavis = dbConn.searchUsingCategoriesInclusive(firstCats);
			Map<Long, Listing> listingsLA = dbConn.searchUsingCategoriesInclusive(secondCats);
			
			//System.out.println("listing davis size: "+listingsDavis.size());
			//System.out.println("listing la size: "+listingsLA.size());
			//Assert.assertEquals("davis listings size correct: "+listingsDavis.size(), listingsDavis.size(), davisListings.size());
			//Assert.assertEquals("la listings size correct: "+listingsLA.size(), listingsLA.size(), laListings.size());
			
			for (Listing id : listingsDavis.values()){
				Assert.assertTrue("davis db listings contain correct cat", id.getCategories().contains(Category.COMPACT)
						|| id.getCategories().contains(Category.COVERED));
			}
			
			for (Listing id : listingsLA.values()){
				Assert.assertTrue("la db listings contain correct cat", id.getCategories().contains(Category.HANDICAPPED));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				for (Long id : davisListings){
					dbConn.removeListing(id);
				}
				
//				for (Long id : laListings){
//					dbConn.removeListing(id);
//				}
			}
			catch (Exception e){
				System.out.println("exception");
			}
			
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
