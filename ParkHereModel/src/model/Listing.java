package model;
/**
 * Created by emmalautz on 10/19/16.
 */
//import android.graphics.Bitmap;
//import android.location.Address;
//import android.media.Image;

import java.io.Serializable;
import java.util.List;

//import javax.swing.ImageIcon;

public class Listing implements Serializable{

    private Lender lender;
    private long lenderId;
    private String title;
    double price_per_hr;
    private Address address;
    private List<byte[]> listing_images;
    private String description;
    private List<ListingAvailibility> availability_list;
    private Integer total_rating;
    private Integer num_ratings;
    private List<String> categories;
    private List<String> comments;
    private String cancellationPolicy;
    private long listingId;
   
    public void setComments(List<String> comments){
    	this.comments = comments;
    }
    
    public List<String> getComments(){
    	return comments;
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

    public List<byte[]> getListingImages() {
        return listing_images;
    }

    public void setListingImages(List<byte[]> listing_images) {
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

    public List<ListingAvailibility> getAvailabilityList() {
        return availability_list;
    }

    public void addAvailibility(ListingAvailibility availibility){
        availability_list.add(availibility);
    }

    public void setAvailabilityList(List<ListingAvailibility> availability_list) {
        this.availability_list = availability_list;
    }

    public double getAverageRating() {
        return num_ratings == 0 ? 0 : (total_rating)/ (num_ratings);
    }

    public void setTotalRating (int total_rating) {
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
