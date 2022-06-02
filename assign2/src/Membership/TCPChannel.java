package Membership;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import Main.Store;
import Storage.ProtocolReceiver;

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

                    serverSocket.setSoTimeout(3000);
    
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
                        e.printStackTrace();
                    }
    
                    this.counter++;
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("TCP");
            }

        if(counter < 2) {
            
            String msg = "JOIN "+Store.nodeId+" "+Integer.toString(Store.mcastPort)+" "+Integer.toString(Store.counter)+"\r\n\r\n";
            Store.executor.execute(new SendMessage(msg));
        }

        if(counter > 2) {
            Store.executor.execute(new ProtocolReceiver(Store.storePort));
            System.out.println("> TCP Potocol Channel Open on: " + Integer.toString(Store.storePort));
            
            Store.executor.scheduleWithFixedDelay(new CastMembershipInfo(Store.mcastAddr, Store.mcastPort, Store.getLogs()), 10, 1, TimeUnit.SECONDS);
            
            Store.executor.scheduleWithFixedDelay(new SetCurrentNodes(), 1, 20, TimeUnit.SECONDS);

        }
            
        
    }
    
}
