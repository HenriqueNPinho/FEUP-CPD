package Storage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Main.Store;
import Utils.Util;

public class ProtocolReceiver implements Runnable {

    private int port;

    public ProtocolReceiver(int port) {
        this.port = port;
    }   

    public static void sendMessage(String nodeId, int nodePort, Object msg) {
        
        try(Socket socket = new Socket(nodeId, nodePort)) {

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            
            objectOutputStream.writeObject(msg);
            
        } catch (UnknownHostException ex) {
        
            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        
        while(Store.isMember()) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {

                serverSocket.setSoTimeout(1000);
                
                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();


                ObjectInputStream objectInputStream = new ObjectInputStream(input);

                try {
                    
                    Object objectReceived = objectInputStream.readObject();

                    if(objectReceived instanceof String) {
                        String message = (String) objectReceived;
                        processMessage(message);
                        
                    }   
                    else if (objectReceived instanceof ArrayList) {
                        ArrayList<String> kvs = (ArrayList<String>)objectReceived;
                        Store.bucket.addKeysValues(kvs);
                        System.out.println("> Key-value receivied from other node");
                    } 
                
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                serverSocket.close();
            } catch (SocketTimeoutException e) {
                

            } catch (IOException e) {
                e.printStackTrace();
            } 

            }
            
               
    }


    private void processMessage(String msg) {

        String[] header = msg.trim().split(" ");
        //String operation = header[0];
        String key = header[1];
        
        String node = getNode(key);

        if(node.equals("")) 
            node = Store.nodeId;

        int port = Util.getNodePort(node);
        
        if(Store.nodeId.equals(node)){
            new HandleMessage(msg);
        }
        else{
            ProtocolReceiver.sendMessage(node, port, msg);
        }
        
    }


    private String getNode(String key){

        int distance = Integer.MAX_VALUE;
        int lastDist=Integer.MAX_VALUE;
        int hashId=0;
        int hashKey=0;
        for(String node : Store.currentNodes) {
            hashId= Util.hashString(node)%360;
            hashKey = Integer.parseInt(key)%360;

            if(hashId >= hashKey) {
                distance = hashId-hashKey;
                distance=Integer.min(distance, lastDist);
                lastDist = distance;
            }
        }
        
        hashId=distance+hashKey;

        if(distance==Integer.MAX_VALUE){
            for(String node : Store.currentNodes) {
                hashId= Util.hashString(node)%360;
                distance=Integer.min(hashId,distance);
                hashId = distance;
            }
        }
        
        String thisNode="";
        for(String node : Store.currentNodes){
            if(Util.hashString(node)%360==hashId){
                thisNode=node;
            }
        }

        return thisNode;
    }
}