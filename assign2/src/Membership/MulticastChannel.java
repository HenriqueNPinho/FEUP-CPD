package Membership;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import Main.Store;


public class MulticastChannel implements Runnable {

    private static InetAddress address;
    private static int port;

    public MulticastChannel(String hostName, int portN) {
        try {
            address = InetAddress.getByName(hostName);
            port = portN;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void sendMessage(byte[] msg) {
        
        try (DatagramSocket sender = new DatagramSocket()) {
            
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length, address, port);
            
            sender.send(msgPacket);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        byte[] buf = new byte[65000];
        
        
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            
            multicastSocket.joinGroup(address);
            
            while(true) {

                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                multicastSocket.receive(packet);

                byte[] bufferCopy = Arrays.copyOf(buf, packet.getLength());                

                Store.executor.execute(new ReceivedMessage(bufferCopy));
                
            }
        

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
