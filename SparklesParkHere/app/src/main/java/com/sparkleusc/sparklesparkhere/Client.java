package com.sparkleusc.sparklesparkhere; /**
 * Created by emmalautz on 11/2/16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.*;
import model.*;

public class Client extends Thread{

    private static final String IP = "localhost";
    private static final int port = 6789;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(){

        try{
            Socket s = new Socket(IP, port);
            System.out.println("test");
            ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.flush();
            System.out.println("made oos");

            System.out.println("made ois");
        }
        catch (IOException e){
            System.out.println("exception in client constructor "+e.getMessage());
        }
        try{
            System.out.println("waiting for message");
            Lender message = (Lender) ois.readObject();
            System.out.println("received message; action: "+message.getClass());
//                if (message instanceof LenderMessage){
//                    System.out.println("properly reading Lender message");
//                }
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("exception in client run: "+e.getMessage());
        }
        this.start();
    }

    public void run(){
        while (true){
            try{
                System.out.println("waiting for message");
                Lender message = (Lender) ois.readObject();
                System.out.println("received message; action: "+message.getClass());
//                if (message instanceof LenderMessage){
//                    System.out.println("properly reading Lender message");
//                }
            }
            catch (IOException | ClassNotFoundException e){
                System.out.println("exception in client run: "+e.getMessage());
            }

        }
    }

    public void sendMessage(Message message){
        try{
            oos.writeObject(message);
        }
        catch (IOException e){
            System.out.println("exception in client send Message: "+e.getMessage());
        }
    }

}
