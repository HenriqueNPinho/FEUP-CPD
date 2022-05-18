package Membership;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import Main.Store;
import Utils.Util;

public class ReceivedMessage implements Runnable{
    
    
    private static byte[] msgBytes;

    public ReceivedMessage(byte[] msg) {
        msgBytes = msg;
        
        
    }

    @Override
    public void run() {
        List<byte[]> headerAndBody = getHeaderAndBody();
        byte[] headerByte = headerAndBody.get(0);

        String message = new String(headerByte);
        String[] headerInfo = message.trim().split(" ");

        String header = headerInfo[0];

        switch (header) {
            case "MEMBERSHIP":

                try {
                    byte[] body = headerAndBody.get(1);

                    String[] recentEvents = (String[]) Util.deserialize(body);

                    Store.addToLog(recentEvents);



                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                break;

            case "JOIN":

                String nodeId = headerInfo[1];
                String nodePort = headerInfo[2];
                String nodeCounter = headerInfo[3];

                if(!nodeId.equals(Store.nodeId)) {

                    boolean sentMsg = false;
                    for(int i = 0; i < Store.sentClusterInfo.size(); i++) {
                        if(Store.sentClusterInfo.get(i).equals(nodeId)) {
                            System.out.println("> Membership info already sent to: "+nodeId);
                            sentMsg = true;
                        }
                    }

                    if(sentMsg) break;

                    Store.addLogEntry(nodeId, nodeCounter);

                    sendMembershipInfo(nodeId, nodePort);
                }


                break;
            case "LEAVE":

                String nodeId_ = headerInfo[1];
                String nodePort_ = headerInfo[2];
                String nodeCounter_ = headerInfo[3];


                if(!nodeId_.equals(Store.nodeId)) {
                    
                    Store.addLogEntry(nodeId_, nodeCounter_);
                    Store.printLog();
                }

            
            default:
                break;
        }
        
    }

    private void sendMembershipInfo(String nodeId, String nodePort) {
       
        String[] logs = Store.getLogs();
        String[] nodes = Store.getNodes();
        
        MembershipInfo membershipInfo = new MembershipInfo(nodes, logs);

        Store.sentClusterInfo.add(nodeId);
                    
        Random random = new Random();

        Store.executor.schedule(new SendMessage(nodeId, Integer.parseInt(nodePort), membershipInfo), random.nextInt(401), TimeUnit.MILLISECONDS);

        System.out.println("> Membership Info sent to: " + nodeId);
    }
    
    private List<byte[]> getHeaderAndBody() {

        int i;
        for (i = 0; i < msgBytes.length; i++) {
            if (msgBytes[i] == 0xD && msgBytes[i + 1] == 0xA && msgBytes[i + 2] == 0xD && msgBytes[i + 3] == 0xA) {
                break;
            }
        }
        byte[] header = Arrays.copyOfRange(msgBytes, 0, i);
        byte[] body = Arrays.copyOfRange(msgBytes, i + 4, msgBytes.length);

        List<byte[]> headerAndBody = new ArrayList<>();

        headerAndBody.add(header);
        headerAndBody.add(body);

        return headerAndBody;
    }

}
