package model;

public class VenmoPayment extends MerchantPayment{
	private String email;
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
}
