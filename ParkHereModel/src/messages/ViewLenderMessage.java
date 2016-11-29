package messages;

import java.util.List;

import model.Review;

public class ViewLenderMessage extends Message{
	public long lenderId;
	public String name;
	public List<Review> allReviews;
	public double averageRating;
	public byte[] profilePicture;
}
