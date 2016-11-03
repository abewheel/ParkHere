package messages;
import java.io.Serializable;

import model.Listing;

public class ListingMessage extends Message implements Serializable{
	public Listing listing;
}
