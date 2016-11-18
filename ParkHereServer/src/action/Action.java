package action;

import java.io.IOException;
import java.sql.SQLException;

import messages.CreateCustomerMessage;
import messages.GetClientTokenMessage;
import messages.LenderMessage;
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
		else if (lenderMess.action.equals(Message.delete)){
			System.out.println("received delete lender message");
			dbConn.removeListing(lenderMess.lender.getLenderId());
		}
		
		comThread.sendMessage(lenderMess);
		
	}
	
}

class ListingAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ListingMessage listMess = (ListingMessage) message;
		if (listMess.action.equals(Message.insert)){
			System.out.println("received create listing message");
			listMess.listing = dbConn.createListing(listMess.listing);
		}
		else if (listMess.action.equals(Message.delete)){
			System.out.println("received delete listing message");
			dbConn.removeListing(listMess.listing.getListingId());
		}
		
		comThread.sendMessage(listMess);
		
	}
	
}

class ListingReviewAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ListingReviewMessage mess = (ListingReviewMessage) message;
		dbConn.addListingComment(mess.comment, mess.listingId, mess.userId);
		comThread.sendMessage(mess);
		
	}
	
}

class ReservationAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		ReservationMessage resMess = (ReservationMessage) message;
		
		if (resMess.action.equals(Message.insert)){
			resMess.reservation = dbConn.createReservation(resMess.reservation);
		}
		
		comThread.sendMessage(resMess);
	}
	
}

class SeekerFavoriteAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread) throws IOException, SQLException, DBException {
		SeekerFavoriteMessage mess = (SeekerFavoriteMessage) message;
		dbConn.createSeekerFavorite(mess.seekerId, mess.listingId);
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
		
		else if (seekMess.action.equals(Message.delete)){
			System.out.println("received delete seeker message");
			dbConn.deleteSeeker(seekMess.seeker.getSeekerId());
		}
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
		mess.merchantId = btConn.createMerchant(mess);
		dbConn.addMerchantId(mess.merchantId, mess.lender.getLenderId());
		comThread.sendMessage(mess);
	}
	
}

class SearchAction extends Action{

	@Override
	public void execute(Message message, DatabaseConnector dbConn, BrainTreeConnector btConn, ServerComThread comThread)
			throws IOException, SQLException, DBException {
		SearchMessage searchMess = (SearchMessage) message;
		if (searchMess.useTimes){
			searchMess.returnListings = dbConn.searchByCoordinatesAndDate(searchMess);
		}
		else{
			searchMess.returnListings = dbConn.searchByCoordinates(searchMess);
		}
		
		comThread.sendMessage(searchMess);
		
	}
	
}