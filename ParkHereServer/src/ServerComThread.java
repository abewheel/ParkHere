import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.LenderMessage;
import messages.Message;
import model.Lender;

public class ServerComThread extends Thread{

	private Server server;
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ServerComThread(Socket s, Server server){
		this.s = s;
		this.server = server;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			System.out.println("just made oos");
			ois = new ObjectInputStream(s.getInputStream());
			System.out.println("just made ois");
			
			Message message = new LenderMessage();
			message.action = Message.insert;
			System.out.println("just made message");
			try {
				oos.writeObject(new Lender());
				oos.flush();
				System.out.println("just sent message");
			} catch (IOException e) {
				System.out.println("exception in server com thread sending msg: "+e.getMessage());
			}
		} catch (IOException e) {
			System.out.println("io exception creating streams in server com thread "+e.getMessage());
		}
		this.start();
		
	}
	
	public void run(){
		
		
		while (true){
			
			Message message = new LenderMessage();
			message.action = Message.insert;
			System.out.println("just made message");
			try {
				oos.writeObject(new Lender());
				oos.flush();
				System.out.println("just sent message");
			} catch (IOException e) {
				System.out.println("exception in server com thread sending msg: "+e.getMessage());
			}
//			try {
//				//Message message = (Message)ois.readObject();
//			} catch (ClassNotFoundException | IOException e) {
//				System.out.println("ioexception maybe in server com thread run "+e.getMessage());
//			}
			
		}
	}
}
