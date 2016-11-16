package model;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Lender extends Role implements Serializable{

    private long lenderId;
    private String merchantId;
    
    @Override
    public String toString(){
    	String superString = super.toString();
    	superString += "lenderId: "+lenderId;
    	return superString;
    }
    
    public void setMerchantId(String merchantId){
    	this.merchantId = merchantId;
    }
    
    public String getMerchantId(){
    	return merchantId;
    }

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
    public Map<Long, Listing> getListings() {
        return super.getListings();
    }

    @Override
    public void removeListing(Listing listing){
        super.removeListing(listing);
    }

    @Override
    public void setListings(Map<Long,Listing> listings) {
        super.setListings(listings);
    }
}
