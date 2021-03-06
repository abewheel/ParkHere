package messages;
import java.io.Serializable;
import java.util.List;

import model.Listing;
import model.ListingAvailibility;
import model.ListingImage;

public class ListingMessage extends Message implements Serializable{
	public Listing listing;
	public Listing updateListing;
	public List<ListingImage> images;
	public List<ListingAvailibility> availabilities;
	public List<Long> deleteAvailabilities;
}
