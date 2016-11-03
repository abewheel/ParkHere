package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Address implements Serializable{
	private String firstLine;
	private String secondLine;
	private String zipCode;
	private String state;
	private String city;
	private long addressId;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
    	firstLine = (String) in.readObject();
    	secondLine = (String) in.readObject();
    	zipCode = (String) in.readObject();
    	state = (String) in.readObject();
    	city = (String) in.readObject();
    	addressId = (Long) in.readLong();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException{
    	out.writeObject(firstLine);
    	out.writeObject(secondLine);
    	out.writeObject(zipCode);
    	out.writeObject(state);
    	out.writeObject(city);
    	out.writeLong(addressId);
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
}
