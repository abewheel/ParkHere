package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Role implements Serializable{
	
    private Map<Long, Reservation> reservations;
    private Map<Long, Listing> listings;
    private Profile profile;
    private int bt_merchant_id;
    private long user_id;

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
//    	if (reservations != null){
//    		sb.append("reservations: "+System.lineSeparator());
//    		reservations.values().forEach(reservation -> sb.append(reservation.toString()));
//    	
//    	}
//    	if (listings != null){
//    		sb.append("listings: "+System.lineSeparator());
//    		listings.values().forEach(listing -> sb.append(listing.toString()));
//    	}
    	if (profile != null){
    		sb.append("profile: "+profile.toString()+ System.lineSeparator());
    	}

    	sb.append("user id: "+user_id+ System.lineSeparator());
    	return sb.toString();
    }
    
    public Map<Long, Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Map<Long, Reservation> reservations) {
        this.reservations = reservations;
    }

    protected Map<Long, Listing> getListings() {
        return listings;
    }

    protected void addListing(Listing listing){
        if (listings == null) listings = new HashMap<>();
        listings.put(listing.getListingId(), listing);
    }

    protected void removeListing(Listing listing){
        if (listings != null) listings.remove(listing);
    }

    protected void setListings(Map<Long, Listing> listings) {
        this.listings = listings;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getBt_merchant_id() {
        return bt_merchant_id;
    }

    public void setBt_merchant_id(int bt_merchant_id) {
        this.bt_merchant_id = bt_merchant_id;
    }

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}
