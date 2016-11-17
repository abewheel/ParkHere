package model;
import java.io.Serializable;
public class MerchantPayment implements Serializable{
	private String dateOfBirth;
	private Address address;
	
	public Address getAddress(){
		return address;
	}
	
	public void setAddress(Address address){
		this.address = address;
	}
	
	public String getDateOfBirth(){
		return dateOfBirth;
	}
	
	public void setDateOfBirth(String DOB){
		this.dateOfBirth = DOB;
	}
}
