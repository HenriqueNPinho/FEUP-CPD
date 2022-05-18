package Membership;

import java.io.*;
import java.net.*;

import Main.Store;

public class ProtocolReceiver implements Runnable {

    private int port;

    public ProtocolReceiver(int port) {
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

                processMessage(message);
            }
        
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
    }


    private void processMessage(String msg) {


        String[] header = msg.trim().split(" ");

        String operation = header[0];
        

        switch (operation) {

            case "JOIN":
                int counterAux = Store.counter+1;

                if(counterAux % 2 == 0) {
                    Store.counter += 1;
                    
                    Store.executor.execute(new TCPChannel(Store.mcastPort));
                    
                    System.out.println("> TCP Membership Channel Open on: " + Integer.toString(Store.mcastPort));
                    
                    String message = "JOIN " + Store.nodeId + " " + Integer.toString(Store.mcastPort) + " " + Integer.toString(Store.counter) + "\r\n\r\n";
                    Store.executor.execute(new SendMessage(message));
                    
                }
                
                break;


            case "LEAVE":
                int counterAux2 = Store.counter+1;
                
                if(counterAux2 % 2 != 0) {
                    Store.counter += 1;

                    String message = "LEAVE " + Store.nodeId + " " + Integer.toString(Store.mcastPort) + " " + Integer.toString(Store.counter) + "\r\n\r\n";

                    Store.executor.execute(new SendMessage(message));
                }

                break;


            case "PUT":
                String filepath = header[1];
                String value = header[2];
                    






                break;

            case "GET":
                String key = header[1];







                break;


            case "DELETE":                    
                String key_ = header[1];








                break;

                
            default:
                break;
        }


        
    }

}
