package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearch implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int price, distance;
	private float rating;
	private List<String> categories;
    //private String startTime, endTime;
	private Timestamp startTime, endTime;
    private double lat, lon;

    public AdvancedSearch(int price, int distance, float rating, boolean covered, boolean handicapped,
                          boolean compact, boolean tandem, boolean truck, Timestamp startTime,
                          Timestamp endTime, double lat, double lon) {
        this.price = price;
        this.distance = distance;
        this.rating = rating;
        categories = new ArrayList<String>();
        if(covered) categories.add(Category.COVERED);
        if(handicapped) categories.add(Category.HANDICAPPED);
        if(compact) categories.add(Category.COMPACT);
        if(tandem) categories.add(Category.TANDEM);
        if(truck) categories.add(Category.TRUCK);
        this.startTime = startTime;
        this.endTime = endTime;
        this.lat = lat;
        this.lon = lon;
    }

    public int getPrice() {
        return price;
    }

    public int getDistance() {
        return distance;
    }

    public float getRating() {
        return rating;
    }

    public List<String> getCategories() {
    	return categories;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }
    
    public double getLat() {
    	return lat;
    }
    
    public double getLon() {
    	return lon;
    }
    
    public void setLat(double lat) {
    	this.lat = lat;
    }
    
    public void setLon(double lon) {
    	this.lon = lon;
    }
}
