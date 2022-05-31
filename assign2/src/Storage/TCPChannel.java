package Storage;

import java.io.*;
import java.net.*;
import KVStorage.*;
import Main.Store;

public class TCPChannel implements Runnable {

    private int port;

    public TCPChannel(int port) {
        this.port = port;
    }

    public static String sendMessage(String nodeId, int nodePort, String message) {
        String response = "";
        try (Socket socket = new Socket(nodeId, nodePort)) {
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            
            objectOutputStream.writeObject(message);

            if(message.split(" ")[0].equals("GET")) {

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    
                response = reader.readLine();
    
                System.out.println(response);

                reader.close();
                input.close();
            }

            objectOutputStream.close();
            output.close();
            socket.close();


        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
        return response;
    }

    @Override
    public void run() {
        
        while(Store.isMember()) {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();

                ObjectInputStream objectInputStream = new ObjectInputStream(input);

                Object objectReceived = objectInputStream.readObject();

                String message = (String) objectReceived;

                processMessage(message);

                objectInputStream.close();
                input.close();
                socket.close();
                serverSocket.close();

            
        
            } catch (IOException | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

    public void processMessage(String message) {
        String[] msgSplit = message.trim().split(" ");

        switch (msgSplit[0]) {
            case "PUT":
 
                
                break;
        
            default:
                break;
        }
    }


    
}
