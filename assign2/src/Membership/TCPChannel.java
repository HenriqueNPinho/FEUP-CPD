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

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String response = reader.readLine();

            System.out.println(response);

            objectOutputStream.close();
            output.close();
            socket.close();

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
                    
                    String msg = "JOIN "+Store.nodeId+" "+Integer.toString(Store.mcastPort)+" "+Integer.toString(Store.counter)+"\r\n\r\n";
                    Store.executor.execute(new SendMessage(msg));
                }

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();


                ObjectInputStream objectInputStream = new ObjectInputStream(input);

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                writer.println("Membership info received "+ Store.nodeId);
                
                try {                
                
                    Object objectReceived = objectInputStream.readObject();

                    if(objectReceived instanceof MembershipInfo) {
                        MembershipInfo membershipInfo = (MembershipInfo) objectReceived;
                        if (counter == 2) {

                            Store.addToLog(membershipInfo.getRecentLogs());
                        }
                        
                    }                
                
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                this.counter++;
            }

            Store.executor.scheduleWithFixedDelay(new CastMembershipInfo(Store.mcastAddr, Store.mcastPort, Store.getLogs()), 1, 1, TimeUnit.SECONDS);
            
            serverSocket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("TCP");
        }
        
    }
    
}
