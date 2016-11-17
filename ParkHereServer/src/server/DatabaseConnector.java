package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.SearchMessage;
import model.Address;
import model.Lender;
import model.Listing;
import model.ListingAvailibility;
import model.ListingResult;
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
			System.out.println("before db connnect");
			conn = DriverManager.getConnection("jdbc:mysql://db4free.net/park_here?user=elautz&password=elautz&useSSL=false");
			System.out.println("connected!");

		}
		catch (SQLException sqle){
			System.out.println("sql excep: "+sqle.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class not found excep: "+e.getMessage());
		}
		
	}
	
	public Map<Long, Listing> getSeekerListings(long seekerId) throws SQLException {
		Map<Long, Listing> favorites = new HashMap<>();
		System.out.println("seeker id in get favorites: "+seekerId);
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LENDER_ID_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+", l."+DBConstants.LISTING_TITLE_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+", a."+DBConstants.LATITUDE_COL+", a."+DBConstants.LONGITUDE_COL+
				", a."+DBConstants.STATE_COL+" FROM "+DBConstants.SEEKER_FAVORITES_TB+" sf INNER JOIN "+ DBConstants.LISTING_TB+" l ON sf."+DBConstants.LISTING_ID_COL+
				" = l."+DBConstants.LISTING_ID_COL+" INNER JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+"sf."+DBConstants.SEEKER_ID_COL+" = "+seekerId);
		
		
		ResultSet rsListing = psListing.executeQuery();
		return populateListings(rsListing);
		//return favorites;
	}
	
	private Listing populateListing(ResultSet rsListing) throws SQLException{
		Listing listing = new Listing();
		//set images
		Address address = new Address();
		address.setAddressId(rsListing.getLong(rsListing.findColumn(DBConstants.ADDRESS_ID_COL)));
		address.setFirstLine(rsListing.getString(rsListing.findColumn(DBConstants.FIRST_LINE_COL)));
		address.setSecondLine(rsListing.getString(rsListing.findColumn(DBConstants.SECOND_LINE_COL)));
		address.setCity(rsListing.getString(rsListing.findColumn(DBConstants.CITY_COL)));
		address.setState(rsListing.getString(rsListing.findColumn(DBConstants.STATE_COL)));
		address.setZipCode(rsListing.getString(rsListing.findColumn(DBConstants.ZIP_CODE_COL)));
		address.setLatitude(rsListing.getDouble(rsListing.findColumn(DBConstants.LATITUDE_COL)));
		address.setLongitude(rsListing.getDouble(rsListing.findColumn(DBConstants.LONGITUDE_COL)));
		System.out.println("before set lender id on listing");
		listing.setLenderId(rsListing.getLong(rsListing.findColumn(DBConstants.LENDER_ID_COL)));
		System.out.println("after set lender id on listing");
		listing.setAddress(address);
		listing.setCancellationPolicy(rsListing.getString(rsListing.findColumn(DBConstants.CANCELLATION_POLICY_COL)));
		System.out.println("cancellation policy retrieved: "+listing.getCancellationPolicy());
		listing.setDescription(rsListing.getString(rsListing.findColumn(DBConstants.DESCRIPTION_COL)));
		listing.setTitle(rsListing.getString(rsListing.findColumn(DBConstants.LISTING_TITLE_COL)));
		listing.setListingId(rsListing.getLong(rsListing.findColumn(DBConstants.LISTING_ID_COL)));
		listing.setPrice_per_hr(rsListing.getDouble(rsListing.findColumn(DBConstants.PRICE_PER_HR_COL)));
		listing.setNumberOfRatings(rsListing.getInt(rsListing.findColumn(DBConstants.NUM_RATINGS_COL)));
		listing.setTotalRating(rsListing.getInt(rsListing.findColumn(DBConstants.TOTAL_RATING_COL)));
		
		PreparedStatement psCategories = conn.prepareStatement("SELECT "+DBConstants.CATEGORY_COL+" FROM "+DBConstants.LISTING_CATEGORY_TB+
				" l INNER JOIN "+DBConstants.CATEGORY_TB+" c ON l."+DBConstants.CATEGORY_ID_COL+" = c."+DBConstants.CATEGORY_ID_COL+" WHERE "+DBConstants.LISTING_ID_COL+
				" = "+listing.getListingId());
		System.out.println("before get listing categories");
		ResultSet rsCats = psCategories.executeQuery();
		listing.setCategories(new ArrayList<>());
		while (rsCats.next()){
			System.out.println("WE HAVE CATEGORIES");
			listing.getCategories().add(rsCats.getString(1));
		}
		
		PreparedStatement psComments = conn.prepareStatement("SELECT * FROM "+DBConstants.LISTING_COMMENT_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listing.getListingId());
		ResultSet rsComments = psComments.executeQuery();
		listing.setComments(new ArrayList<>());
		while (rsComments.next()){
			listing.getComments().add(rsComments.getString(rsComments.findColumn(DBConstants.COMMENT_COL)));
		}
		
		PreparedStatement psAvailable = conn.prepareStatement("SELECT "+DBConstants.AVAILIBILITY_ID_COL+", "+
		DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+", "+DBConstants.IS_RESERVED_COL+" FROM "+DBConstants.AVAILABILITY_TB+
		" WHERE "+DBConstants.LISTING_ID_COL+" = "+listing.getListingId());
		System.out.println("before get listing availabilities");
		ResultSet rsAvailable = psAvailable.executeQuery();
		if (listing.getAvailabilityList() == null) listing.setAvailabilityList(new ArrayList<>());
		while (rsAvailable.next()){
			ListingAvailibility av = new ListingAvailibility();
			av.setListingId(listing.getListingId());
			av.setAvailabilityId(rsAvailable.getLong(rsAvailable.findColumn(DBConstants.AVAILIBILITY_ID_COL)));
			av.setBeginDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
			av.setEndDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.END_DATE_TIME_COL)));
			av.setIsReserved(rsAvailable.getBoolean(rsAvailable.findColumn(DBConstants.IS_RESERVED_COL)));
			
			listing.getAvailabilityList().add(av);
		}
		return listing;
	}
	
	private Map<Long, Listing> populateListings(ResultSet rsListing){
		Map<Long, Listing> listings = new HashMap<>();
		System.out.println("before get populate listings");
		try {
			while (rsListing.next()){
				
				Listing listing = populateListing(rsListing);
				listings.put(listing.getListingId(), listing);
				System.out.println(listing.getCancellationPolicy());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return listings;
	}
	
	public Map<Long, Listing>getLenderListings(long lenderId) throws SQLException{
		System.out.println("before get listings");
		//Map<Long, Listing> listings = new HashMap<>();
		
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LENDER_ID_COL+", +l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+
				", l."+DBConstants.LISTING_TITLE_COL+", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
				", a."+DBConstants.STATE_COL+", a."+DBConstants.LONGITUDE_COL+", a."+DBConstants.LATITUDE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+
				"l."+DBConstants.LENDER_ID_COL+" = "+lenderId);
		
		ResultSet rsListing = psListing.executeQuery();
		return populateListings(rsListing);
		
	}
	
	public void getLender(long userId, User user) throws SQLException, DBException{
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
			
			lender.setListings(getLenderListings(lender.getLenderId()));
			lender.setMerchantId(rsLender.getString(rsLender.findColumn(DBConstants.MERCHANT_ID_COL)));
			lender.setReservations(getReservations(lender.getLenderId(), true));
			System.out.println("after  get listings and reservations");
			user.setLender(lender);
			if (rsLender.getBoolean(rsLender.findColumn(DBConstants.IS_DEFAULT_ROLE_COL))){
				user.setCurrent_role(lender);
			}
		}
		else{
			user.setLender(null);
		}
	}
	
	public void removeListing(long listingId) throws SQLException{
	
		PreparedStatement psAvailabilities = conn.prepareStatement("DELETE FROM "+DBConstants.AVAILABILITY_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psAvailabilities.executeUpdate();
		PreparedStatement psCategories = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_CATEGORY_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psCategories.executeUpdate();
		PreparedStatement psImages = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_IMAGE_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psImages.executeUpdate();
		PreparedStatement psSeekerFavorites = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_FAVORITES_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psSeekerFavorites.executeUpdate();
		PreparedStatement psListing = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psListing.executeUpdate();
	}
	
	public Boolean canRemoveListing(long listingId) throws SQLException{
		PreparedStatement psReservation = conn.prepareStatement("SELECT * FROM "+DBConstants.RESERVATION_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		ResultSet rs = psReservation.executeQuery();
		return rs.next();
	}
	
	public void getSeeker(long userId, User user) throws SQLException, DBException{
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
			seeker.setFavorites(getSeekerListings(seeker.getSeekerId()));
			seeker.setReservations(getReservations(seeker.getSeekerId(), false));
			
			user.setSeeker(seeker);
			if (rsSeeker.getBoolean(rsSeeker.findColumn(DBConstants.IS_DEFAULT_ROLE_COL))){
				user.setCurrent_role(seeker);
			}
		}
		else{
			user.setSeeker(null);
		}

	}
	
//	public Map<Long, Listing> search(String zipcode) throws SQLException{
//		//Map<Long, Listing> listings = new HashMap<>();
//		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LENDER_ID_COL+", l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LISTING_TITLE_COL+
//				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
//				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
//				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
//				", a."+DBConstants.STATE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
//				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
//				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+
//				"a."+DBConstants.ZIP_CODE_COL+" = '"+zipcode+"'");
//		//PreparedStatement ps = conn.prepareStatement("SELECT )
//		ResultSet rs = psListing.executeQuery();
//		return populateListings(rs);
//		//return listings;
//	}
//	
	
	public Map<Long, Reservation> getReservations(long id, Boolean isLender) throws DBException{
		Map<Long, Reservation> reservations = new HashMap<>();
		System.out.println("before get reservations");
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT r."+DBConstants.RESERVATION_ID_COL+", r."+DBConstants.SEEKER_ID_COL+
					", r."+DBConstants.LENDER_ID_COL+", r."+DBConstants.LISTING_ID_COL+
					", r."+DBConstants.AMOUNT_PAID_COL+", r."+DBConstants.TRANSACTION_ID_COL+
					", r."+DBConstants.BEGIN_DATE_TIME_COL+", r."+DBConstants.END_DATE_TIME_COL+
					" FROM "+DBConstants.RESERVATION_TB+" r WHERE r."+
					(isLender? DBConstants.LENDER_ID_COL : DBConstants.SEEKER_ID_COL)+" = "+id);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				Reservation reservation = new Reservation();
				//ListingAvailibility available = new ListingAvailibility();
				reservation.setBtTransactionId(rs.getInt(rs.findColumn(DBConstants.TRANSACTION_ID_COL)));
				reservation.setLenderId(rs.getLong(rs.findColumn(DBConstants.LENDER_ID_COL)));
				reservation.setSeekerId(rs.getLong(rs.findColumn(DBConstants.SEEKER_ID_COL)));
				reservation.setListingId(rs.getLong(rs.findColumn(DBConstants.LISTING_ID_COL)));
				reservation.setReservationId(rs.getLong(rs.findColumn(DBConstants.RESERVATION_ID_COL)));
				reservation.setListing(getListing(reservation.getListingId()));
				reservation.setBeginDate(rs.getTimestamp(rs.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
				reservation.setEndDate(rs.getTimestamp(rs.findColumn(DBConstants.END_DATE_TIME_COL)));
				//reservation.setPricePerHour(rs.getInt(rs.findColumn(DBConstants.PRICE_PER_HR_COL)));
			//	available.setAvailabilityId(rs.getLong(rs.findColumn(DBConstants.AVAILIBILITY_ID_COL)));
			//	available.setBeginDateTime(rs.getTimestamp(rs.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
			//	available.setEndDateTime(rs.getTimestamp(rs.findColumn(DBConstants.END_DATE_TIME_COL)));
			//	available.setIsReserved(rs.getBoolean(rs.findColumn(DBConstants.IS_RESERVED_COL)));
			//	reservation.setListingAvailibility(available);
				reservations.put(reservation.getReservationId(), reservation);
			}
			System.out.println("after get reservations");
			//if (reservations.isEmpty())
			return reservations;
		} catch (SQLException e) {
			throw new DBException(DBException.GET_RESERVATION + " "+e.getMessage());
		}
		
	}
	
	public Listing getListing(long listingId) throws SQLException{
		

		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LENDER_ID_COL+", +l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+
				", l."+DBConstants.LISTING_TITLE_COL+", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+
				", a."+DBConstants.STATE_COL+", a."+DBConstants.LONGITUDE_COL+", a."+DBConstants.LATITUDE_COL+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+" WHERE "+
				"l."+DBConstants.LISTING_ID_COL+" = "+listingId);
		
		ResultSet rsListing = psListing.executeQuery();
		Map<Long, Listing> map = populateListings(rsListing);
		for (Listing list : map.values()){
			return list;
		}
		
		return null;
	}
	
	public User getUser(String email) throws DBException{
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM "+DBConstants.USER_TB+" WHERE "+DBConstants.USER_EMAIL_COL+" = '"+email+"'");
			ResultSet rs = ps.executeQuery();
			User user = new User();
			if (rs.next()){
				user.setEmail(email);
				user.setFirstName(rs.getString(rs.findColumn(DBConstants.FIRST_NAME_COL)));
				user.setLastName(rs.getString(rs.findColumn(DBConstants.LAST_NAME_COL)));
				user.setPassword(rs.getString(rs.findColumn(DBConstants.PASSWORD_COL)));
				user.setUser_id(rs.getLong(rs.findColumn(DBConstants.USER_ID_COL)));
			}
			
			getSeeker(user.getUser_id(), user);
			getLender(user.getUser_id(), user);
		
			System.out.println("user naem "+user.getFirstName());
			System.out.println("email "+user.getEmail());
			return user;
		} catch (SQLException e) {
			throw new DBException(DBException.INVALID_USER_EMAIL+" "+DBException.GET_USER);
		}
	}
	
	public User createUser(User user) throws DBException{
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("INSERT INTO "+DBConstants.USER_TB+" ("+
					DBConstants.USER_EMAIL_COL+", "+DBConstants.FIRST_NAME_COL+", "+DBConstants.LAST_NAME_COL+", "+DBConstants.PASSWORD_COL+") VALUES ('"+
					user.getEmail()+"', '"+user.getFirstName()+"', '"+user.getLastName()+"', '"+user.getPassword()+"')", Statement.RETURN_GENERATED_KEYS);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			System.out.println("about to print id");
			if (rs.next()){
				System.out.println(rs.getLong(1));
				user.setUser_id(rs.getLong(1));
			}
			
			return user;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(DBException.INVALID_USER_EMAIL+" "+DBException.CREATE_USER);
		}
	
		
	}

	public Map<Long, ListingResult> searchByCoordinatesAndDate(SearchMessage searchMessage) throws SQLException{
		Map<Long, ListingResult> results = new HashMap<>();
		System.out.println("in search");
		double latitude = searchMessage.advanced.getLat();
		double longitude = searchMessage.advanced.getLon();
		
		double minLat = latitude - 5;
		double minLong = longitude - 5;
		double maxLat = latitude + 5;
		double maxLong = longitude + 5;
		
		System.out.println("minLat: "+minLat);
		System.out.println("maxLat: "+maxLat);
		System.out.println("minLon: "+minLong);
		System.out.println("maxLon: "+maxLong);
		
		StringBuilder sb = new StringBuilder("SELECT l."+DBConstants.LENDER_ID_COL+", l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LISTING_TITLE_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+", a."+DBConstants.LATITUDE_COL+", a."+DBConstants.LONGITUDE_COL+
				", a."+DBConstants.STATE_COL+", ( 3959 * acos( cos( radians("+searchMessage.advanced.getLat()+") )  * cos( radians( a."+DBConstants.LATITUDE_COL+" ) ) * "+
				"cos( radians( a."+DBConstants.LONGITUDE_COL+" ) - radians("+searchMessage.advanced.getLon()+") ) + sin( radians("+searchMessage.advanced.getLat()+") ) "
	              +"* sin( radians( a."+DBConstants.LATITUDE_COL+" ) ) ) ) AS "+DBConstants.DISTANCE_ALIAS+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+
				(!searchMessage.advanced.getCategories().isEmpty() ? 
						" INNER JOIN "+DBConstants.LISTING_CATEGORY_TB+" lc ON l."+DBConstants.LISTING_ID_COL+" = lc."+DBConstants.LISTING_ID_COL+
						" INNER JOIN "+DBConstants.CATEGORY_TB+" ca ON ca."+DBConstants.CATEGORY_ID_COL+" = lc."+DBConstants.CATEGORY_ID_COL : "")+
				" WHERE ");
		
		if (searchMessage.advanced.getEndTime() != null){
			System.out.println(searchMessage.advanced.getStartTime().toString());
			System.out.println(searchMessage.advanced.getEndTime().toString());
			sb.append(" EXISTS (SELECT NULL FROM "+DBConstants.AVAILABILITY_TB+" av WHERE av."+DBConstants.LISTING_ID_COL+" = "+
					" l."+DBConstants.LISTING_ID_COL+" AND av."+DBConstants.BEGIN_DATE_TIME_COL+" <= ? AND av."+DBConstants.END_DATE_TIME_COL+" >= ?) AND ");
			
			sb.append(" NOT EXISTS(SELECT NULL FROM "+DBConstants.RESERVATION_TB+" r WHERE r."+DBConstants.LISTING_ID_COL+" = l."+DBConstants.LISTING_ID_COL
					+" AND (r."+DBConstants.END_DATE_TIME_COL+
				" BETWEEN ? AND ?) OR (r."+DBConstants.BEGIN_DATE_TIME_COL+" BETWEEN ? AND ?)) AND ");
		}
		
				
		if (searchMessage.advanced.getCategories() != null && !searchMessage.advanced.getCategories().isEmpty()){
			sb.append("ca."+DBConstants.CATEGORY_COL+" IN (");
			for (String cat : searchMessage.advanced.getCategories()){
				sb.append("'"+cat+"',");
			}
			
			sb.deleteCharAt(sb.length()-1);
			sb.append(") AND ");
		}
		
		sb.append("l."+DBConstants.PRICE_PER_HR_COL+" < "+searchMessage.advanced.getPrice()+" AND a."+DBConstants.LATITUDE_COL+" BETWEEN "+minLat+" AND "+maxLat+" AND a."+DBConstants.LONGITUDE_COL
				+" BETWEEN "+minLong+" AND "+maxLong +" HAVING "+DBConstants.DISTANCE_ALIAS+" < "+searchMessage.advanced.getDistance()
						+ " ORDER BY "+DBConstants.DISTANCE_ALIAS);
		
		
		PreparedStatement psListing = conn.prepareStatement(sb.toString());
		if (searchMessage.advanced.getEndTime() != null){
			psListing.setTimestamp(1, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(2, searchMessage.advanced.getEndTime());
			psListing.setTimestamp(3, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(4, searchMessage.advanced.getEndTime());
			psListing.setTimestamp(5, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(6, searchMessage.advanced.getEndTime());
			
			System.out.println(searchMessage.advanced.getStartTime());
			System.out.println(searchMessage.advanced.getEndTime());
		}
		System.out.println(sb.toString());
		ResultSet rs = psListing.executeQuery();
		System.out.println("after query");
		while (rs.next()){
			System.out.println("we have search results!!!!");
			Listing listing = populateListing(rs);
			ListingResult listingResult = new ListingResult();
			listingResult.listing = listing;
			System.out.println(listing.getAddress().getFirstLine());
			listingResult.distance = rs.getDouble(rs.findColumn(DBConstants.DISTANCE_ALIAS));
			System.out.println("distance: "+listingResult.distance);
			System.out.println("lat: "+listing.getAddress().getLatitude());
			System.out.println("lat: "+listing.getAddress().getLongitude());
			results.put(listing.getListingId(),  listingResult);
		}
		
		return results;
		
	}
	
	
	public Map<Long, ListingResult> searchByCoordinates(SearchMessage searchMessage) throws SQLException{
		Map<Long, ListingResult> results = new HashMap<>();
		System.out.println("in search");
		double latitude = searchMessage.advanced.getLat();
		double longitude = searchMessage.advanced.getLon();
		
		double minLat = latitude - 5;
		double minLong = longitude - 5;
		double maxLat = latitude + 5;
		double maxLong = longitude + 5;
		
		System.out.println("minLat: "+minLat);
		System.out.println("maxLat: "+maxLat);
		System.out.println("minLon: "+minLong);
		System.out.println("maxLon: "+maxLong);
		
		StringBuilder sb = new StringBuilder("SELECT l."+DBConstants.LENDER_ID_COL+", l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LISTING_TITLE_COL+
				", l."+DBConstants.TOTAL_RATING_COL+", l."+DBConstants.NUM_RATINGS_COL+", l."+DBConstants.PRICE_PER_HR_COL+
				", c."+DBConstants.CANCELLATION_POLICY_COL+", a."+DBConstants.ADDRESS_ID_COL+", "+"a."+DBConstants.ZIP_CODE_COL+
				", a."+DBConstants.FIRST_LINE_COL+", a."+DBConstants.SECOND_LINE_COL+", a."+DBConstants.CITY_COL+", a."+DBConstants.LATITUDE_COL+", a."+DBConstants.LONGITUDE_COL+
				", a."+DBConstants.STATE_COL+", ( 3959 * acos( cos( radians("+searchMessage.advanced.getLat()+") )  * cos( radians( a."+DBConstants.LATITUDE_COL+" ) ) * "+
				"cos( radians( a."+DBConstants.LONGITUDE_COL+" ) - radians("+searchMessage.advanced.getLon()+") ) + sin( radians("+searchMessage.advanced.getLat()+") ) "
	              +"* sin( radians( a."+DBConstants.LATITUDE_COL+" ) ) ) ) AS "+DBConstants.DISTANCE_ALIAS+" FROM "+DBConstants.LISTING_TB+" l LEFT JOIN "+DBConstants.CANCELLATION_POLICY_TB+" c ON "+
				"l."+DBConstants.CANCELLATION_POLICY_ID_COL+" = c."+DBConstants.CANCELLATION_POLICY_ID_COL+
				" INNER JOIN "+DBConstants.ADDRESS_TB+" a ON l."+DBConstants.ADDRESS_ID_COL+" = a."+DBConstants.ADDRESS_ID_COL+
				(!searchMessage.advanced.getCategories().isEmpty() ? 
						" INNER JOIN "+DBConstants.LISTING_CATEGORY_TB+" lc ON l."+DBConstants.LISTING_ID_COL+" = lc."+DBConstants.LISTING_ID_COL+
						" INNER JOIN "+DBConstants.CATEGORY_TB+" ca ON ca."+DBConstants.CATEGORY_ID_COL+" = lc."+DBConstants.CATEGORY_ID_COL : "")+
				" WHERE ");
				
		if (searchMessage.advanced.getCategories() != null && !searchMessage.advanced.getCategories().isEmpty()){
			sb.append("ca."+DBConstants.CATEGORY_COL+" IN (");
			for (String cat : searchMessage.advanced.getCategories()){
				sb.append("'"+cat+"',");
			}
			
			sb.deleteCharAt(sb.length()-1);
			sb.append(") AND");
		}
		
			
		sb.append(" l."+DBConstants.PRICE_PER_HR_COL+" < "+searchMessage.advanced.getPrice()+" AND a."+DBConstants.LATITUDE_COL+" BETWEEN "+minLat+" AND "+maxLat+" AND a."+DBConstants.LONGITUDE_COL
				+" BETWEEN "+minLong+" AND "+maxLong +" HAVING "+DBConstants.DISTANCE_ALIAS+" < "+searchMessage.advanced.getDistance()
						+ " ORDER BY "+DBConstants.DISTANCE_ALIAS);
		
		System.out.println(sb.toString());
		PreparedStatement psListing = conn.prepareStatement(sb.toString());
		ResultSet rs = psListing.executeQuery();
		System.out.println("after query");
		while (rs.next()){
			System.out.println("we have search results!!!!");
			Listing listing = populateListing(rs);
			ListingResult listingResult = new ListingResult();
			listingResult.listing = listing;
			System.out.println(listing.getAddress().getFirstLine());
			listingResult.distance = rs.getDouble(rs.findColumn(DBConstants.DISTANCE_ALIAS));
			System.out.println("distance: "+listingResult.distance);
			System.out.println("lat: "+listing.getAddress().getLatitude());
			System.out.println("lat: "+listing.getAddress().getLongitude());
			results.put(listing.getListingId(),  listingResult);
		}
		
		return results;
		
	}
	
	//left out profile pic for now
	//hardcoded default role for now
	public Seeker createSeeker(Seeker seeker) throws SQLException, DBException{
		
		if (seeker.getUser_id() == 0) throw new DBException(DBException.CREATE_SEEKER+" "+DBException.INVALID_USER_ID);

		PreparedStatement psCheck = conn.prepareStatement("SELECT "+DBConstants.SEEKER_ID_COL+" FROM "+DBConstants.SEEKER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+seeker.getUser_id());
		ResultSet rsCheck = psCheck.executeQuery();
		if (rsCheck.next()) throw new DBException(DBException.CREATE_SEEKER+" "+DBException.INVALID_USER_ID);
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.SEEKER_TB+" ("+DBConstants.USER_ID_COL+", "+DBConstants.PHONE_NUM_COL+", "+
				DBConstants.IS_DEFAULT_ROLE_COL+") VALUES ("+seeker.getUser_id()+", '"+seeker.getProfile().getPhoneNumber()+"', "+true+")", Statement.RETURN_GENERATED_KEYS);
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()){
			seeker.setSeekerId(rs.getLong(1));
		}
		
		seeker.setReservations(new HashMap<>());
		seeker.setFavorites(new HashMap<>());
		return seeker;
	}
	
	//left out profile pic for now
	//hardcoded default role for now
	public Lender createLender(Lender lender) throws SQLException, DBException{
		if (lender.getUser_id() == 0) throw new DBException(DBException.CREATE_LENDER+" "+DBException.INVALID_USER_ID);
		
		PreparedStatement psCheck = conn.prepareStatement("SELECT "+DBConstants.LENDER_ID_COL+" FROM "+DBConstants.LENDER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+lender.getUser_id());
		ResultSet rsCheck = psCheck.executeQuery();
		if (rsCheck.next()) throw new DBException(DBException.CREATE_LENDER+" "+DBException.INVALID_USER_ID);
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.LENDER_TB+" ("+DBConstants.USER_ID_COL+", "+DBConstants.PHONE_NUM_COL+", "+
				DBConstants.IS_DEFAULT_ROLE_COL+") VALUES ("+lender.getUser_id()+", '"+lender.getProfile().getPhoneNumber()+"', "+true+")", Statement.RETURN_GENERATED_KEYS);
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()){
			lender.setLenderId(rs.getLong(1));
		}
		lender.setListings(new HashMap<>());
		lender.setReservations(new HashMap<>());
		return lender;
	}
	
	public void purge() throws SQLException{
		PreparedStatement ps1 = conn.prepareStatement("DELETE FROM "+DBConstants.ADDRESS_TB);
		PreparedStatement ps2 = conn.prepareStatement("DELETE FROM "+DBConstants.AVAILABILITY_TB);
		PreparedStatement ps4 = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_CATEGORY_TB);
		PreparedStatement ps9 = conn.prepareStatement("DELETE FROM "+DBConstants.RESERVATION_TB);
		PreparedStatement ps5 = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_IMAGE_TB);
		PreparedStatement ps7 = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_FAVORITES_TB);
		PreparedStatement ps6 = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_TB);
		PreparedStatement ps3 = conn.prepareStatement("DELETE FROM "+DBConstants.LENDER_TB);
		PreparedStatement ps8 = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_TB);
		PreparedStatement ps10 = conn.prepareStatement("DELETE FROM "+DBConstants.USER_TB);
	
		
		
		ps2.executeUpdate();
		ps4.executeUpdate();
		ps9.executeUpdate();
		ps5.executeUpdate();
		ps7.executeUpdate();
		ps6.executeUpdate();
		ps1.executeUpdate();
		
		ps3.executeUpdate();
		ps8.executeUpdate();
	
		ps10.executeUpdate();
	}
	
	public void addMerchantId(String merchantId, long lenderId) throws SQLException{
		System.out.println("in add merchant id in database");
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.LENDER_TB+" SET "+DBConstants.MERCHANT_ID_COL+" = '"+merchantId+"' WHERE "+
				DBConstants.LENDER_ID_COL + " = "+lenderId);
		ps.executeUpdate();
	}
	
	public void addCustomerId(String customerId, long seekerId) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.SEEKER_TB+" SET "+DBConstants.CUSTOMER_ID_COL+" = '"+customerId+"' WHERE "+
				DBConstants.LENDER_ID_COL + " = "+seekerId);
		ps.executeUpdate();
	}
	//NOT DONE
	public Listing createListing(Listing listing) throws SQLException, DBException{
		
		if (listing.getAddress() == null) throw new DBException(DBException.CREATE_LISTING+" "+DBException.INVALID_ADDRESS);
		
		System.out.println("in create listing");
		Address address = listing.getAddress();
		PreparedStatement psAddress = conn.prepareStatement("INSERT INTO "+DBConstants.ADDRESS_TB+" ("+DBConstants.FIRST_LINE_COL+", "+DBConstants.SECOND_LINE_COL+
				", "+DBConstants.ZIP_CODE_COL+", "+DBConstants.CITY_COL+", "+DBConstants.STATE_COL+", "+DBConstants.LONGITUDE_COL+", "+DBConstants.LATITUDE_COL+") VALUES ('"+address.getFirstLine()+
				"', '"+address.getSecondLine()+"', '"+address.getZipCode()+"', '"+address.getCity()+"', '"+address.getState()+"', "+address.getLongitude()+", "+address.getLatitude()+")", Statement.RETURN_GENERATED_KEYS);
		
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
			//	" WHERE "+DBConstants.CANCELLATION_POLICY_COL+" = '"+listing.getCancellationPolicy()+"'), "+listing.getPricePerHr()+", "+
				" WHERE "+DBConstants.CANCELLATION_POLICY_COL+" = '"+listing.getCancellationPolicy()+"'), "+listing.getPricePerHr()+", "+
				address.getAddressId()+")", Statement.RETURN_GENERATED_KEYS);
		
		psListing.executeUpdate();
		ResultSet rsListing = psListing.getGeneratedKeys();
		if (rsListing.next()){
			listing.setListingId(rsListing.getLong(1));
		}
		
		StringBuilder sb = new StringBuilder("INSERT INTO "+DBConstants.LISTING_CATEGORY_TB+" ("+DBConstants.LISTING_ID_COL+", "+DBConstants.CATEGORY_ID_COL+") VALUES ");
		if (listing.getCategories() != null && !listing.getCategories().isEmpty()){
			int size = listing.getCategories().size();
			int index = 1;
			for (String category : listing.getCategories()){
				if (index < size){
					sb.append(" ("+listing.getListingId()+", (SELECT "+DBConstants.CATEGORY_ID_COL+" FROM "+DBConstants.CATEGORY_TB+" WHERE "+
				DBConstants.CATEGORY_COL+" = '"+category+"')), ");
				}
				index++;
			}
			
			String lastCategory = listing.getCategories().get(size-1);
			sb.append("("+listing.getListingId()+", (SELECT "+DBConstants.CATEGORY_ID_COL+" FROM "+DBConstants.CATEGORY_TB+" WHERE "+
					DBConstants.CATEGORY_COL+" = '"+lastCategory+"'))");
			
			PreparedStatement psCategories = conn.prepareStatement(sb.toString());
			psCategories.executeUpdate();
		}
		
		
		if (listing.getAvailabilityList()!= null && !listing.getAvailabilityList().isEmpty()){
			
			
			for (ListingAvailibility av : listing.getAvailabilityList()){
				
				PreparedStatement psAv = conn.prepareStatement("INSERT INTO "+DBConstants.AVAILABILITY_TB+" ("+DBConstants.LISTING_ID_COL+
						", "+DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+") VALUES ("+listing.getListingId()+", ?, ?)", Statement.RETURN_GENERATED_KEYS);
				psAv.setTimestamp(1, av.getBeginDateTime());
				psAv.setTimestamp(2,  av.getEndDateTime());
				psAv.executeUpdate();
				ResultSet rsAv = psAv.getGeneratedKeys();
				if (rsAv.next()){
					av.setAvailabilityId(rsAv.getLong(1));
					av.setListingId(listing.getListingId());
				}
				
			}
		}
		//insert images
		return listing;
	}
	
	public void addListingComment(String comment, Long listingId, Long userId) throws SQLException{
		
		StringBuilder sb = new StringBuilder("INSERT INTO "+DBConstants.LISTING_COMMENT_TB+" ("+DBConstants.LISTING_ID_COL+", "+DBConstants.COMMENT_COL);
		sb.append(userId == null ? "" : DBConstants.USER_ID_COL);
		sb.append(") VALUES ("+listingId+", '"+comment+"'");
		sb.append(userId == null ? ")" : userId+")");
		
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		ps.executeUpdate();
	}
	
	public void createSeekerFavorite(long seekerId, long listingId) throws SQLException{
		PreparedStatement psfav = conn.prepareStatement("INSERT INTO "+DBConstants.SEEKER_FAVORITES_TB+" ("+DBConstants.SEEKER_ID_COL+", "+DBConstants.LISTING_ID_COL+") VALUES ("+
				seekerId+", "+listingId+")");
		psfav.executeUpdate();
		
	}
	
	public void deleteSeeker(long seekerId)throws SQLException{

		deleteReservation(seekerId, false);
		
		PreparedStatement psFavorites = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_FAVORITES_TB+" WHERE "+DBConstants.SEEKER_ID_COL+" = "+seekerId);
		psFavorites.executeUpdate();
		
		PreparedStatement psSeeker = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_TB+" WHERE "+DBConstants.SEEKER_ID_COL+" = "+seekerId);
		psSeeker.executeUpdate();
	}
	
	public void deleteLender(long lenderId)throws SQLException{
		
		deleteReservation(lenderId, true);
		
		PreparedStatement psListings = conn.prepareStatement("SELECT "+DBConstants.LISTING_ID_COL+" FROM "+DBConstants.LISTING_TB+" WHERE "+
				DBConstants.LENDER_ID_COL + " = "+lenderId);
		ResultSet rsListings = psListings.executeQuery();
		while (rsListings.next()){
			long listingId = rsListings.getLong(1);
			removeListing(listingId);
		}
			
		PreparedStatement psLender = conn.prepareStatement("DELETE FROM "+DBConstants.LENDER_TB+" WHERE "+DBConstants.LENDER_ID_COL+" = "+lenderId);
		psLender.executeUpdate();
	}
	
	public void deleteReservation(long id, Boolean isLender)throws SQLException{
		
		PreparedStatement psReservation = conn.prepareStatement("DELETE FROM "+DBConstants.RESERVATION_TB+" WHERE "+
		(isLender ? DBConstants.LENDER_ID_COL : DBConstants.SEEKER_ID_COL)+" = "+id);
		psReservation.executeUpdate();
	}
	
	public void deleteUser(long userId) throws SQLException{
		PreparedStatement psLender = conn.prepareStatement("SELECT "+DBConstants.LENDER_ID_COL+" FROM "+DBConstants.LENDER_TB+" WHERE "+
				DBConstants.USER_ID_COL+" = "+userId);
		ResultSet rsLender = psLender.executeQuery();
		if (rsLender.next()){
			long lenderId = rsLender.getLong(1);
			deleteLender(lenderId);
		}
		
		PreparedStatement psSeeker = conn.prepareStatement("SELECT "+DBConstants.SEEKER_ID_COL+" FROM "+DBConstants.SEEKER_TB+" WHERE "+
				DBConstants.USER_ID_COL+" = "+userId);
		ResultSet rsSeeker = psSeeker.executeQuery();
		if (rsSeeker.next()){
			long seekerId = rsSeeker.getLong(1);
			deleteSeeker(seekerId);
		}
		
		PreparedStatement psUser = conn.prepareStatement("DELETE FROM "+DBConstants.USER_TB+" WHERE "+DBConstants.USER_ID_COL+" = "+userId);
		psUser.executeUpdate();
	}
	
	public Reservation createReservation(Reservation reservation) throws SQLException, DBException{
		
		if (reservation.getLenderId() == 0 )throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_LENDER_ID);
		if ( reservation.getListingId() == 0 )throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_LISTING_ID);
		if ( reservation.getSeekerId() == 0 ) throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_SEEKER_ID);
		//if ( reservation.getListingAvailibility() == null) throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_AVAILABILITY);
		//if (reservation.getListingAvailibility().getAvailabilityId() == 0) throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_AVAILABILITY);
			
		
		//not currently inserting amount paid or transaction_id
		PreparedStatement psfav = conn.prepareStatement("INSERT INTO "+DBConstants.RESERVATION_TB+" ("+DBConstants.LISTING_ID_COL+
				", "+DBConstants.LENDER_ID_COL+", "+DBConstants.SEEKER_ID_COL+", "+DBConstants.END_DATE_TIME_COL+", "+DBConstants.BEGIN_DATE_TIME_COL+
				") VALUES ("+ reservation.getListingId()+", "+reservation.getLenderId()+", "+reservation.getSeekerId()+", ?, ? )", 
				Statement.RETURN_GENERATED_KEYS);
		psfav.setTimestamp(1, reservation.getEndDate());
		psfav.setTimestamp(2, reservation.getBeginDate());
		psfav.executeUpdate();
		ResultSet rs = psfav.getGeneratedKeys();
		if (rs.next()){
			reservation.setReservationId(rs.getLong(1));
			//reservation.set
		}
		
//		PreparedStatement psAvail = conn.prepareStatement("UPDATE "+DBConstants.AVAILABILITY_TB+" SET "+DBConstants.IS_RESERVED_COL+" = TRUE WHERE "+
//		DBConstants.LISTING_ID_COL+" = "+reservation.getListingId()+" AND ("+DBConstants.END_DATE_TIME_COL+" BETWEEN ? AND ?) OR ("+DBConstants.BEGIN_DATE_TIME_COL+" BETWEEN ? AND ?)");
//		psAvail.setTimestamp(1, reservation.getBeginDate());
//		psAvail.setTimestamp(2, reservation.getEndDate());
//		psAvail.setTimestamp(3, reservation.getBeginDate());
//		psAvail.setTimestamp(4, reservation.getEndDate());
//		psAvail.executeUpdate();
		
		return reservation;
	}
	
	public Boolean checkUserPasssword(String email, String password) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM "+DBConstants.USER_TB+" WHERE "+DBConstants.USER_EMAIL_COL+" = '"+email
				+"' AND "+DBConstants.PASSWORD_COL+" = '"+password+"'");
		
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	public void close(){
		try{
			conn.close();
		}
		catch (SQLException sqle){
			System.out.println(sqle.getMessage());
		}
		
	}

}
