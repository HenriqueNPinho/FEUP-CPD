package Membership;

import java.io.*;
import java.net.*;

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

                System.out.println(message);
            }
        
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        
        
    }
    
}
