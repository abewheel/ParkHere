import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Address;
import model.CancellationPolicy;
import model.Lender;
import model.Listing;
import model.ListingAvailibility;
import model.Profile;
import model.Reservation;
import model.Seeker;
import model.User;

public class DatabaseConnector {
	private Connection conn;
	public DatabaseConnector(){
		conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://db4free.net/park_here?user=elautz&password=elautz&useSSL=false");
			System.out.println("connected!");
			//st = conn.createStatement();
//			
//			ps = conn.prepareStatement("SELECT * FROM cancellation_policy");
			
//			User user = new User();
//			user.setEmail("EMMA");
//			user.setName("NAME");
//			user.setPassword("PASSWORD");
//			createUser(user);
//			System.out.println("user : "+user.getUser_id());
//			
//			Lender lender = new Lender();
//			Profile lendProf = new Profile();
//			
//			Seeker seeker = new Seeker();
//			Profile seekerProf = new Profile();
//			
//			lender.setProfile(lendProf);
//			lender.setUser_id(user.getUser_id());
//			lender.setBt_merchant_id(2);
//			lendProf.setPhoneNumber(1232345);
//			
//			createLender(lender);
//			System.out.println("lender: "+lender.getLenderId());
//			
//			Listing listing = new Listing();
//			Listing listing2 = new Listing();
//			Listing listing3 = new Listing();
//			
//			Address one = new Address();
//			Address two = new Address();
//			Address three = new Address();
//			
//			one.setFirstLine("first line");
//			one.setSecondLine("second line");
//			one.setCity("Los Angeles");
//			one.setState("CA");
//			one.setZipCode("90007");
//			
//			two.setFirstLine("first 2");
//			two.setSecondLine("second");
//			two.setState("CA");
//			two.setCity("Davis");
//			two.setZipCode("90007");
//			
//			three.setFirstLine("three one");
//			three.setSecondLine("three two");
//			three.setCity("Sacramento");
//			three.setState("CA");
//			three.setZipCode("90007");
//			
//			listing.setAddress(one);
//			listing.setLenderId(lender.getLenderId());
//			listing.setTitle("title 1");
//			listing.setDescription("desc 1");
//			listing.setCancellationPolicy(CancellationPolicy.TEN_DOLLAR_CHARGE);
//			listing.setPrice_per_hr(10);
//			
//			ListingAvailibility la1 = new ListingAvailibility();
//			la1.setBeginDateTime(new Timestamp(100000));
//			la1.setEndDateTime(new Timestamp(765432));
//			//la1.setIsReserved(true);
//			la1.setListingId(listing.getListingId());
//			ListingAvailibility la2 = new ListingAvailibility();
//			la2.setBeginDateTime(new Timestamp(102200));
//			la2.setEndDateTime(new Timestamp(7622342));
//			//la1.setIsReserved(true);
//			la2.setListingId(listing.getListingId());
//			ListingAvailibility la3 = new ListingAvailibility();
//			la3.setBeginDateTime(new Timestamp(10001100));
//			la3.setEndDateTime(new Timestamp(76543211));
//			//la1.setIsReserved(true);
//			la3.setListingId(listing2.getListingId());
//			ListingAvailibility la4 = new ListingAvailibility();
//			la4.setBeginDateTime(new Timestamp(1033000));
//			la4.setEndDateTime(new Timestamp(76543332));
//			//la1.setIsReserved(true);
//			la4.setListingId(listing2.getListingId());
//			ListingAvailibility la5 = new ListingAvailibility();
//			la5.setBeginDateTime(new Timestamp(10009900));
//			la5.setEndDateTime(new Timestamp(765432999));
//			//la1.setIsReserved(true);
//			la5.setListingId(listing3.getListingId());
//			ListingAvailibility la6 = new ListingAvailibility();
//			la6.setBeginDateTime(new Timestamp(11213000));
//			la6.setEndDateTime(new Timestamp(765456432));
//			//la1.setIsReserved(true);
//			la6.setListingId(listing3.getListingId());
//			
//			listing2.setAvailabilityList(new ArrayList<>());
//			listing.setAvailabilityList(new ArrayList<>());
//			listing3.setAvailabilityList(new ArrayList<>());
//			listing.addAvailibility(la1);
//			listing.addAvailibility(la2);
//			listing2.addAvailibility(la3);
//			listing2.addAvailibility(la4);
//			listing3.addAvailibility(la5);
//			listing3.addAvailibility(la6);
//			
//			listing2.setAddress(two);
//			listing2.setLenderId(lender.getLenderId());
//			listing2.setTitle("title 2");
//			listing2.setDescription("desc 2");
//			listing2.setCancellationPolicy(CancellationPolicy.NO_CHARGE);
//			listing2.setPrice_per_hr(15);
//			
//			listing3.setAddress(three);
//			listing3.setLenderId(lender.getLenderId());
//			listing3.setTitle("title 3");
//			listing3.setDescription("desc 3");
//			listing3.setCancellationPolicy(CancellationPolicy.FIVE_DOLLAR_CHARGE);
//			listing3.setPrice_per_hr(20);
//			
//			createListing(listing);
//			System.out.println("listing : "+listing.getListingId());
//			System.out.println("address1 : "+one.getAddressId());
//			createListing(listing2);
//			System.out.println("listing2 : "+listing2.getListingId());
//			System.out.println("address2 : "+two.getAddressId());
//			createListing(listing3);
//			System.out.println("listing3 : "+listing3.getListingId());
//			System.out.println("address3 : "+three.getAddressId());
//			
//			System.out.println("available1: "+la1.getAvailabilityId());
//			System.out.println("available2: "+la2.getAvailabilityId());
//			System.out.println("available3: "+la3.getAvailabilityId());
//			System.out.println("available4: "+la4.getAvailabilityId());
//			System.out.println("available5: "+la5.getAvailabilityId());
//			System.out.println("available6: "+la6.getAvailabilityId());
//			
//			seeker.setBt_merchant_id(5);
//			seeker.setUser_id(user.getUser_id());
//			seeker.setProfile(seekerProf);
//			seekerProf.setPhoneNumber(98765);
//			
//			createSeeker(seeker);
//			System.out.println("seeker : "+seeker.getSeekerId());
//			
//			this.createSeekerFavorite(seeker.getSeekerId(), listing.getListingId());
//			this.createSeekerFavorite(seeker.getSeekerId(), listing2.getListingId());
//			
//			Reservation reservation = new Reservation();
//			reservation.setBTTransactionId(6);
//			reservation.setLenderId(lender.getLenderId());
//			reservation.setListingId(listing.getLenderId());
//			reservation.setSeekerId(seeker.getSeekerId());
//			reservation.setPricePerHour((int) listing.getPricePerHr());
//			reservation.setListingAvailibility(la2);
//			
//			createReservation(reservation);
//			System.out.println("reservation : "+reservation.getReservationId());
//			
//			User userQueried = getUser(user.getEmail());
//			System.out.println((userQueried.getUser_id() == user.getUser_id()) +" : correct user id returned");
//			Seeker seekerQueried = userQueried.getSeeker();
//			Lender lenderQueried = userQueried.getLender();
//			System.out.println((seekerQueried.getSeekerId() == seeker.getSeekerId())+" : corrrect seeker id returned");
//			System.out.println((lenderQueried.getLenderId() == lender.getLenderId())+" : correct lender id returned");
//			
//			
//			System.out.println((seeker.getReservations().isEmpty()) + " : reservations are empty");
//			
			
			
			//ps.setString(1, name); // set first variable in prepared statement
//			rs = ps.executeQuery();
//			//System.out.println(rs.);
//			while (rs.next()) {
//				Integer fname = rs.getInt("cancellation_policy_id");
//				String lname = rs.getString("cancellation_policy");
//				//int studentID = rs.getInt("studentID");
//				System.out.println ("id = " + fname);
//				System.out.println ("policy = " + lname);
//				//System.out.println ("studentID = " + studentID);
//			}
		}
		catch (SQLException sqle){
			System.out.println("sql excep: "+sqle.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class not found excep: "+e.getMessage());
		}
		
	}
	
	public List<Listing> getSeekerListings(long seekerId) throws SQLException {
		List<Listing> favorites = new ArrayList<>();
		
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LENDER_ID_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
				", a."+DBConstants.STATE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+
				" INNER JOIN "+DBConstants.SEEKER_FAVORITES_TB+" sf ON l."+DBConstants.LISTING_ID_COL+" = sf."+DBConstants.LISTING_ID_COL
				+"WHERE "+"sf."+DBConstants.SEEKER_ID_COL+" = "+seekerId);
		
		ResultSet rsListing = psListing.executeQuery();
		populateListing(favorites, rsListing);
		return favorites;
	}
	
	private void populateListing(List<Listing> listings, ResultSet rsListing) throws SQLException{
		
		while (rsListing.next()){
			Listing listing = new Listing();
			//set images
			Address address = new Address();
			address.setAddressId(rsListing.getLong(rsListing.findColumn(DBConstants.ADDRESS_ID_COL)));
			address.setFirstLine(rsListing.getString(rsListing.findColumn(DBConstants.FIRST_LINE_COL)));
			address.setSecondLine(rsListing.getString(rsListing.findColumn(DBConstants.SECOND_LINE_COL)));
			address.setCity(rsListing.getString(rsListing.findColumn(DBConstants.CITY_COL)));
			address.setState(rsListing.getString(rsListing.findColumn(DBConstants.STATE_COL)));
			address.setZipCode(rsListing.getString(rsListing.findColumn(DBConstants.ZIP_CODE_COL)));
			
			listing.setLenderId(rsListing.getLong(rsListing.findColumn(DBConstants.LENDER_ID_COL)));
			listing.setAddress(address);
			listing.setCancellationPolicy(rsListing.getString(rsListing.findColumn(DBConstants.CANCELLATION_POLICY_COL)));
			listing.setDescription(rsListing.getString(rsListing.findColumn(DBConstants.DESCRIPTION_COL)));
			listing.setTitle(rsListing.getString(rsListing.findColumn(DBConstants.LISTING_TITLE_COL)));
			listing.setListingId(rsListing.getLong(rsListing.findColumn(DBConstants.LISTING_ID_COL)));
			listing.setNumberOfRatings(rsListing.getInt(rsListing.findColumn(DBConstants.NUM_RATINGS_COL)));
			listing.setTotalRating(rsListing.getInt(rsListing.findColumn(DBConstants.TOTAL_RATING_COL)));
			
			PreparedStatement psCategories = conn.prepareStatement("SELECT "+DBConstants.CATEGORY_COL+" FROM "+DBConstants.LISTING_CATEGORY_TB+
					" l INNER JOIN "+DBConstants.CATEGORY_TB+" c ON l."+DBConstants.CATEGORY_ID_COL+" = c."+DBConstants.CATEGORY_ID_COL+" WHERE "+DBConstants.LISTING_ID_COL+
					" = "+listing.getListingId());
			
			ResultSet rsCats = psCategories.executeQuery();
			while (rsCats.next()){
				listing.getCategories().add(rsCats.getString(1));
			}
			
			PreparedStatement psAvailable = conn.prepareStatement("SELECT "+DBConstants.AVAILIBILITY_ID_COL+", "+
			DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+", "+DBConstants.IS_RESERVED_COL+" FROM "+DBConstants.AVAILABILITY_TB+
			" WHERE "+DBConstants.LISTING_ID_COL+" = "+listing.getListingId());
			
			ResultSet rsAvailable = psAvailable.executeQuery();
			while (rsAvailable.next()){
				ListingAvailibility av = new ListingAvailibility();
				av.setListingId(listing.getListingId());
				av.setAvailabilityId(rsAvailable.getLong(rsAvailable.findColumn(DBConstants.AVAILIBILITY_ID_COL)));
				av.setBeginDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
				av.setEndDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.END_DATE_TIME_COL)));
				av.setIsReserved(rsAvailable.getBoolean(rsAvailable.findColumn(DBConstants.IS_RESERVED_COL)));
				listing.getAvailabilityList().add(av);
			}
			
			listings.add(listing);
		}
	}
	
	public List<Listing>getLenderListings(long lenderId, Lender lender) throws SQLException{
		
		List<Listing> listings = new ArrayList<>();
		
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
				", a."+DBConstants.STATE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+
				"l."+DBConstants.LENDER_ID_COL+" = "+lenderId);
		
		ResultSet rsListing = psListing.executeQuery();
		populateListing(listings, rsListing);
		
		return listings;
	}
	
	public void getLender(long userId, User user) throws SQLException{
		PreparedStatement psLender = conn.prepareStatement("SELECT * FROM "+DBConstants.LENDER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+userId);
		ResultSet rsLender = psLender.executeQuery();
		
		if (rsLender.next()){
			System.out.println("got the lender!!");
			Lender lender = new Lender();
			lender.setUser_id(userId);
			lender.setLenderId(rsLender.getLong(rsLender.findColumn(DBConstants.LENDER_ID_COL)));
			Profile profile = new Profile();
			profile.setPhoneNumber(rsLender.getString(rsLender.findColumn(DBConstants.PHONE_NUM_COL)));
			System.out.println("lender phone : "+rsLender.getString(rsLender.findColumn(DBConstants.PHONE_NUM_COL)));
			//profile pic
			//merchant id
			lender.setProfile(profile);
			
			lender.setListings(getLenderListings(lender.getLenderId(), lender));
			lender.setReservations(getReservations(lender.getLenderId(), true));
			
			user.setLender(lender);
			if (rsLender.getBoolean(rsLender.findColumn(DBConstants.IS_DEFAULT_ROLE_COL))){
				user.setCurrent_role(lender);
			}
		}
	}
	
	public void getSeeker(long userId, User user) throws SQLException{
		PreparedStatement psSeeker = conn.prepareStatement("SELECT * FROM "+DBConstants.SEEKER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+userId);
		ResultSet rsSeeker = psSeeker.executeQuery();
		
		if (rsSeeker.next()){
			Seeker seeker = new Seeker();
			seeker.setUser_id(userId);
			seeker.setSeekerId(rsSeeker.getLong(rsSeeker.findColumn(DBConstants.SEEKER_ID_COL)));
			Profile profile = new Profile();
			profile.setPhoneNumber(rsSeeker.getString(rsSeeker.findColumn(DBConstants.PHONE_NUM_COL)));
			//profile pic
			//merchant id
			seeker.setProfile(profile);
			//seeker.setIsDefau
			seeker.setFavorites(getSeekerListings(seeker.getSeekerId()));
			seeker.setReservations(getReservations(seeker.getSeekerId(), false));
			
			user.setSeeker(seeker);
			if (rsSeeker.getBoolean(rsSeeker.findColumn(DBConstants.IS_DEFAULT_ROLE_COL))){
				user.setCurrent_role(seeker);
			}
		}

	}
	
	public List<Listing> search(String zipcode) throws SQLException{
		List<Listing> listings = new ArrayList<>();
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
				", a."+DBConstants.STATE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+
				"a."+DBConstants.ZIP_CODE_COL+" = "+zipcode);
		//PreparedStatement ps = conn.prepareStatement("SELECT )
		ResultSet rs = psListing.executeQuery();
		populateListing(listings, rs);
		return listings;
	}
	
	public List<Reservation> getReservations(long id, Boolean isLender) throws SQLException{
		List<Reservation> reservations = new ArrayList<>();
		
		PreparedStatement ps = conn.prepareStatement("SELECT r."+DBConstants.RESERVATION_ID_COL+", r."+DBConstants.SEEKER_ID_COL+
				", r."+DBConstants.LENDER_ID_COL+", r."+DBConstants.LISTING_ID_COL+", r."+DBConstants.AVAILIBILITY_ID_COL+
				", r."+DBConstants.AMOUNT_PAID_COL+", r."+DBConstants.TRANSACTION_ID_COL+
				", a."+DBConstants.BEGIN_DATE_TIME_COL+", a."+DBConstants.END_DATE_TIME_COL+", a."+
				DBConstants.IS_RESERVED_COL+" FROM "+DBConstants.RESERVATION_TB+" r INNER JOIN "+DBConstants.AVAILABILITY_TB+
				" a ON r."+DBConstants.AVAILIBILITY_ID_COL+" = a."+DBConstants.AVAILIBILITY_ID_COL+" WHERE r."+
				(isLender? DBConstants.LENDER_ID_COL : DBConstants.SEEKER_ID_COL)+" = "+id);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()){
			Reservation reservation = new Reservation();
			ListingAvailibility available = new ListingAvailibility();
			reservation.setBtTransactionId(rs.getInt(rs.findColumn(DBConstants.TRANSACTION_ID_COL)));
			reservation.setLenderId(rs.getLong(rs.findColumn(DBConstants.LENDER_ID_COL)));
			reservation.setSeekerId(rs.getLong(rs.findColumn(DBConstants.SEEKER_ID_COL)));
			reservation.setListingId(rs.getLong(rs.findColumn(DBConstants.LISTING_ID_COL)));
			reservation.setReservationId(rs.getLong(rs.findColumn(DBConstants.RESERVATION_ID_COL)));
			reservation.setPricePerHour(rs.getInt(rs.findColumn(DBConstants.PRICE_PER_HR_COL)));
			available.setAvailabilityId(rs.getLong(rs.findColumn(DBConstants.AVAILIBILITY_ID_COL)));
			available.setBeginDateTime(rs.getTimestamp(rs.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
			available.setEndDateTime(rs.getTimestamp(rs.findColumn(DBConstants.END_DATE_TIME_COL)));
			available.setIsReserved(rs.getBoolean(rs.findColumn(DBConstants.IS_RESERVED_COL)));
			reservation.setListingAvailibility(available);
			reservations.add(reservation);
		}
		
		return reservations;
	}
	
	public User getUser(String email) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM "+DBConstants.USER_TB+" WHERE "+DBConstants.USER_EMAIL_COL+" = '"+email+"'");
		ResultSet rs = ps.executeQuery();
		User user = new User();
		if (rs.next()){
			user.setEmail(email);
			user.setName(rs.getString(rs.findColumn(DBConstants.USER_NAME_COL)));
			user.setPassword(rs.getString(rs.findColumn(DBConstants.PASSWORD_COL)));
			user.setUser_id(rs.getLong(rs.findColumn(DBConstants.USER_ID_COL)));
		}
		
//		PreparedStatement psSeeker = conn.prepareStatement("SELECT * FROM "+DBConstants.SEEKER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+user.getUser_id());
//		ResultSet rsSeeker = psSeeker.executeQuery();
//		
//		if (rsSeeker.next()){
//			Seeker seeker = new Seeker();
//			seeker.setUser_id(user.getUser_id());
//			seeker.setSeekerId(rsSeeker.getLong(rsSeeker.findColumn(DBConstants.SEEKER_ID_COL)));
//			Profile profile = new Profile();
//			profile.setPhoneNumber(rsSeeker.getString(rsSeeker.findColumn(DBConstants.PHONE_NUM_COL)));
//			//profile pic
//			//merchant id
//			seeker.setProfile(profile);
//			
//			user.setSeeker(seeker);
//		}
		
		getSeeker(user.getUser_id(), user);
		getLender(user.getUser_id(), user);
	
		System.out.println("user naem "+user.getName());
		System.out.println("email "+user.getEmail());
		return user;
	}
	
	public User createUser(User user) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.USER_TB+" ("+
				DBConstants.USER_EMAIL_COL+", "+DBConstants.USER_NAME_COL+", "+DBConstants.PASSWORD_COL+") VALUES ('"+
				user.getEmail()+"', '"+user.getName()+"', '"+user.getPassword()+"')", Statement.RETURN_GENERATED_KEYS);
	
		System.out.println("INSERT INTO "+DBConstants.USER_TB+" ("+
				DBConstants.USER_EMAIL_COL+", "+DBConstants.USER_NAME_COL+", "+DBConstants.PASSWORD_COL+") VALUES ("+
				user.getEmail()+", "+user.getName()+", "+user.getPassword()+")");
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		System.out.println("about to print id");
		if (rs.next()){
			System.out.println(rs.getLong(1));
			user.setUser_id(rs.getLong(1));
		}
		
		return user;
	}
	
	//left out profile pic for now
	//hardcoded default role for now
	public Seeker createSeeker(Seeker seeker) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.SEEKER_TB+" ("+DBConstants.USER_ID_COL+", "+DBConstants.PHONE_NUM_COL+", "+
				DBConstants.IS_DEFAULT_ROLE_COL+") VALUES ("+seeker.getUser_id()+", '"+seeker.getProfile().getPhoneNumber()+"', "+true+")", Statement.RETURN_GENERATED_KEYS);
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()){
			seeker.setSeekerId(rs.getLong(1));
		}
		
		return seeker;
	}
	
	//left out profile pic for now
	//hardcoded default role for now
	public Lender createLender(Lender lender) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.LENDER_TB+" ("+DBConstants.USER_ID_COL+", "+DBConstants.PHONE_NUM_COL+", "+
				DBConstants.IS_DEFAULT_ROLE_COL+") VALUES ("+lender.getUser_id()+", '"+lender.getProfile().getPhoneNumber()+"', "+true+")", Statement.RETURN_GENERATED_KEYS);
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()){
			lender.setLenderId(rs.getLong(1));
		}
		
		return lender;
	}
	
	//NOT DONE
	public Listing createListing(Listing listing) throws SQLException{
		
		System.out.println("in create listing");
		Address address = listing.getAddress();
		PreparedStatement psAddress = conn.prepareStatement("INSERT INTO "+DBConstants.ADDRESS_TB+" ("+DBConstants.FIRST_LINE_COL+", "+DBConstants.SECOND_LINE_COL+
				", "+DBConstants.ZIP_CODE_COL+", "+DBConstants.CITY_COL+", "+DBConstants.STATE_COL+") VALUES ('"+address.getFirstLine()+
				"', '"+address.getSecondLine()+"', '"+address.getZipCode()+"', '"+address.getCity()+"', '"+address.getState()+"')", Statement.RETURN_GENERATED_KEYS);
		
		psAddress.executeUpdate();
		ResultSet rsAddress = psAddress.getGeneratedKeys();
		if (rsAddress.next()){
			address.setAddressId(rsAddress.getLong(1));
		}
		
		//String sql = "INSERT INTO "+DBConstants.AVAILABILITY_TB+" "
		PreparedStatement psListing = conn.prepareStatement("INSERT INTO "+DBConstants.LISTING_TB+" ("+DBConstants.LENDER_ID_COL+", "+DBConstants.LISTING_TITLE_COL+
				", "+DBConstants.DESCRIPTION_COL+", "+DBConstants.TOTAL_RATING_COL+", "+DBConstants.NUM_RATINGS_COL+", "+
				DBConstants.CANCELLATION_POLICY_ID_COL+", "+DBConstants.PRICE_PER_HR_COL+", "+DBConstants.ADDRESS_ID_COL+") VALUES ("+listing.getLenderId()+
				", '"+listing.getTitle()+"', '"+listing.getDescription()+"', 0, 0, (SELECT "+DBConstants.CANCELLATION_POLICY_ID_COL+" FROM "+DBConstants.CANCELLATION_POLICY_TB+
				" WHERE "+DBConstants.CANCELLATION_POLICY_COL+" = '"+listing.getCancellationPolicy()+"'), "+listing.getPricePerHr()+", "+
				address.getAddressId()+")", Statement.RETURN_GENERATED_KEYS);
		
		psListing.executeUpdate();
		ResultSet rsListing = psListing.getGeneratedKeys();
		if (rsListing.next()){
			listing.setListingId(rsListing.getLong(1));
		}
		
		if (listing.getAvailabilityList()!= null && !listing.getAvailabilityList().isEmpty()){
			
			
			for (ListingAvailibility av : listing.getAvailabilityList()){
				
				PreparedStatement psAv = conn.prepareStatement("INSERT INTO "+DBConstants.AVAILABILITY_TB+" ("+DBConstants.LISTING_ID_COL+
						", "+DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+") VALUES ("+listing.getListingId()+", ?, ?)", Statement.RETURN_GENERATED_KEYS);
				psAv.setTimestamp(1, av.getBeginDateTime());
				psAv.setTimestamp(2,  av.getEndDateTime());
				psAv.executeUpdate();
				ResultSet rsAv = psAv.getGeneratedKeys();
				av.setAvailabilityId(rsAv.getLong(1));
				av.setListingId(listing.getListingId());
			}
		}
		//insert images
		return listing;
	}
	
	public void createSeekerFavorite(long seekerId, long listingId) throws SQLException{
		PreparedStatement psfav = conn.prepareStatement("INSERT INTO "+DBConstants.SEEKER_FAVORITES_TB+" ("+DBConstants.SEEKER_ID_COL+", "+DBConstants.LISTING_ID_COL+") VALUES ("+
				seekerId+", "+listingId+")");
		psfav.executeUpdate();
		
	}
	
	public Reservation createReservation(Reservation reservation) throws SQLException{
		//not currently inserting amount paid or transaction_id
		PreparedStatement psfav = conn.prepareStatement("INSER INTO "+DBConstants.RESERVATION_TB+" ("+DBConstants.LISTING_ID_COL+
				", "+DBConstants.LENDER_ID_COL+", "+DBConstants.SEEKER_ID_COL+", "+DBConstants.AVAILIBILITY_ID_COL+
				") VALUES ("+ reservation.getListingId()+", "+reservation.getLenderId()+", "+reservation.getSeekerId()+", "+reservation.getListingAvailibility().getAvailabilityId()+")", 
				Statement.RETURN_GENERATED_KEYS);
		psfav.executeUpdate();
		ResultSet rs = psfav.getGeneratedKeys();
		if (rs.next()){
			reservation.setReservationId(rs.getLong(1));
		}
		return reservation;
	}
	
	public Boolean checkUserPasssword(String email, String password) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM "+DBConstants.USER_TB+" WHERE "+DBConstants.USER_EMAIL_COL+" = '"+email
				+"' AND "+DBConstants.PASSWORD_COL+" = '"+password+"'");
		
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

}
