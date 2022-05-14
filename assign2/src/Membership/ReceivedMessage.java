package Membership;

import java.util.*;
import java.util.concurrent.TimeUnit;

import Main.Store;

public class ReceivedMessage implements Runnable{
    
    private static String message;

    public ReceivedMessage(String msg) {

        message = msg;
    }

    @Override
    public void run() {

        String[] msgArray = message.split(" ");
        
        switch (msgArray[0]) {
            case "MEMBERSHIP":


                
                break;

            case "JOIN":
                if(!msgArray[1].equals(Store.nodeId)) {

                    for(int i = 0; i < Store.sentClusterInfo.size(); i++) {
                        if(Store.sentClusterInfo.get(i).equals(msgArray[1]))
                            return;
                    }

                    System.out.println(msgArray[1] + " " + msgArray[2]);

                    String[] logs = Store.getLogs();
                    String[] nodes = Store.getNodes();

                    Store.addLogEntry(msgArray[1], msgArray[2]);
                    
                    Random random = new Random();

                    byte[] header = "MEMBERSHIP ".getBytes();
                    //byte[] body1 = logs.
                    
                    Store.sentClusterInfo.add(msgArray[1]);
                    
                    Store.executor.schedule(new SendMessage(null), random.nextInt(), TimeUnit.MILLISECONDS);
                }


                break;
            case "LEAVE":
            
        
            default:
                break;
        }
        
    }
}
