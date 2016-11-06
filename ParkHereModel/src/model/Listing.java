package model;
/**
 * Created by emmalautz on 10/19/16.
 */
//import android.graphics.Bitmap;
//import android.location.Address;
//import android.media.Image;

//import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import javax.swing.ImageIcon;

public class Listing implements Serializable{

    private Lender lender;
    private long lenderId;
    private String title;
    double price_per_hr;
    private Address address;
   // private List<ImageIcon> listing_images;
    private String description;
    private List<ListingAvailibility> availability_list;
    private Integer total_rating;
    private Integer num_ratings;
    private List<String> categories;
    private String cancellationPolicy;
    private long listingId;
    
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//    	lender = (Lender) in.readObject();
//    	title = (String) in.readObject();
//    	price_per_hr = in.readDouble();
//    	address = (Address) in.readObject();
//    	//listing_images = (List<ImageIcon>) in.readObject();
//    	description = (String) in.readObject();
//    	availability_list = (List<ListingAvailibility>) in.readObject();
//    	total_rating = in.readInt();
//    	num_ratings = in.readInt();
//    	categories = (List<String>)in.readObject();
//    	cancellationPolicy = (String) in.readObject();
//    	listingId = in.readLong();
//    	lenderId = in.readLong();
//    }
//    
//    private void writeObject(ObjectOutputStream out) throws IOException{
//    	out.writeObject(lender);
//    	out.writeObject(title);
//    	out.writeDouble(price_per_hr);
//    	out.writeObject(address);
//    	//out.writeObject(listing_images);
//    	out.writeObject(description);
//    	out.writeObject(availability_list);
//    	out.writeInt(total_rating);
//    	out.writeInt(num_ratings);
//    	out.writeObject(categories);
//    	out.writeObject(cancellationPolicy);
//    	out.writeLong(listingId);
//    	out.writeLong(lenderId);
//    }

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

//    public List<ImageIcon> getListingImages() {
//        return listing_images;
//    }
//
//    public void setListingImages(List<ImageIcon> listing_images) {
//        this.listing_images = listing_images;
//    }
//
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
        return (total_rating)/ (num_ratings);
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
