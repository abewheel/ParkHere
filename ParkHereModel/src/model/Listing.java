package model;
/**
 * Created by emmalautz on 10/19/16.
 */
//import android.graphics.Bitmap;
//import android.location.Address;
//import android.media.Image;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

//import javax.swing.ImageIcon;

public class Listing implements Serializable{
	//deleteable member variables
	private HashMap<Long, ListingImage> listing_images;
    private HashMap<Long, ListingAvailibility> availability_list;
    
    private Lender lender;
    private long lenderId;
    private String title;
    double price_per_hr;
    private Address address;
    
    private String description;
    private Double total_rating;
    private Integer num_ratings;
    private List<String> categories;
    private List<Review> reviews;
   // private List<String> comments;
    private String cancellationPolicy;
    private long listingId;
    private Boolean deleted;
   
    public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void setPrice_per_hr(double price_per_hr) {
		this.price_per_hr = price_per_hr;
	}

	public void setLender(Lender lender) {
		this.lender = lender;
	}
	
	public void setLenderId(long lender){
		this.lenderId = lender;
	}
	
	public long getLenderId(){
		return lenderId;
	}

	public long getListingId(){
        return listingId;
    }

    public void setListingId(long id){
        listingId = id;
    }

    public Lender getLender() {
        return lender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public double getPricePerHr(){
        return price_per_hr;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public HashMap<Long, ListingImage> getListingImages() {
        return listing_images;
    }

    public void setListingImages(HashMap<Long, ListingImage> listing_images) {
        this.listing_images = listing_images;
    }

//    public void addImage(ImageIcon image){
//        if (listing_images == null) listing_images = new ArrayList<>();
//        listing_images.add(image);
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Long, ListingAvailibility> getAvailabilityList() {
        return availability_list;
    }

    public void addAvailibility(ListingAvailibility availibility){
        availability_list.put(availibility.getAvailabilityId(), availibility);
    }

    public void setAvailabilityList(HashMap<Long, ListingAvailibility> availability_list) {
        this.availability_list = availability_list;
    }

    public double getAverageRating() {
        return num_ratings == 0 ? 0 : (total_rating)/ (double)(num_ratings);
    }

    public void setTotalRating (double total_rating) {
        this.total_rating = total_rating;
    }

    public void setNumberOfRatings(int num_ratings){
        this.num_ratings = num_ratings;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }
    
}
