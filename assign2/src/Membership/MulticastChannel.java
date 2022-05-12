package Membership;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
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

    public void sendMessage(String msg) {
        
        try (DatagramSocket sender = new DatagramSocket()) {

            byte[] sbuf = msg.getBytes();
            
            DatagramPacket msgPacket = new DatagramPacket(sbuf, sbuf.length, address, port);
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
                String received = new String(packet.getData());

                Store.executor.execute(new ReceivedMessage(received));
                
            }
        

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
