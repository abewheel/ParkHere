package model;

public class BankPayment extends MerchantPayment{
	private String routingNumber;
	private String accountNumber;
	
	public void setRoutingNumber(String routingNumber){
		this.routingNumber = routingNumber;
	}
	
	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}
	
	public String getAccountNumber(){
		return accountNumber;
	}
	
	public String getRoutingNumber(){
		return routingNumber;
	}
}
