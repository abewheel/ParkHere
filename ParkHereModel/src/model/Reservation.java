package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by emmalautz on 10/19/16.
 */

public class Reservation implements Serializable{

    private Lender lender;
    private Seeker seeker;
    private Listing listing;
    private ListingAvailibility listingAvailibility;
    private int pricePerHour;
    private int btTransactionId;
    private long reservationId;

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	lender = (Lender) in.readObject();
    	seeker = (Seeker) in.readObject();
    	listing = (Listing) in.readObject();
    	listingAvailibility = (ListingAvailibility) in.readObject();
    	pricePerHour = in.readInt();
    	btTransactionId = in.readInt();
    	reservationId = (Long) in.readLong();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
    	out.writeObject(lender);
    	out.writeObject(seeker);
    	out.writeObject(listing);
    	out.writeObject(listingAvailibility);
    	out.writeInt(pricePerHour);
    	out.writeInt(btTransactionId);
    	out.writeLong(reservationId);
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


    public ListingAvailibility getListingAvailibility() {
		return listingAvailibility;
	}

	public void setListingAvailibility(ListingAvailibility listingAvailibility) {
		this.listingAvailibility = listingAvailibility;
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
