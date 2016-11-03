package messages;

import java.io.Serializable;

import model.Lender;

public class LenderMessage extends Message implements Serializable{
	
	public Lender lender;
}
