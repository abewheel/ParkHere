package server;
import java.sql.Blob;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.ListingMessage;
import messages.ListingReviewMessage;
import messages.ProfilePicMessage;
import messages.SearchMessage;
import messages.ViewLenderMessage;
import model.Address;
import model.Lender;
import model.Listing;
import model.ListingAvailibility;
import model.ListingImage;
import model.ListingResult;
import model.Profile;
import model.Reservation;
import model.Review;
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
		
		PreparedStatement psListing = conn.prepareStatement("SELECT l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+
		DBConstants.LENDER_ID_COL+
				", l."+DBConstants.PRICE_PER_HR_COL+", l."+DBConstants.LISTING_TITLE_COL+", l."+DBConstants.DELETED_COL+", l."+DBConstants.CATEGORIES_COL+
				", l."+DBConstants.CANCELLATION_POLICY_COL+", l."+DBConstants.ADDRESS_ID_COL+", "+"l."+DBConstants.ZIP_CODE_COL+
				", l."+DBConstants.FIRST_LINE_COL+", l."+DBConstants.SECOND_LINE_COL+", l."+DBConstants.CITY_COL+", l."+DBConstants.LATITUDE_COL+
				", l."+DBConstants.LONGITUDE_COL+
				", l."+DBConstants.STATE_COL+" FROM "+DBConstants.SEEKER_FAVORITES_TB+" sf INNER JOIN "+ 
				DBConstants.LISTING_TB+" l ON sf."+DBConstants.LISTING_ID_COL+
				" = l."+DBConstants.LISTING_ID_COL+" WHERE "+"sf."+DBConstants.SEEKER_ID_COL+" = "+seekerId);
		
		
		ResultSet rsListing = psListing.executeQuery();
		return populateListings(rsListing);
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
		
		String json = rsListing.getString(rsListing.findColumn(DBConstants.CATEGORIES_COL));
		System.out.println(json);
		if (json != null && !json.equals("")){
			Gson gson = new Gson();
			List<String> categories = gson.fromJson(json, List.class);
			listing.setCategories(categories);
		}
		
		System.out.println("before set lender id on listing");
		listing.setLenderId(rsListing.getLong(rsListing.findColumn(DBConstants.LENDER_ID_COL)));
		System.out.println("after set lender id on listing");
		listing.setAddress(address);
		listing.setCancellationPolicy(rsListing.getString(rsListing.findColumn(DBConstants.CANCELLATION_POLICY_COL)));
		System.out.println("cancellation policy retrieved: "+listing.getCancellationPolicy());
		listing.setDescription(rsListing.getString(rsListing.findColumn(DBConstants.DESCRIPTION_COL)));
		listing.setTitle(rsListing.getString(rsListing.findColumn(DBConstants.LISTING_TITLE_COL)));
		listing.setDeleted(rsListing.getBoolean(rsListing.findColumn(DBConstants.DELETED_COL)));
		listing.setListingId(rsListing.getLong(rsListing.findColumn(DBConstants.LISTING_ID_COL)));
		listing.setPrice_per_hr(rsListing.getDouble(rsListing.findColumn(DBConstants.PRICE_PER_HR_COL)));

		PreparedStatement psComments = conn.prepareStatement("SELECT * FROM "+DBConstants.LISTING_COMMENT_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listing.getListingId());
		ResultSet rsComments = psComments.executeQuery();
		double totalRating = 0;
		int numRatings = 0;
		listing.setReviews(new ArrayList<>());
		while (rsComments.next()){
			Review r = new Review();
			r.setComment(rsComments.getString(rsComments.findColumn(DBConstants.COMMENT_COL)));
			r.setRating(rsComments.getDouble(rsComments.findColumn(DBConstants.RATING_COL)));
			r.setListingId(rsComments.getLong(rsComments.findColumn(DBConstants.LISTING_ID_COL)));
			r.setFirstName(rsComments.getString(rsComments.findColumn(DBConstants.FIRST_NAME_COL)));
			numRatings++;
			totalRating += r.getRating();
			listing.getReviews().add(r);
		}
		
		listing.setTotalRating(totalRating);
		listing.setNumberOfRatings(numRatings);
		
		PreparedStatement psAvailable = conn.prepareStatement("SELECT "+DBConstants.AVAILIBILITY_ID_COL+", "+DBConstants.DELETED_COL+", "+
		DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+", "+DBConstants.IS_RESERVED_COL+" FROM "+DBConstants.AVAILABILITY_TB+
		" WHERE "+DBConstants.LISTING_ID_COL+" = "+listing.getListingId());
		System.out.println("before get listing availabilities");
		ResultSet rsAvailable = psAvailable.executeQuery();
		if (listing.getAvailabilityList() == null) listing.setAvailabilityList(new HashMap<>());
		while (rsAvailable.next()){
			ListingAvailibility av = new ListingAvailibility();
			av.setListingId(listing.getListingId());
			av.setAvailabilityId(rsAvailable.getLong(rsAvailable.findColumn(DBConstants.AVAILIBILITY_ID_COL)));
			av.setBeginDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
			av.setEndDateTime(rsAvailable.getTimestamp(rsAvailable.findColumn(DBConstants.END_DATE_TIME_COL)));
			av.setIsReserved(rsAvailable.getBoolean(rsAvailable.findColumn(DBConstants.IS_RESERVED_COL)));
			av.setDeleted(rsAvailable.getBoolean(rsAvailable.findColumn(DBConstants.DELETED_COL)));
			
			listing.getAvailabilityList().put(av.getAvailabilityId(), av);
		}
		
		//NO IMAGES
		
		return listing;
	}
	
	public Map<Long, ListingImage> getListingImages(Long id) throws SQLException{
		PreparedStatement psImages = conn.prepareStatement("SELECT * FROM "+DBConstants.LISTING_IMAGE_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+id);
		ResultSet rsImages = psImages.executeQuery();
		Map<Long, ListingImage> images = new HashMap<>();
		//listing.setListingImages(new HashMap<>());
		while (rsImages.next()){
			ListingImage listingImage = new ListingImage();
			Blob blob = rsImages.getBlob(rsImages.findColumn(DBConstants.IMAGE_COL));
			if (blob != null){
				int blobLength = (int) blob.length();  
				byte[] blobAsBytes = blob.getBytes(1, blobLength);
				listingImage.setImage(blobAsBytes);
				listingImage.setListing_image_id(rsImages.getLong(rsImages.findColumn(DBConstants.LISTING_IMAGE_ID_COL)));
				images.put(listingImage.getListing_image_id(), listingImage);
				//release the blob and free up memory. (since JDBC 4.0)
				blob.free();
			}
		}
		return images;
	}
	
	public void deleteSeekerFavorite(long seekerId, long listingId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("DELETE FROM "+DBConstants.SEEKER_FAVORITES_TB+" WHERE "+DBConstants.SEEKER_ID_COL+
				" = "+seekerId+" AND "+DBConstants.LISTING_ID_COL+" = "+listingId);
		ps.executeUpdate();
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
	
	public String getListingsSQL(){
		StringBuilder sb = new StringBuilder("SELECT l."+DBConstants.LENDER_ID_COL+", +l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+
				", l."+DBConstants.LISTING_TITLE_COL+", l."+DBConstants.PRICE_PER_HR_COL+", l."+DBConstants.DELETED_COL+", l."+DBConstants.CATEGORIES_COL+
				", l."+DBConstants.CANCELLATION_POLICY_COL+", l."+DBConstants.ADDRESS_ID_COL+", "+"l."+DBConstants.ZIP_CODE_COL+
				", l."+DBConstants.FIRST_LINE_COL+", l."+DBConstants.SECOND_LINE_COL+", l."+DBConstants.CITY_COL+
				", l."+DBConstants.STATE_COL+", l."+DBConstants.LONGITUDE_COL+", l."+DBConstants.LATITUDE_COL+" FROM "+DBConstants.LISTING_TB+" l WHERE ");
		return sb.toString();
	}
	
	public Map<Long, Listing>getLenderListings(long lenderId) throws SQLException{

		PreparedStatement psListing = conn.prepareStatement(getListingsSQL()+
				"l."+DBConstants.LENDER_ID_COL+" = "+lenderId);
		
		ResultSet rsListing = psListing.executeQuery();
		return populateListings(rsListing);
	}
	
	public void addProfilePic(ProfilePicMessage message){
		ProfilePicMessage mess = (ProfilePicMessage) message;
		
		try {
			Blob blob = new javax.sql.rowset.serial.SerialBlob(message.picture);
			String table = mess.isLender ? DBConstants.LENDER_TB : DBConstants.SEEKER_TB;
			String col = mess.isLender ? DBConstants.LENDER_ID_COL : DBConstants.SEEKER_ID_COL;
			
			PreparedStatement ps = conn.prepareStatement("UPDATE "+table+" SET "+DBConstants.PROFILE_PIC_COL+" = ? WHERE "+ col +" = "+mess.id);
			ps.setBlob(1, blob);
			ps.executeUpdate();
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}
		
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
			lender.setProfile(profile);
			Blob blob = rsLender.getBlob(rsLender.findColumn(DBConstants.PROFILE_PIC_COL));
			if (blob != null){
				int blobLength = (int) blob.length();  
				byte[] blobAsBytes = blob.getBytes(1, blobLength);
				lender.setProfilePicture(blobAsBytes);
				//release the blob and free up memory. (since JDBC 4.0)
				blob.free();
			}
			lender.setListings(getLenderListings(lender.getLenderId()));
			lender.setMerchantId(rsLender.getString(rsLender.findColumn(DBConstants.MERCHANT_ID_COL)));
			lender.setReservations(getReservations(lender.getLenderId(), true));
			user.setLender(lender);
			if (rsLender.getBoolean(rsLender.findColumn(DBConstants.IS_DEFAULT_ROLE_COL))){
				user.setCurrent_role(lender);
			}
		}
		else{
			user.setLender(null);
		}
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
			seeker.setProfile(profile);
			Blob blob = rsSeeker.getBlob(rsSeeker.findColumn(DBConstants.PROFILE_PIC_COL));
			if (blob != null){
				int blobLength = (int) blob.length();  
				byte[] blobAsBytes = blob.getBytes(1, blobLength);
				seeker.setProfilePicture(blobAsBytes);
				//release the blob and free up memory. (since JDBC 4.0)
				blob.free();
			}
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

	
	public void setReservationReviewed(long reservationId) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.RESERVATION_TB+" SET "+DBConstants.IS_REVIEWED_COL+" = TRUE WHERE "+DBConstants.RESERVATION_ID_COL+" = "+reservationId);
		ps.executeUpdate();
	}
	
	public void addReview(ListingReviewMessage mess) throws SQLException{
		setReservationReviewed(mess.reservation.getReservationId());
		if (!mess.comment.equals("")){
			addListingComment(mess.comment, mess.reservation.getListingId(), mess.userName, mess.rate);
		}

	}
	
	public Map<Long, Reservation> getReservations(long id, Boolean isLender) throws DBException{
		Map<Long, Reservation> reservations = new HashMap<>();
		System.out.println("before get reservations");
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM "+DBConstants.RESERVATION_TB+" WHERE "+
					(isLender? DBConstants.LENDER_ID_COL : DBConstants.SEEKER_ID_COL)+" = "+id);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				Reservation reservation = new Reservation();
				reservation.setBTTransactionId(rs.getString(rs.findColumn(DBConstants.TRANSACTION_ID_COL)));
				reservation.setLenderId(rs.getLong(rs.findColumn(DBConstants.LENDER_ID_COL)));
				reservation.setSeekerId(rs.getLong(rs.findColumn(DBConstants.SEEKER_ID_COL)));
				reservation.setListingId(rs.getLong(rs.findColumn(DBConstants.LISTING_ID_COL)));
				reservation.setReservationId(rs.getLong(rs.findColumn(DBConstants.RESERVATION_ID_COL)));
				reservation.setListing(getListing(reservation.getListingId()));
				reservation.setIsReviewed(rs.getBoolean(rs.findColumn(DBConstants.IS_REVIEWED_COL)));
				reservation.setBeginDate(rs.getTimestamp(rs.findColumn(DBConstants.BEGIN_DATE_TIME_COL)));
				reservation.setEndDate(rs.getTimestamp(rs.findColumn(DBConstants.END_DATE_TIME_COL)));

				reservations.put(reservation.getReservationId(), reservation);
			}
			System.out.println("after get reservations");
			return reservations;
		} catch (SQLException e) {
			throw new DBException(DBException.GET_RESERVATION + " "+e.getMessage());
		}
		
	}
	
	public Listing getListing(long listingId) throws SQLException{

		PreparedStatement psListing = conn.prepareStatement(getListingsSQL()+
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
			System.out.println(e.getMessage());
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
		
		StringBuilder sb = new StringBuilder("SELECT l."+DBConstants.LENDER_ID_COL+", lend."+DBConstants.MERCHANT_ID_COL+", l."+DBConstants.CATEGORIES_COL+
				", l."+DBConstants.LISTING_ID_COL+", l."+DBConstants.DESCRIPTION_COL+", l."+DBConstants.LISTING_TITLE_COL+
				", l."+DBConstants.PRICE_PER_HR_COL+", l."+DBConstants.DELETED_COL+
				", l."+DBConstants.CANCELLATION_POLICY_COL+", l."+DBConstants.ADDRESS_ID_COL+", l."+DBConstants.ZIP_CODE_COL+
				", l."+DBConstants.FIRST_LINE_COL+", l."+DBConstants.SECOND_LINE_COL+", l."+DBConstants.CITY_COL+", l."+DBConstants.LATITUDE_COL+", l."+
				DBConstants.LONGITUDE_COL+", l."+DBConstants.STATE_COL+", ( 3959 * acos( cos( radians("+searchMessage.advanced.getLat()+") )  * cos( radians( l."+
				DBConstants.LATITUDE_COL+" ) ) * "+"cos( radians( l."+DBConstants.LONGITUDE_COL+" ) - radians("+searchMessage.advanced.getLon()+
				") ) + sin( radians("+searchMessage.advanced.getLat()+") ) "
	              +"* sin( radians( l."+DBConstants.LATITUDE_COL+" ) ) ) ) AS "+DBConstants.DISTANCE_ALIAS+" FROM "+DBConstants.LISTING_TB+
				" l INNER JOIN "+DBConstants.LENDER_TB+" lend ON l."+DBConstants.LENDER_ID_COL+" = lend."+DBConstants.LENDER_ID_COL+" WHERE ");
		
		if (searchMessage.advanced.getEndTime() != null){
			sb.append(" EXISTS (SELECT NULL FROM "+DBConstants.AVAILABILITY_TB+" av WHERE av."+DBConstants.LISTING_ID_COL+" = "+
					" l."+DBConstants.LISTING_ID_COL+" AND av."+DBConstants.BEGIN_DATE_TIME_COL+" <= ? AND av."+DBConstants.END_DATE_TIME_COL+" >= ? AND "+
					DBConstants.DELETED_COL+" = FALSE) AND ");
			
			sb.append(" NOT EXISTS(SELECT NULL FROM "+DBConstants.RESERVATION_TB+" r WHERE r."+DBConstants.LISTING_ID_COL+" = l."+DBConstants.LISTING_ID_COL
					+" AND (r."+DBConstants.END_DATE_TIME_COL+
				" BETWEEN ? AND ?) OR (r."+DBConstants.BEGIN_DATE_TIME_COL+" BETWEEN ? AND ?)) AND ");
		}
				
		if (searchMessage.advanced.getCategories() != null && !searchMessage.advanced.getCategories().isEmpty()){
		
			for (String cat : searchMessage.advanced.getCategories()){
				sb.append("JSON_SEARCH(l."+DBConstants.CATEGORIES_COL+", 'all', '"+cat+"') IS NOT NULL AND ");
			}
		}
		
		sb.append("l."+DBConstants.PRICE_PER_HR_COL+" < "+searchMessage.advanced.getPrice()+" AND l."+DBConstants.LATITUDE_COL+" BETWEEN "+minLat+" AND "+
				maxLat+" AND l."+DBConstants.LONGITUDE_COL +" BETWEEN "+minLong+" AND "+maxLong +" HAVING "+DBConstants.DISTANCE_ALIAS+" < "+
				searchMessage.advanced.getDistance()+" AND l."+DBConstants.DELETED_COL+" = FALSE"+" ORDER BY "+DBConstants.DISTANCE_ALIAS);
		
		
		PreparedStatement psListing = conn.prepareStatement(sb.toString());
		if (searchMessage.advanced.getEndTime() != null){
			psListing.setTimestamp(1, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(2, searchMessage.advanced.getEndTime());
			psListing.setTimestamp(3, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(4, searchMessage.advanced.getEndTime());
			psListing.setTimestamp(5, searchMessage.advanced.getStartTime());
			psListing.setTimestamp(6, searchMessage.advanced.getEndTime());

		}
		

		ResultSet rs = psListing.executeQuery();

		while (rs.next()){

			Listing listing = populateListing(rs);
			ListingResult listingResult = new ListingResult();
			listingResult.listing = listing;
			Lender lender = new Lender();
			lender.setLenderId(listing.getLenderId());
			lender.setMerchantId(rs.getString(rs.findColumn(DBConstants.MERCHANT_ID_COL)));
			listing.setLender(lender);
			listingResult.distance = rs.getDouble(rs.findColumn(DBConstants.DISTANCE_ALIAS));
			results.put(listing.getListingId(),  listingResult);
		}
		
		return results;
		
	}
	
	public Listing removeListing(ListingMessage message) throws SQLException, DBException{
		Listing newListingId = createListing(message);
		
		
		long listingId = message.updateListing.getListingId();
		List<Long> toDelete = message.deleteAvailabilities;
		StringBuilder sbDelete = new StringBuilder("DELETE FROM "+DBConstants.AVAILABILITY_TB+" WHERE "+
				DBConstants.LISTING_ID_COL+" = "+listingId);
		StringBuilder ids = new StringBuilder("(");
		if (!toDelete.isEmpty()){
			sbDelete.append(" AND "+DBConstants.AVAILIBILITY_ID_COL+" NOT IN ");
			for (Long id : toDelete){
				ids.append(id+",");
			}
			ids.deleteCharAt(ids.length()-1);
			ids.append(")");
			PreparedStatement psDeleteAvails = conn.prepareStatement("UPDATE "+ DBConstants.AVAILABILITY_TB+" SET "+DBConstants.LISTING_ID_COL+" = "+newListingId.getListingId()+
					" AND "+DBConstants.DELETED_COL+" = TRUE WHERE "+DBConstants.AVAILIBILITY_ID_COL+" IN "+ids.toString());
			psDeleteAvails.executeUpdate();
		}
		
		PreparedStatement psAvailabilities = conn.prepareStatement(sbDelete.toString()+ (toDelete.isEmpty() ? "" : ids.toString()));
		
		psAvailabilities.executeUpdate();
		PreparedStatement psImages = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_IMAGE_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psImages.executeUpdate();
		PreparedStatement psSeekerFavorites = conn.prepareStatement("UPDATE "+DBConstants.SEEKER_FAVORITES_TB+" SET "+DBConstants.LISTING_ID_COL+" = "+
		newListingId.getListingId()+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psSeekerFavorites.executeUpdate();
		PreparedStatement psReservations = conn.prepareStatement("UPDATE "+DBConstants.RESERVATION_TB+" SET "+DBConstants.LISTING_ID_COL+" = "+
				newListingId.getListingId()+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psReservations.executeUpdate();
		PreparedStatement psListing = conn.prepareStatement("DELETE FROM "+DBConstants.LISTING_TB+" WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		psListing.executeUpdate();

		return newListingId;
	}
	
	public void deleteListing(long listingId) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.LISTING_TB+" SET "+DBConstants.DELETED_COL+" = TRUE WHERE "+DBConstants.LISTING_ID_COL+" = "+listingId);
		ps.executeUpdate();
		
		PreparedStatement psAvail = conn.prepareStatement("UPDATE "+DBConstants.AVAILABILITY_TB+" SET "+DBConstants.DELETED_COL+" = TRUE WHERE "+ DBConstants.LISTING_ID_COL+" = "+listingId);
		psAvail.executeUpdate();
	}
	
	
	public void deleteListingAvailability(long id) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.AVAILABILITY_TB+" SET "+DBConstants.DELETED_COL+" = TRUE WHERE "+ DBConstants.AVAILIBILITY_ID_COL+" = "+id);
		ps.executeUpdate();
	}
	
	public void deleteListingImages(List<Long> ids) throws SQLException{
		StringBuilder sb = new StringBuilder("DELETE FROM "+DBConstants.LISTING_IMAGE_TB+" WHERE "+ DBConstants.LISTING_IMAGE_ID_COL+" IN (");
		Boolean first = true;
		for (Long id : ids){
			if (first) {
				sb.append(id);
				first = false;
			}
			else sb.append(","+id);
			
		}
	
		sb.append(")");
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		ps.executeUpdate();
	}
	
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
	public Listing createListing(ListingMessage listingMess) throws SQLException, DBException{
		Listing listing = listingMess.listing;
		
		if (listing.getAddress() == null) throw new DBException(DBException.CREATE_LISTING+" "+DBException.INVALID_ADDRESS);
		
		Address address = listing.getAddress();

		Boolean haveCategories = listing.getCategories() != null && !listing.getCategories().isEmpty();
		PreparedStatement psListing = conn.prepareStatement("INSERT INTO "+DBConstants.LISTING_TB+" ("+
				DBConstants.LENDER_ID_COL+", "+
				DBConstants.LISTING_TITLE_COL+", "+
				DBConstants.DESCRIPTION_COL+", "+
				DBConstants.TOTAL_RATING_COL+", "+
				DBConstants.NUM_RATINGS_COL+", "+
				DBConstants.CANCELLATION_POLICY_COL+", "+
				DBConstants.PRICE_PER_HR_COL+", "+
				DBConstants.FIRST_LINE_COL+", "+
				DBConstants.SECOND_LINE_COL+", "+
				DBConstants.ZIP_CODE_COL+", "+
				DBConstants.CITY_COL+", "+
				DBConstants.STATE_COL+", "+
				DBConstants.LONGITUDE_COL+", "+
				DBConstants.LATITUDE_COL+
				(haveCategories ? ", "+DBConstants.CATEGORIES_COL : "" )+ 
				") VALUES ("+
				listing.getLenderId()+", '"+
				listing.getTitle()+"', '"+
				listing.getDescription()+"', 0, 0, '"+
				listing.getCancellationPolicy()+"', "+
				listing.getPricePerHr()+", '"+
				address.getFirstLine()+"', '"+
				address.getSecondLine()+"', '"+
				address.getZipCode()+"', '"+
				address.getCity()+"', '"+
				address.getState()+"', "+
				address.getLongitude()+", "+
				address.getLatitude()+ 
				(haveCategories ? ", ?" : "")+ ")", Statement.RETURN_GENERATED_KEYS);
		

		if (haveCategories){
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(listing.getCategories(), List.class);
			psListing.setString(1, json);
		}
		
		psListing.executeUpdate();
		ResultSet rsListing = psListing.getGeneratedKeys();
		if (rsListing.next()){
			listing.setListingId(rsListing.getLong(1));
		}
		
		if (listingMess.availabilities!= null && !listingMess.availabilities.isEmpty()){
			HashMap<Long, ListingAvailibility> avails = new HashMap<>();
			
			for (ListingAvailibility av : listingMess.availabilities){
				
				PreparedStatement psAv = conn.prepareStatement("INSERT INTO "+DBConstants.AVAILABILITY_TB+" ("+DBConstants.LISTING_ID_COL+
						", "+DBConstants.BEGIN_DATE_TIME_COL+", "+DBConstants.END_DATE_TIME_COL+") VALUES ("+listing.getListingId()+", ?, ?)", Statement.RETURN_GENERATED_KEYS);
				psAv.setTimestamp(1, av.getBeginDateTime());
				psAv.setTimestamp(2,  av.getEndDateTime());
				psAv.executeUpdate();
				ResultSet rsAv = psAv.getGeneratedKeys();
				if (rsAv.next()){
					av.setAvailabilityId(rsAv.getLong(1));
					av.setListingId(listing.getListingId());
					avails.put(av.getAvailabilityId(), av);
				}
				System.out.println("after avail");
			}
			
			listing.setAvailabilityList(avails);
		}
		
		if (listingMess.images!= null && !listingMess.images.isEmpty()){
			HashMap<Long, ListingImage> images = new HashMap<>();
			
			for (ListingImage listingImage : listingMess.images){
				
				PreparedStatement psImage = conn.prepareStatement("INSERT INTO "+DBConstants.LISTING_IMAGE_TB+" ("+DBConstants.IMAGE_COL+", "+DBConstants.LISTING_ID_COL+") VALUES (?, "
						+listing.getListingId()+")", Statement.RETURN_GENERATED_KEYS);
				Blob blob = new javax.sql.rowset.serial.SerialBlob(listingImage.getImage());
				psImage.setBlob(1, blob);
				psImage.executeUpdate();
				ResultSet rs = psImage.getGeneratedKeys();
				if (rs.next()){
					listingImage.setListing_image_id(rs.getLong(1));
					images.put(listingImage.getListing_image_id(), listingImage);
				}
				System.out.println("after image");
			}
			
		}

		return listing;
	}
	
	public void addListingComment(String comment, Long listingId, String firstName, double rating) throws SQLException{
		
		StringBuilder sb = new StringBuilder("INSERT INTO "+DBConstants.LISTING_COMMENT_TB+" ("+DBConstants.LISTING_ID_COL+", "+DBConstants.COMMENT_COL+", "+DBConstants.RATING_COL+", "+DBConstants.FIRST_NAME_COL);
		sb.append(") VALUES ("+listingId+", '"+comment+"', "+rating+", '"+firstName+"')");
		
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		ps.executeUpdate();
	}
	
	public void createSeekerFavorite(long seekerId, long listingId) throws SQLException{
		PreparedStatement psfav = conn.prepareStatement("INSERT INTO "+DBConstants.SEEKER_FAVORITES_TB+" ("+DBConstants.SEEKER_ID_COL+", "+DBConstants.LISTING_ID_COL+") VALUES ("+
				seekerId+", "+listingId+")");
		psfav.executeUpdate();
		
	}
	
	public Reservation createReservation(Reservation reservation, String transactionId) throws SQLException, DBException{
		
		if (reservation.getLenderId() == 0 )throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_LENDER_ID);
		if ( reservation.getListingId() == 0 )throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_LISTING_ID);
		if ( reservation.getSeekerId() == 0 ) throw new DBException(DBException.CREATE_RESERVATION+" "+DBException.INVALID_SEEKER_ID);
			
		reservation.setBTTransactionId(transactionId);
		//not currently inserting amount paid or transaction_id
		PreparedStatement psfav = conn.prepareStatement("INSERT INTO "+DBConstants.RESERVATION_TB+" ("+DBConstants.LISTING_ID_COL+
				", "+DBConstants.LENDER_ID_COL+", "+DBConstants.SEEKER_ID_COL+", "+DBConstants.END_DATE_TIME_COL+", "+DBConstants.BEGIN_DATE_TIME_COL+", "
				+DBConstants.TRANSACTION_ID_COL+") VALUES ("+ reservation.getListingId()+", "+reservation.getLenderId()+", "+reservation.getSeekerId()+", ?, ?,'"+transactionId+"' )", 
				Statement.RETURN_GENERATED_KEYS);
		psfav.setTimestamp(1, reservation.getEndDate());
		psfav.setTimestamp(2, reservation.getBeginDate());
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
	
	public void close(){
		try{
			conn.close();
		}
		catch (SQLException sqle){
			System.out.println(sqle.getMessage());
		}
		
	}

	public void addGCMToken(String registrationToken, long user_id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE "+DBConstants.USER_TB+" SET "+DBConstants.REGISTRATION_TOKEN_COL+" = ? WHERE "+
				DBConstants.USER_ID_COL+" = "+user_id);
		ps.setString(1, registrationToken);
		ps.executeUpdate();
	}

	public void getViewLenderInfo(ViewLenderMessage mess) throws SQLException {
		long lenderId = mess.lenderId;
		PreparedStatement ps = conn.prepareStatement("SELECT u."+DBConstants.FIRST_NAME_COL+", u."+DBConstants.LAST_NAME_COL+", l."+DBConstants.PROFILE_PIC_COL+" FROM "+
				DBConstants.USER_TB+" u INNER JOIN "+DBConstants.LENDER_TB+" l ON u."+DBConstants.USER_ID_COL+" = l."+DBConstants.USER_ID_COL+" WHERE l."+DBConstants.LENDER_ID_COL+" = "+lenderId);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()){
			mess.name = rs.getString(rs.findColumn(DBConstants.FIRST_NAME_COL));
			Blob blob = rs.getBlob(rs.findColumn(DBConstants.PROFILE_PIC_COL));
			if (blob != null){
				int blobLength = (int) blob.length();  
				byte[] blobAsBytes = blob.getBytes(1, blobLength);
				mess.profilePicture = blobAsBytes;
				//release the blob and free up memory. (since JDBC 4.0)
				blob.free();
			}
		}
		
		PreparedStatement psReviews = conn.prepareStatement("SELECT * FROM "+DBConstants.LISTING_COMMENT_TB+" c INNER JOIN "+DBConstants.LISTING_TB+
				" l ON c."+DBConstants.LISTING_ID_COL+" = l."+DBConstants.LISTING_ID_COL+" WHERE l."+DBConstants.LENDER_ID_COL+" = "+lenderId);
		
		double totalRating = 0;
		double numRatings = 0;
		ResultSet rsReviews = psReviews.executeQuery();
		List<Review> reviews = new ArrayList<>();
		while (rsReviews.next()){
			Review listingReview = new Review();
			listingReview.setComment(rsReviews.getString(rsReviews.findColumn(DBConstants.COMMENT_COL)));
			listingReview.setFirstName(rsReviews.getString(DBConstants.FIRST_NAME_COL));
			listingReview.setListingId(rsReviews.getLong(DBConstants.LISTING_ID_COL));
			listingReview.setRating(rsReviews.getDouble(rsReviews.findColumn(DBConstants.RATING_COL)));
			reviews.add(listingReview);
			totalRating += listingReview.getRating();
			numRatings++;
		}
		
		mess.allReviews = reviews;
		mess.averageRating = (totalRating == 0 || numRatings == 0) ? 0 : totalRating/numRatings;
	}

}
