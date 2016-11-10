package messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import model.Listing;

public class SearchMessage extends Message implements Serializable{
	public String zipcode;
	public Map<Long, Listing> returnListings;
	public List<String> categories;
}
