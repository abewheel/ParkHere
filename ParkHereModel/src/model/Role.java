package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.util.ArrayList;
import java.util.List;

public abstract class Role {
	
    private List<Reservation> reservations;
    private List<Listing> listings;
    private Profile profile;
    private int bt_merchant_id;
    private long user_id;

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    protected List<Listing> getListings() {
        return listings;
    }

    protected void addListing(Listing listing){
        if (listings == null) listings = new ArrayList<>();
        listings.add(listing);
    }

    protected void removeListing(Listing listing){
        if (listings != null) listings.remove(listing);
    }

    protected void setListings(List<Listing> listings) {
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
