package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.io.Serializable;
import java.util.Map;

public class Seeker extends Role implements Serializable{

    private long seeker_id;
    private String customerId;
    
    @Override
    public String toString(){
    	String superString = super.toString();
    	superString += "seekerId: "+seeker_id;
    	return superString;
    }
    
    public String getCustomerId(){
    	return customerId;
    }
    
    public void setCustomerId(String customerId){
    	this.customerId = customerId;
    }

    public Map<Long, Listing> getFavorites(){
        return super.getListings();
    }
    
    public void setFavorites(Map<Long, Listing> listings){
    	super.setListings(listings);
    }

    public void addToFavorites(Listing listing){
        super.addListing(listing);
    }

    public void removeFromFavorites(Listing listing){
        super.removeListing(listing);
    }

    //remove Reservation?
    public long getSeekerId(){
        return seeker_id;
    }


    public void setSeekerId(long id){
        this.seeker_id = id;
    }
}
