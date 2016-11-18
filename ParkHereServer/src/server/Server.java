package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

import action.ActionFactory;
import messages.Message;

public class Server extends Thread {
	
	private DatabaseConnector dbConn;
	private BrainTreeConnector btConn;
	private ActionFactory actionFactory;
	private static final int port = 6789;
	private ServerSocket ss = null;
	private Vector<ServerComThread> serverThreads;
	public Server(){
		dbConn = new DatabaseConnector();
		btConn = new BrainTreeConnector();
		actionFactory = new ActionFactory();
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
			actionFactory.getAction(message).execute(message, dbConn, btConn, origThread);
		}

		 catch (SQLException e) {
			System.out.println("sql excep server process message: "+e.getMessage());
		}
		catch (IOException e) {
			System.out.println("io excep in server process mess "+e.getMessage());
		} catch (DBException e) {
			System.out.println("dbexcep in server process mess "+e.getMessage());
		}
	}

}
