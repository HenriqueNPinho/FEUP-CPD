package Main;

import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;

import Utils.*;
import KVStorage.*;

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

                
                case "PUT":
                case "GET":
                case "DELETE":
                    
                    try (Socket socket = new Socket(nodeId, nodePort)) {
                
    
                        OutputStream output = socket.getOutputStream();
                        PrintWriter writer = new PrintWriter(output, true);
                        
                        
                        if(operation.equals("PUT")) {
                            String s = Util.readFile(args[2]);
                            String key= Integer.toString(Crypt.hashString(s));
                            String value= s;
                            String message = operation + " " + key + " " + value;

                            System.out.println(key);
                        
                            writer.println(message.toString());
                        }

                        else if(operation.equals("GET")) {
                            String key = args[2];
                            String message = operation + " " + key;
                        
                            writer.println(message.toString());
                           
                            InputStream input = socket.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                            String response = reader.readLine();

                            System.out.println(response);

                            reader.close();
                            input.close();

                        }
                       

                        else if(operation.equals("DELETE")) {
                            String key = args[2];
                            String message = operation + " " + key;
                        
                            writer.println(message.toString());
                        }

                        writer.close();
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