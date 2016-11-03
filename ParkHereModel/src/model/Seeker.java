package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.util.List;

public class Seeker extends Role {

    private long seeker_id;

    public List<Listing> getFavorites(){
        return super.getListings();
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
