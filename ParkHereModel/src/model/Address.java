package model;

import java.io.Serializable;

public class Address implements Serializable{
	private String firstLine;
	private String secondLine;
	private String zipCode;
	private String state;
	private String city;
	private long addressId;
	private double longitude;
	private double latitude;

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if (firstLine != null){
			sb.append("firstLine: "+firstLine+System.lineSeparator());
		}
		if (secondLine != null){
			sb.append("secondLine: "+secondLine+System.lineSeparator());
		}
		if (zipCode != null){
			sb.append("zip: "+zipCode+System.lineSeparator());
		}
		if (city != null){
			sb.append("city: "+city+System.lineSeparator());
		}
		if (state != null){
			sb.append("state: "+state+System.lineSeparator());
		}
		sb.append("longitude: "+longitude+System.lineSeparator());
		sb.append("latitude: "+latitude+System.lineSeparator());
		return sb.toString();
	}
	
	public String getFirstLine() {
		return firstLine;
	}
	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	public String getSecondLine() {
		return secondLine;
	}
	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
