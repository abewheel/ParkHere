package messages;

import model.Reservation;

public class ListingReviewMessage extends Message{
	public Reservation reservation;
	public int rate;
	public String comment;
	public Long userId;
}
