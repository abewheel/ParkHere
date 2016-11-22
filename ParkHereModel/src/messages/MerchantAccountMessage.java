package messages;

import model.Lender;
import model.MerchantPayment;
import model.User;

public class MerchantAccountMessage extends Message{
	public MerchantPayment merchantPayment;
	public Lender lender;
	public User user;
	public String merchantId;
	public Boolean success;
}
