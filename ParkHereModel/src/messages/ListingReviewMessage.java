package messages;

import model.Reservation;

public class ListingReviewMessage extends Message{
	public Reservation reservation;
	public double rate;
	public String comment;
	public String userName;
}
