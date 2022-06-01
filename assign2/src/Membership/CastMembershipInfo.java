package Membership;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Main.Store;

public class CastMembershipInfo implements Runnable {


    ArrayList<String> recentEvents;
    private InetAddress address;
    private int port;

    public CastMembershipInfo(String hostName, int port, ArrayList<String> recentLogs) {
        try {
           
            recentEvents = recentLogs;
            this.port = port;
            address = InetAddress.getByName(hostName);
        
        
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        
            while(Store.isMember()) {

                try (DatagramSocket sender = new DatagramSocket()) {
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(recentEvents);
                    
                    byte[] info = baos.toByteArray();
                    byte[] header = ("MEMBERSHIP \r\n\r\n").getBytes();
        
                    byte[] data = new byte[header.length + info.length];
        
                    System.arraycopy(header, 0, data, 0, header.length);
                    System.arraycopy(info, 0, data, header.length, info.length);
        
                    DatagramPacket packet = new DatagramPacket(data, data.length, this.address, this.port);
        
                    sender.send(packet);
        
                    oos.close();
                    baos.close();
                    sender.close();
                
                
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
    }
    
}
