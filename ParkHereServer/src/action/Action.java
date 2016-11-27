package action;

import java.io.IOException;
import java.sql.SQLException;

import messages.CreateCustomerMessage;
import messages.GetClientTokenMessage;
import messages.GetListingImagesMessage;
import messages.LenderMessage;
import messages.ListingAvailabilityMessage;
import messages.ListingImageMessage;
import messages.ListingMessage;
import messages.ListingReviewMessage;
import messages.MerchantAccountMessage;
import messages.Message;
import messages.ProfilePicMessage;
import messages.ReservationMessage;
import messages.SearchMessage;
import messages.SeekerFavoriteMessage;
import messages.SeekerMessage;
import messages.UserMessage;
import server.BrainTreeConnector;
import server.DBException;
import server.DatabaseConnector;
import server.ServerComThread;

public abstract class Action {

	public abstract void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException;
}

class CreateCustomerAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		CreateCustomerMessage mess = (CreateCustomerMessage) message;
		mess.customerId = btConn.createClient(mess.user, mess.user.getLender());
		dbConn.addCustomerId(mess.customerId, mess.user.getSeeker().getSeekerId());
		comThread.sendMessage(mess);
	}
	
}

class GetClientTokenAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		GetClientTokenMessage mess = (GetClientTokenMessage) message;
		mess.clientToken = btConn.getClientTokenNoCustomerId();
		comThread.sendMessage(mess);
	}
	
}

class LenderAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		LenderMessage lenderMess = (LenderMessage) message;
		if (lenderMess.action.equals(Message.insert)){
			lenderMess.lender = dbConn.createLender(lenderMess.lender);
		}
//		else if (lenderMess.action.equals(Message.delete)){
//			System.out.println("received delete lender message");
//			dbConn.removeListing(lenderMess.lender.getLenderId());
//		}
		
		comThread.sendMessage(lenderMess);
		
	}
	
}

class ListingAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ListingMessage listMess = (ListingMessage) message;
		if (listMess.action.equals(Message.insert)){
			System.out.println("received create listing message");
			listMess.listing = dbConn.createListing(listMess);
		}
		else if (listMess.action.equals(Message.update)){
			System.out.println("received delete listing message");
			dbConn.removeListing(listMess.listing.getListingId());
		}
		else if (listMess.action.equals(Message.delete)){
			System.out.println("received delete listing message");
			dbConn.deleteListing(listMess.listing.getListingId());
		}
		
		comThread.sendMessage(listMess);
		
	}
	
}

class ListingAvailabilityAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread)
			throws IOException, SQLException, DBException {
		if (message.action.equals(Message.delete)){
			dbConn.deleteListingAvailability(((ListingAvailabilityMessage) message).availability_id);
		}
	}
	
}

class ListingReviewAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ListingReviewMessage mess = (ListingReviewMessage) message;
		//dbConn.addListingComment(mess.comment, mess.listingId, mess.userId);
		dbConn.addReview(mess);
		comThread.sendMessage(mess);
		
	}
	
}

class ReservationAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ReservationMessage resMess = (ReservationMessage) message;
		System.out.println("in reservation action");
		if (resMess.action.equals(Message.insert)){
			//String amount = "";
			System.out.println(resMess.reservation.getEndDate().toString());
			System.out.println(resMess.reservation.getBeginDate().toString());
			//LocalDate today = resMess.reservation.getEndDate().toLocalDateTime().toLocalDate();
			//LocalDate birthday = resMess.reservation.getBeginDate().toLocalDateTime().toLocalDate();
			long milliseconds1 = resMess.reservation.getBeginDate().getTime();
			  long milliseconds2 = resMess.reservation.getEndDate().getTime();

			  long diff = milliseconds2 - milliseconds1;
			//  long diffSeconds = diff / 1000;
			  long diffMinutes = diff / (60 * 1000);
			//  long diffHours = diff / (60 * 60 * 1000);
			 // long diffDays = diff / (24 * 60 * 60 * 1000);

			double hours = (double) diffMinutes / 60.0;
		//	Period p = Period.between(birthday, today);
			System.out.println("before calculating pay");
			//long p2 = ChronoUnit.DAYS.between(birthday, today);
//			double p2 = 0.5;
//			p2 = p2 * 24;
			System.out.println(hours);
			System.out.println("listing pricer per hr "+ resMess.reservation.getListing().getPricePerHr());
			double paid = (double) hours * resMess.reservation.getListing().getPricePerHr();
			System.out.println(paid);
			//sysout
			System.out.println("nonce" + resMess.nonce);
			System.out.println("lender"+resMess.reservation.getLender());
			System.out.println("merchant id"+resMess.reservation.getLender().getMerchantId());
			try{
				String id = btConn.createTransactionNoClient(resMess.nonce, resMess.reservation.getLender().getMerchantId(), Double.toString(paid));
				
				resMess.reservation = dbConn.createReservation(resMess.reservation, id);
				resMess.success = true;
			}
			catch (NullPointerException e){
				resMess.success = false;
			}
			finally{
				comThread.sendMessage(resMess);
			}
			
		}
		
		comThread.sendMessage(resMess);
	}
	
}

class SeekerFavoriteAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		SeekerFavoriteMessage mess = (SeekerFavoriteMessage) message;
		if (mess.action.equals(Message.insert)){
			dbConn.createSeekerFavorite(mess.seekerId, mess.listingId);
		}
		else{
			dbConn.deleteSeekerFavorite(mess.seekerId, mess.listingId);
		}
		
		comThread.sendMessage(mess);	
	}
	
}

class SeekerAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		SeekerMessage seekMess = (SeekerMessage) message;
		
		if (seekMess.action.equals(Message.insert)){
			seekMess.seeker = dbConn.createSeeker(seekMess.seeker);
		}
		
//		else if (seekMess.action.equals(Message.delete)){
//			System.out.println("received delete seeker message");
//			dbConn.deleteSeeker(seekMess.seeker.getSeekerId());
//		}
		comThread.sendMessage(seekMess);
		
	}
	
}

class UserAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		UserMessage mess = (UserMessage)message;
		if (mess.action.equals(Message.check_validity)){
			System.out.println("IN CORRECT MESSAGE");
			System.out.println("user: "+mess.user.getEmail()+"; pass: "+mess.user.getPassword());
			mess.success = dbConn.checkUserPasssword(mess.user.getEmail(), mess.user.getPassword());
			//return mess;
		}
		else if (mess.action.equals(Message.insert)){
			System.out.println("in server correct message");
			//System.out.println(mess.user.getName());
			System.out.println(mess.user.getEmail());
			mess.user = dbConn.createUser(mess.user);
			//return mess;
		}
		else if (mess.action.equals(Message.get)){
			mess.user = dbConn.getUser(mess.user.getEmail());
		}
		
		comThread.sendMessage(mess);
	}
	
}

class ProfilePicAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		dbConn.addProfilePic((ProfilePicMessage)message);
		comThread.sendMessage(message);
	}
	
}

class MerchantAccountAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		System.out.println("create merchant account");
		MerchantAccountMessage mess = (MerchantAccountMessage) message;
		try{
			mess.merchantId = btConn.createMerchant(mess);
			dbConn.addMerchantId(mess.merchantId, mess.lender.getLenderId());
			mess.success = true;
		}
		catch (NullPointerException e){
			mess.success = false;
		}
		finally{
			comThread.sendMessage(mess);
		}
		
	}
	
}

class SearchAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread)
			throws IOException, SQLException, DBException {
		SearchMessage searchMess = (SearchMessage) message;
	//	if (searchMess.useTimes){
			searchMess.returnListings = dbConn.searchByCoordinatesAndDate(searchMess);
	//	}
//		else{
//			searchMess.returnListings = dbConn.searchByCoordinates(searchMess);
//		}
		
		comThread.sendMessage(searchMess);
		
	}
	
}

class ListingImageAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread)
			throws IOException, SQLException, DBException {
		dbConn.deleteListingImages(((ListingImageMessage)message).imagesToDelete);
		comThread.sendMessage(message);
	}
	
}

class GetListingImagesAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread)
			throws IOException, SQLException, DBException {
		GetListingImagesMessage mess = (GetListingImagesMessage) message;
		mess.images = dbConn.getListingImages(mess.id);
		comThread.sendMessage(mess);
	}
	
}