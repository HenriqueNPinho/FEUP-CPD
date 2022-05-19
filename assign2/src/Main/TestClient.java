package Main;

import java.rmi.*;
import java.rmi.registry.*;

import RMI.RMIRemote;

public class TestClient {
    
    public static String nodeId;
    public static String accessPoint;
    public static String operation;
    
    public static void main(String[] args) {
        try {
            String[] nodeAp = args[0].split(":");
            nodeId = nodeAp[0];
            accessPoint = nodeAp[1];
            operation = args[1].toUpperCase();

            Registry registry;
            registry = LocateRegistry.getRegistry(nodeId);
            RMIRemote node = (RMIRemote) registry.lookup(accessPoint);
            
            
            switch (operation) {
                case "JOIN":
                    node.join();
                    break;
                case "LEAVE":
                    node.leave();
                    break;
                
                /**
                 try (Socket socket = new Socket(nodeId, nodePort)) {
                     
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    
                    writer.println(operation.toString());
                    
                } catch (UnknownHostException ex) {
    
                    System.out.println("Server not found: " + ex.getMessage());
        
                } catch (IOException ex) {
        
                    System.out.println("I/O error: " + ex.getMessage());
                }*/
                
                case "PUT":
                
                
                   
                
                
                
                
                
                
                
                
                
                break;

            case "GET":
            
            
            

            







            
            
            

            
                break;

                case "DELETE":
                
                

                

                
                
                
                
                break;
                
                default:
                break;
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
    }