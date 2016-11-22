package messages;

import model.User;

public class CreateCustomerMessage extends Message {
	public User user;
	public String customerId;
	public Boolean success;
}
