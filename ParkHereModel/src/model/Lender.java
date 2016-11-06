package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Lender extends Role implements Serializable{

    private long lenderId;
    
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//    	lenderId = in.readLong();
//    }
//    
//    private void writeObject(ObjectOutputStream out) throws IOException{
//    	out.writeLong(lenderId);
//    }

    public long getLenderId(){
        return lenderId;
    }

    public void setLenderId(long id){
        this.lenderId = id;
    }

    @Override
    public void addListing(Listing listing){
        super.addListing(listing);
    }

    @Override
    public List<Listing> getListings() {
        return super.getListings();
    }

    @Override
    public void removeListing(Listing listing){
        super.removeListing(listing);
    }

    @Override
    public void setListings(List<Listing> listings) {
        super.setListings(listings);
    }
}
