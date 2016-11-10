package server;

public class DBException extends Exception{
	
	public static String INVALID_LENDER_ID = "Invalid lender id";
	public static String INVALID_LISTING_ID = "Invalid listing id";
	public static String INVALID_SEEKER_ID = "Invalid seeker id";
	public static String INVALID_RESERVATION_ID = "Invalid reservation id";
	public static String INVALID_USER_ID = "Invalid user id";
	public static String INVALID_USER_EMAIL = "Invalid user email";
	public static String CREATE_RESERVATION = "create reservation";
	public static String GET_RESERVATION = "get reservation";
	public static String CREATE_LISTING = "create listing";
	public static String GET_LISTING = "get listing";
	public static String CREATE_LENDER = "create lender";
	public static String GET_LENDER = "get lender";
	public static String CREATE_SEEKER = "create seeker";
	public static String GET_SEEKER = "get seeker";
	public static String CREATE_USER = "create user";
	public static String GET_USER = "get user";
	public static String INVALID_ADDRESS = "invalid address";
	public static String INVALID_AVAILABILITY = "invalid availability";
	
	public DBException(String message){
		super(message);
	}
}
