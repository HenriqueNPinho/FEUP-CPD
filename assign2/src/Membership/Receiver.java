package Membership;

import java.io.*;
import java.net.*;

import Main.Store;

public class Receiver implements Runnable {

    private int port;

    public Receiver(int port) {
        this.port = port;
    }   

    @Override
    public void run() {
    
       

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while(true) {

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                String message = reader.readLine();

                processMsg(message);;
            }
        
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
    }


    private void processMsg(String msg) {

        switch (msg) {
            case "JOIN":
                int counterAux = Store.counter+1;

                if(counterAux % 2 == 0) {
                    Store.counter += 1;

                    String message = "MEMBERSHIP " + Store.nodeId + " " + Integer.toString(Store.counter);

                    Store.executor.execute(new SendMessage(message));

                }
                
                break;


            case "LEAVE":
                int counterAux2 = Store.counter+1;
                
                if(counterAux2 % 2 != 0) {
                    Store.counter += 1;



                }
            default:
                break;
        }

    }


    
}
