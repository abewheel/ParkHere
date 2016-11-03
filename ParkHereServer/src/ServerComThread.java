import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Message;

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
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("io exception creating streams in server com thread "+e.getMessage());
		}
		this.start();
		
	}
	
	public void run(){
		
		while (true){
			try {
				Message message = (Message)ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("ioexception maybe in server com thread run "+e.getMessage());
			}
			
		}
	}
}
