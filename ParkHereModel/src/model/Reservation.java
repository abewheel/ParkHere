package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Reservation implements Serializable{

    private Lender lender;
    private Seeker seeker;
    private Listing listing;
   // private ListingAvailibility listingAvailibility;
    private Timestamp beginDate;
    private Timestamp endDate;
    private int pricePerHour;
    private int btTransactionId;
    private long reservationId;
    
    private long listingId;
    private long seekerId;
    private long lenderId;

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	if (lender != null){
    		sb.append("lender: "+lender.toString()+System.lineSeparator());
    	}
    	if (seeker != null){
    		sb.append("seeker: "+seeker.toString()+System.lineSeparator());
    	}
    	if (listing != null){
    		sb.append("listing: "+listing.toString()+System.lineSeparator());
    	}
    	
    	sb.append("price: "+pricePerHour+System.lineSeparator());
    	sb.append("reservationId: "+reservationId+System.lineSeparator());
    	sb.append("listingId: "+listingId+System.lineSeparator());
    	sb.append("lenderId: "+lenderId+System.lineSeparator());
    	sb.append("seekerId: "+seekerId+System.lineSeparator());
    	return sb.toString();
    }
    
    public long getLenderId(){
    	return lenderId;
    }
    
    public long getSeekerId(){
    	return seekerId;
    }
    
    public long getListingId(){
    	return listingId;
    }
    
    public void setLenderId(long id){
    	lenderId = id;
    }
    
    public void setListingId(long id){
    	listingId = id;
    }
    
    public void setSeekerId(long id){
    	seekerId = id;
    }
    
    public long getReservationId(){
        return reservationId;
    }

    public void setReservationId(long id){
        reservationId = id;
    }

    public Lender getLender() {
        return lender;
    }

    public void setLender(Lender lender) {
        this.lender = lender;
    }

    public Seeker getSeeker() {
        return seeker;
    }

    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    

	public Timestamp getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public int getBtTransactionId() {
		return btTransactionId;
	}

	public void setBtTransactionId(int bt_transaction_id) {
		this.btTransactionId = bt_transaction_id;
	}

	public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int price_per_hour) {
        this.pricePerHour = price_per_hour;
    }

    public int getBTTransactionId() {
        return btTransactionId;
    }

    public void setBTTransactionId(int bt_transaction_id) {
        this.btTransactionId = bt_transaction_id;
    }

}
