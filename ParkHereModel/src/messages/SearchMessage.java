package messages;

import java.io.Serializable;
import java.util.Map;

import model.AdvancedSearch;
import model.ListingResult;

public class SearchMessage extends Message implements Serializable{
	public Map<Long, ListingResult> returnListings;
	public AdvancedSearch advanced;
	public Boolean useTimes;
}
