package messages;

import java.util.Map;

import model.ListingImage;

public class GetListingImagesMessage extends Message{
	public Long id;
	public Map<Long, ListingImage> images;
}
