package Storage;

import java.io.*;
import java.net.*;
import KVStorage.*;
import Main.Store;
import Membership.SendMessage;


public class ProtocolReceiver implements Runnable {

    private int port;

    public ProtocolReceiver(int port) {
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

                processMessage(message);
            }
        
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
               
    }


    private void processMessage(String msg) {


        String[] header = msg.trim().split(" ");

        String operation = header[0];
        String key = header[1];
        
        String node= getNode(key);

        switch (operation) {
            
            case "PUT":
                String value = header[2];          
                if(Store.nodeId.equals(node)){
                    new HandleMessage(msg);
                }
                else{
                    TCPChannel.sendMessage(node, 0, msg);
                }
                break;

            case "GET":

                break;


            case "DELETE":               

                break;

                
            default:
                break;
        }


        
    }


    private String getNode(String key){

        int distance = Integer.MAX_VALUE;
        int lastDist=Integer.MAX_VALUE;
        int hashId=0;
        int hashKey=0;
        for(String node : Store.currentNodes) {
            hashId= Crypt.hashString(node)%360;
            hashKey = Integer.parseInt(key)%360;

            if(hashId >= hashKey) {
                distance = hashId-hashKey;
                distance=Integer.min(distance, lastDist);
            }
        }
        hashId=distance+hashKey;
        /// 
        if(distance==Integer.MAX_VALUE){
            for(String node : Store.currentNodes) {
                hashId= Crypt.hashString(node)%360;
                distance=Integer.min(hashId,distance);
            }
        }
      
        String thisNode="";
        for(String node : Store.currentNodes){
            if(Crypt.hashString(node)%360==hashId){
                thisNode=node;
            }
        }

        return thisNode;
    }
}
