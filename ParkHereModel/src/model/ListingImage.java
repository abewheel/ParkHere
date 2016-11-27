package model;

import java.io.Serializable;

public class ListingImage implements Serializable{
	private byte[] image;
	private long listing_image_id;
//	private Boolean deleted;
	
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public long getListing_image_id() {
		return listing_image_id;
	}
	public void setListing_image_id(long listing_image_id) {
		this.listing_image_id = listing_image_id;
	}
//	public Boolean getDeleted() {
//		return deleted;
//	}
//	public void setDeleted(Boolean deleted) {
//		this.deleted = deleted;
//	}

}
