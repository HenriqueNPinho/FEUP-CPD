package Membership;

import java.io.*;
import java.net.*;

public class TCPChannel implements Runnable {

    private int port;
    private int counter = 0;

    public TCPChannel(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {

            while(this.counter < 3) {

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                String message = reader.readLine();

                this.counter++;

            }

            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
