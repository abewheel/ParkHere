package messages;

import java.io.Serializable;

public class SeekerFavoriteMessage extends Message implements Serializable{
	public long seekerId;
	public long listingId;
	public String action;
}
