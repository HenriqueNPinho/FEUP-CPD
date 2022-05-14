package Membership;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import Main.Store;

public class TCPChannel implements Runnable {

    private int port;
    private int counter = 0;

    public TCPChannel(int port) {
        this.port = port;
    }

    public static void sendMessage(String nodeId, int nodePort, byte[] msg) {
        
        
        try (Socket socket = new Socket(nodeId, nodePort)) {
 
            OutputStream output = socket.getOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(output);
            dataOutput.write(msg);

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

    }




    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {

            //while(this.counter < 3) {

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                String message = reader.readLine();

                System.out.println(message);

                this.counter++;


                serverSocket.close();

            //}

            Store.executor.scheduleAtFixedRate(new SendMessage(null), 1, 1, TimeUnit.SECONDS);



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
