package Membership;

import java.util.*;
import java.util.concurrent.TimeUnit;

import Main.Store;

public class ReceivedMessage implements Runnable{
    
    
    private static String header;
    private static String nodeId;
    private static String nodeCounter;
    private static String nodePort;

    public ReceivedMessage(String msg) {
        String[] msgSplit = msg.split(" ");
        header = msgSplit[0];
        nodeId = msgSplit[1];
        nodePort = msgSplit[2];
        nodeCounter = msgSplit[3];
    }

    @Override
    public void run() {
        
        switch (header) {
            case "MEMBERSHIP":


                
                break;

            case "JOIN":
                if(!nodeId.equals(Store.nodeId)) {

                    /**for(int i = 0; i < Store.sentClusterInfo.size(); i++) {
                        if(Store.sentClusterInfo.get(i).equals(nodeId)) {
                            System.out.println("ALready sent msg");
                            return;
                        }
                    }*/

                    System.out.println(nodeId + " " + nodeCounter);

                    sendMembershipInfo();
                }


                break;
            case "LEAVE":

                if(!nodeId.equals(Store.nodeId)) {

                    Store.addLogEntry(nodeId, nodeCounter);
                }

            
            default:
                break;
        }
        
    }

    private void sendMembershipInfo() {
        String[] logs = Store.getLogs();
        String[] nodes = Store.getNodes();
        System.out.println("GETTING MEMbership info");
        
        MembershipInfo membershipInfo = new MembershipInfo(nodes, logs);

        Store.addLogEntry(nodeId, nodeCounter);
                    
        Random random = new Random();

        Store.sentClusterInfo.add(nodeId);

        System.out.println("TCP msg sent");
        Store.executor.schedule(new SendMessage(nodeId, Integer.parseInt(nodePort), membershipInfo), random.nextInt(401), TimeUnit.MILLISECONDS);
    }
}
