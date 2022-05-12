package Main;
import java.io.*;
import java.net.*;

public class TestClient {
    
    public static String nodeId;
    public static int nodePort;
    public static String operation;
    
    public static void main(String[] args) {
        String[] nodeAp = args[0].split(":");
        nodeId = nodeAp[0];
        nodePort = Integer.parseInt(nodeAp[1]);
        operation = args[1];


        switch (operation) {
            case "JOIN":
            case "LEAVE":
                try (Socket socket = new Socket(nodeId, nodePort)) {
 
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println(operation.toString());

                } catch (UnknownHostException ex) {
    
                    System.out.println("Server not found: " + ex.getMessage());
        
                } catch (IOException ex) {
        
                    System.out.println("I/O error: " + ex.getMessage());
                }
                
                break;
        
            default:
                break;
        }
    }
}