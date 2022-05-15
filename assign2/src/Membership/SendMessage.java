package Membership;

import java.lang.reflect.Member;

import Main.Store;

public class SendMessage implements Runnable {

    private String msg;

    private String header;
    private String nodeId;
    private int nodePort;

    private MembershipInfo membershipInfo;

    public SendMessage(String msg) {
        String[] msgArray = msg.split(" ");
        this.header = msgArray[0];
        this.msg = msg;
    }

    public SendMessage(String nodeId, int nodePort, MembershipInfo membershipInfo) {
        this.header = "MEMBERSHIP";
        this.nodeId = nodeId;
        this.nodePort = nodePort;
        this.membershipInfo = membershipInfo;
    }

    @Override
    public void run() {

        switch (header) {
            case "JOIN":
            case "LEAVE":
                
                Store.mcChannel.sendMessage(msg);

                // MEMBERSHIP WITH THE MOST RECENT LOGS

                //Store.mcChannel.sendMessage(msg);

            
                
                break;

            case "MEMBERSHIP":

                System.out.println("ENVIAR");
                TCPChannel.sendMessage(nodeId, nodePort, membershipInfo);

            
                break;
        
            default:
                break;
        }
        
    }
    
}
