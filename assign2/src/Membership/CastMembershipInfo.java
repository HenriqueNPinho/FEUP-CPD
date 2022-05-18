package Membership;

import java.io.*;
import java.net.*;

import Main.Store;

public class CastMembershipInfo implements Runnable {


    String[] recentEvents;
    String msg;
    private InetAddress address;
    private int port;

    public CastMembershipInfo(String hostName, int port, MembershipInfo membershipInfo) {
        try {
           
            recentEvents = membershipInfo.getRecentLogs();
            this.port = port;
            address = InetAddress.getByName(hostName);
        
        
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CastMembershipInfo(String hostName, int portN, String msg) {
        try {
            address = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        port = portN;
        this.msg = msg;
    }

    @Override
    public void run() {


        if(Store.counter % 2 == 0) {

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    } 
    
}
