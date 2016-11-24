package server;

public class DBConstants {
	//tables
	public static final String USER_TB = "user";
	public static final String SEEKER_TB = "seeker";
	public static final String LENDER_TB = "lender";
	public static final String ADDRESS_TB = "address";
	public static final String AVAILABILITY_TB = "availability";
	public static final String CANCELLATION_POLICY_TB = "cancellation_policy";
	public static final String CATEGORY_TB = "category";
	public static final String LISTING_TB = "listing";
	public static final String LISTING_CATEGORY_TB = "listing_category";
	public static final String LISTING_IMAGE_TB = "listing_image";
	public static final String RESERVATION_TB = "reservation";
	public static final String SEEKER_FAVORITES_TB = "seeker_favorites";
	public static final String LISTING_COMMENT_TB = "listing_comment";
	
	//address col
	public static final String ADDRESS_ID_COL = "address_id";
	public static final String ZIP_CODE_COL = "zip_code";
	public static final String FIRST_LINE_COL = "first_line";
	public static final String SECOND_LINE_COL = "second_line";
	public static final String CITY_COL = "city";
	public static final String STATE_COL = "state";
	public static final String LATITUDE_COL = "latitude";
	public static final String LONGITUDE_COL = "longitude";
	
	//availability
	public static final String AVAILIBILITY_ID_COL = "availability_id";
	public static final String LISTING_ID_COL = "listing_id";
	public static final String BEGIN_DATE_TIME_COL = "begin_date_time";
	public static final String END_DATE_TIME_COL = "end_date_time";
	public static final String IS_RESERVED_COL = "is_reserved";
	
	//cancellation_policy
	public static final String CANCELLATION_POLICY_COL = "cancellation_policy";
	public static final String CANCELLATION_POLICY_ID_COL = "cancellation_policy_id";
	
	//category
	public static final String CATEGORY_COL = "category";
	public static final String CATEGORY_ID_COL = "category_id";
	
	//lender
	public static final String LENDER_ID_COL = "lender_id";
	public static final String USER_ID_COL = "user_id";
	public static final String PHONE_NUM_COL = "phone_number";
	public static final String PROFILE_PIC_COL = "profile_pic";
	public static final String IS_DEFAULT_ROLE_COL = "is_default_role";
	public static final String MERCHANT_ID_COL = "merchant_id";
	
	//listing
	//LISTING_ID_COL
	//LENDER_ID_COL
	public static final String LISTING_TITLE_COL = "listing_title";
	public static final String DESCRIPTION_COL = "description";
	public static final String TOTAL_RATING_COL = "total_rating";
	public static final String NUM_RATINGS_COL = "number_of_ratings";
	//CANCELLATION_POLICY_ID_COL
	//ADDRESS_ID_COL
	public static final String PRICE_PER_HR_COL = "price_per_hr";
	
	//listing category
	//LISTING_ID_COL
	//CATEGORY_ID_COL
	
	//listing_image
	//LISTING_ID_COL
	public static final String IMAGE_COL = "image";
	
	//reservation
	public static final String RESERVATION_ID_COL = "reservation_id";
	public static final String SEEKER_ID_COL = "seeker_id";
	public static final String IS_REVIEWED_COL = "is_reviewed";
	//LENDER_ID_COL
	//LISTING_ID_COL
	//public static final String BEGIN_DATE_TIME_COL = "begin_date_time";
	//public static final String END_DATE_TIME_COL = "end_date_time";
	public static final String AMOUNT_PAID_COL = "amount_paid";
	public static final String TRANSACTION_ID_COL = "transaction_id";
	
	//seeker
	//SEEKER_ID_COL
	//USER_ID_COL
	//PHONE_NUMBER_COL
	//PROFILE_PIC_COL
	//IS_DEFAULT_ROLE_COL
	public static final String CUSTOMER_ID_COL = "customer_id";
	
	//seeker favorites
	//SEEKER_ID_COL
	//LISTING_ID_COL
	
	//user
	public static final String USER_EMAIL_COL = "user_email";
	public static final String FIRST_NAME_COL = "first_name";
	public static final String LAST_NAME_COL = "last_name";
	public static final String PASSWORD_COL = "password";
	//USER_ID_COL
	
	//search
	public static final String DISTANCE_ALIAS = "distance";
	
	//listing_comment
	//LISTING_ID_COL
	//USER_ID_COL
	public static final String COMMENT_COL = "comment";
	public static final String DELETED_COL = "deleted";
	public static final String RATING_COL = "rating";
	public static final String LISTING_IMAGE_ID = "listing_image_id";
	public static final String CATEGORIES_COL = "categories";
	
	
}
