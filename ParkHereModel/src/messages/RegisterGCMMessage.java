package messages;

import model.User;

public class RegisterGCMMessage extends Message {
	public User user;
	public String registrationToken;
}