package model;
/**
 * Created by emmalautz on 10/19/16.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Seeker extends Role implements Serializable{

    private long seeker_id;
    
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//    	seeker_id = (Long) in.readLong();
//    }
//    
//    private void writeObject(ObjectOutputStream out) throws IOException{
//    	out.writeLong(seeker_id);
//    }

    public List<Listing> getFavorites(){
        return super.getListings();
    }
    
    public void setFavorites(List<Listing> listings){
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
