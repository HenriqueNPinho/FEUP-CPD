package Storage;

import java.io.*;
import java.net.*;
import KVStorage.*;
import Main.Store;
import Utils.Util;

public class ProtocolReceiver implements Runnable {

    private int port;

    public ProtocolReceiver(int port) {
        this.port = port;
    }   

    public void sendMessage(String nodeId, int nodePort, String msg) {
        
        try(Socket socket = new Socket(nodeId, nodePort)) {


            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println(msg.toString());

            writer.close();
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
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while(Store.isMember()) {
                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                String message = reader.readLine();

                processMessage(message);

            }
        
            
        } catch (IOException e) {
            e.printStackTrace();
        }
               
    }


    private void processMessage(String msg) {

        String[] header = msg.trim().split(" ");
        String operation = header[0];
        String key = header[1];
        
        String node= getNode(key);

        if(node.equals("")) 
            node = Store.nodeId;
        
        if(Store.nodeId.equals(node)){
            new HandleMessage(msg);
        }
        else{
            sendMessage(node, Util.getNodePort(node), msg);
            
        }
        
        //new HandleMessage(msg);
        
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
