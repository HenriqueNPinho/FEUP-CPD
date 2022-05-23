package Storage;

import java.io.*;
import java.net.*;

import Main.Store;

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
        

        switch (operation) {
            case "PUT":
                String value = header[2];
                    

                int distance = 0;
                for(String node : Store.currentNodes) {
                    int hashId = hash(node);
                    int hashKey = hash(key);

                    if(hashId >= hashKey) {
                        
                    }
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

    private int hash(String nodeId) {
        return 0;
    }

}
