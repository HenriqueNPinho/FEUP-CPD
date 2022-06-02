package Main;

import java.io.*;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;

import Utils.*;

import RMI.RMIRemote;

public class TestClient {
    
    public static String nodeId;
    public static int nodePort;
    public static String accessPoint;
    public static String operation;
    
    public static void main(String[] args) {
        try {
            String[] nodeAp = args[0].split(":");
            nodeId = nodeAp[0];
            nodePort = Integer.parseInt(nodeAp[1]);
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

                case "PRINTSTORAGE":
                    node.printStorage();
                    break;

                
                case "PUT":
                case "GET":
                case "DELETE":
                    
                    try (Socket socket = new Socket(nodeId, nodePort)) {
                
    
                        OutputStream output = socket.getOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            
                        
                        
                        
                        if(operation.equals("PUT")) {
                            String s = Util.readFile(args[2]);
                            String key= Integer.toString(Util.hashString(s));
                            String value= s;
                            String message = operation + " " + key + " " + value;

                            System.out.println(key);
                        
                            objectOutputStream.writeObject(message);
                        }

                        else if(operation.equals("GET")) {
                            String key = args[2];
                            String message = operation + " " + key;
                        
                            objectOutputStream.writeObject(message);

                        }
                       

                        else if(operation.equals("DELETE")) {
                            String key = args[2];
                            String message = operation + " " + key;
                        
                            objectOutputStream.writeObject(message);
                        }

                        objectOutputStream.close();
                        output.close();
                        socket.close();


                    } catch (UnknownHostException ex) {
        
                        System.out.println("Server not found: " + ex.getMessage());
            
                    } catch (IOException ex) {
            
                        System.out.println("I/O error: " + ex.getMessage());
                    }

                    break;
                
                default:
                break;
            }
            
            } catch (RemoteException e) {            
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }