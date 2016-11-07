package messages;

import java.util.List;
import java.io.Serializable;

import model.Listing;

public class SearchMessage extends Message implements Serializable{
	public String zipcode;
	public List<Listing> returnListings;
	public List<String> categories;
}
