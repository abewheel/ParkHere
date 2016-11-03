package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Date;
/**
 * Created by emmalautz on 10/19/16.
 */

public class ListingAvailibility implements Serializable{
    private Timestamp beginDateTime;
    private Timestamp endDateTime;
    private Boolean isReserved;
    private long availabilityId;
    private long listingId;
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	beginDateTime = (Timestamp) in.readObject();
    	endDateTime = (Timestamp) in.readObject();
    	isReserved = in.readBoolean();
    	availabilityId = in.readLong();
    	listingId = in.readLong();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
    	out.writeObject(beginDateTime);
    	out.writeObject(endDateTime);
    	out.writeBoolean(isReserved);
    	out.writeLong(availabilityId);
    	out.writeLong(listingId);
    }
    public Timestamp getBeginDateTime() {
		return beginDateTime;
	}

	public void setBeginDateTime(Timestamp beginDateTime) {
		this.beginDateTime = beginDateTime;
	}

	public Timestamp getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Timestamp endDateTime) {
		this.endDateTime = endDateTime;
	}

	public long getListingId() {
		return listingId;
	}

	public void setListingId(long listingId) {
		this.listingId = listingId;
	}

	public Boolean getIsReserved() {
		return isReserved;
	}

    public void setIsReserved(Boolean isReserved){
        this.isReserved = isReserved;
    }

    public Boolean isReserved(){
        return isReserved;
    }

    public long getAvailabilityId(){
        return availabilityId;
    }

    public void setAvailabilityId(long id){
        availabilityId = id;
    }
}
