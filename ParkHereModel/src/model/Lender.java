package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Lender extends Role {

    private long lenderId;

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
