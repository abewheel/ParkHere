package messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import model.Listing;

public class SearchMessage extends Message implements Serializable{
	public String zipcode;
	public Map<Long, ListingResult> returnListings;
	public List<String> categories;
	public int miles;
	public double longitude;
	public double latitude; 
}
