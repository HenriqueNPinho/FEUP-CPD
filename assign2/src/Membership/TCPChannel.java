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

    public static void sendMessage(String nodeId, int nodePort, MembershipInfo membershipInfo) {
        
        
        try (Socket socket = new Socket(nodeId, nodePort)) {
 
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            
            objectOutputStream.writeObject(membershipInfo);

            objectOutputStream.close();
            output.close();
            socket.close();

            System.out.println("TCP object sent");

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

    }




    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            
            while(this.counter < 3) {

                System.out.println(counter);

                if(counter > 0) {
                    System.out.println("SENT MCAST AGAIN");
                    String msg = "JOIN "+Store.nodeId+" "+Integer.toString(Store.mcastPort)+" "+Integer.toString(Store.counter);
                    Store.executor.execute(new SendMessage(msg));
                }
                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();


                ObjectInputStream objectInputStream = new ObjectInputStream(input);
                
                try {
                    MembershipInfo membershipInfo = (MembershipInfo) objectInputStream.readObject();
                    System.out.println(membershipInfo.msg);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                this.counter++;
            }


            serverSocket.close();

            /**String[] recentLogs = membershipInfo.getRecentLogs();

            for(int i = 0; i < recentLogs.length; i++) {
                Store.log.add(recentLogs[i]);
            }*/


            Store.executor.scheduleAtFixedRate(new SendMessage(new String()), 1, 1, TimeUnit.SECONDS);



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("TCP");
        }
        
    }
    
}
