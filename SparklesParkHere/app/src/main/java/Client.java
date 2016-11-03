/**
 * Created by emmalautz on 11/2/16.
 */
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread{

    private static final String IP = "localhost";
    private static final int port = 6789;

    public Client(){

        try{
            Socket s = new Socket(IP, port);
        }
        catch (IOException e){
            System.out.println("exception in client constructor "+e.getMessage());
        }

    }

}
