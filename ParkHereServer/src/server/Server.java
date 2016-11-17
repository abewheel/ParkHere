package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

import messages.CreateCustomerMessage;
import messages.GetClientTokenMessage;
import messages.LenderMessage;
import messages.ListingMessage;
import messages.MerchantAccountMessage;
import messages.Message;
import messages.ReservationMessage;
import messages.SearchMessage;
import messages.SeekerMessage;
import messages.UserMessage;

public class Server extends Thread {
	
	private DatabaseConnector dbConn;
	private BrainTreeConnector btConn;
	private static final int port = 6789;
	private ServerSocket ss = null;
	private Vector<ServerComThread> serverThreads;
	public Server(){
		dbConn = new DatabaseConnector();
		btConn = new BrainTreeConnector();
		serverThreads = new Vector<>();
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("IOException in server constructor");
		}
		this.start();
	}
	
	@Override
	public void run(){
		
		while (true){
			try {
				System.out.println("about to accept client");
				Socket s = ss.accept();
				serverThreads.addElement(new ServerComThread(s, this));
				System.out.println("client accepted");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String [] args){
		new Server();
	}
	
	public void processMessage(Message message, ServerComThread origThread){
		try {
			if (message instanceof UserMessage){
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
				
				origThread.sendMessage(mess);
			}
			else if (message instanceof ReservationMessage){
				ReservationMessage resMess = (ReservationMessage) message;
				
				if (resMess.action.equals(Message.insert)){
					resMess.reservation = dbConn.createReservation(resMess.reservation);
				}
				
				origThread.sendMessage(resMess);
				
			}
			else if (message instanceof ListingMessage){
				ListingMessage listMess = (ListingMessage) message;
				if (listMess.action.equals(Message.insert)){
					System.out.println("received create listing message");
					listMess.listing = dbConn.createListing(listMess.listing);
				}
				else if (listMess.action.equals(Message.delete)){
					System.out.println("received delete listing message");
					dbConn.removeListing(listMess.listing.getListingId());
				}
				
				origThread.sendMessage(listMess);
				
			}
			else if (message instanceof SeekerMessage){
				SeekerMessage seekMess = (SeekerMessage) message;
				
				if (seekMess.action.equals(Message.insert)){
					seekMess.seeker = dbConn.createSeeker(seekMess.seeker);
				}
				
				else if (seekMess.action.equals(Message.delete)){
					System.out.println("received delete seeker message");
					dbConn.deleteSeeker(seekMess.seeker.getSeekerId());
				}
				origThread.sendMessage(seekMess);
			}
			else if (message instanceof LenderMessage){
				LenderMessage lenderMess = (LenderMessage) message;
				if (lenderMess.action.equals(Message.insert)){
					lenderMess.lender = dbConn.createLender(lenderMess.lender);
				}
				else if (lenderMess.action.equals(Message.delete)){
					System.out.println("received delete lender message");
					dbConn.removeListing(lenderMess.lender.getLenderId());
				}
				
				origThread.sendMessage(lenderMess);
			}
			else if (message instanceof SearchMessage){
				SearchMessage searchMess = (SearchMessage) message;
				if (searchMess.useTimes){
					searchMess.returnListings = dbConn.searchByCoordinatesAndDate(searchMess);
				}
				else{
					searchMess.returnListings = dbConn.searchByCoordinates(searchMess);
				}
				
				origThread.sendMessage(searchMess);
			}
			else if (message instanceof CreateCustomerMessage){
				CreateCustomerMessage mess = (CreateCustomerMessage) message;
				mess.customerId = btConn.createClient(mess.user, mess.user.getLender());
				dbConn.addCustomerId(mess.customerId, mess.user.getSeeker().getSeekerId());
				origThread.sendMessage(mess);
				
			}
			else if (message instanceof GetClientTokenMessage){
				GetClientTokenMessage mess = (GetClientTokenMessage) message;
				mess.clientToken = btConn.getClientToken(mess.customerId);
				origThread.sendMessage(mess);
			}
			else if (message instanceof MerchantAccountMessage){
				MerchantAccountMessage mess = (MerchantAccountMessage) message;
				mess.merchantId = btConn.createMerchant(mess);
				dbConn.addMerchantId(mess.merchantId, mess.lender.getLenderId());
				origThread.sendMessage(mess);
			}

		}
//		catch (IOException ioe){
//			System.out.println("ioe in server process message: "+ioe.getMessage());
//		}
		 catch (SQLException e) {
			System.out.println("sql excep server process message: "+e.getMessage());
			//return null;
		}
		//return message;
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
