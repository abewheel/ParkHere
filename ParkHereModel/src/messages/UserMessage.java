package messages;

import java.io.Serializable;

import model.User;

public class UserMessage extends Message implements Serializable{
		public Boolean success;
		public User user;
}
