import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import messages.Message;

public class Server extends Thread {
	
	DatabaseConnector dbConnection;
	private static final int port = 6789;
	private ServerSocket ss = null;
	private Vector<ServerComThread> serverThreads;
	public Server(){
		dbConnection = new DatabaseConnector();
		serverThreads = new Vector<>();
		try {
			ss = new ServerSocket(6789);
		} catch (IOException e) {
			System.out.println("IOException in server constructor");
		}

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
	
	public void processMessage(Message message){
		
	}

}
