import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.LenderMessage;
import messages.Message;
import messages.UserMessage;

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

		} catch (IOException e) {
			System.out.println("io exception creating streams in server com thread "+e.getMessage());
		}
		this.start();
		
	}
	
	public void run(){
		
		while (true){
			
			try {
				Message message = (Message) ois.readObject();
				System.out.println("read message: "+message.action);

				server.processMessage(message, this);


			} catch (IOException e) {
				//System.out.println("ioe"+e.getMessage());
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("ioe"+e.getMessage());
			}
			
		}
	}
	
	public void sendMessage(Message message) throws IOException{
		//UserMessage mess = (UserMessage) message;
		//System.out.println("about to print shit");
		//System.out.println(mess.user.getName());
		//System.out.println(mess.user.getUser_id());
		oos.reset();
		oos.writeObject(message);
		//oos.writeObject("test string");
		oos.flush();
	}
}
