package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Role implements Serializable{
	
    private Map<Long, Reservation> reservations;
    private Map<Long, Listing> listings;
    private Profile profile;
    private int bt_merchant_id;
    private long user_id;
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//    	reservations = (List<Reservation>) in.readObject();
//    	listings = (List<Listing>) in.readObject();
//    	profile = (Profile) in.readObject();
//    	bt_merchant_id = in.readInt();
//    	user_id = (Long) in.readLong();
//    }
//    
//    private void writeObject(ObjectOutputStream out) throws IOException{
//    	out.writeObject(reservations);
//    	out.writeObject(listings);
//    	out.writeObject(profile);
//    	out.writeInt(bt_merchant_id);
//    	out.writeLong(user_id);
//    }
    
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
