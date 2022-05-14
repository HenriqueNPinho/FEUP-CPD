package Membership;

import Main.Store;

public class SendMessage implements Runnable {

    private String msg;

    private String header;
    private String nodeId;
    private int nodePort;

    public SendMessage(String msg) {
        String[] msgArray = msg.split(" ");
        this.header = msgArray[0];
        this.msg = msg;
    }

    public SendMessage(String header, String nodeId, int nodePort, byte[] body, byte[] body1) {
        this.header = header;
        this.nodeId = nodeId;
        this.nodePort = nodePort;
    }

    public SendMessage(String header, byte[] body) {
        this.header = header;
    }

    @Override
    public void run() {

        switch (header) {
            case "JOIN":
            case "LEAVE":
                
                Store.mcChannel.sendMessage(msg);

                // MEMBERSHIP WITH THE MOST RECENT LOGS

                Store.mcChannel.sendMessage(msg);

            
                
                break;

            case "MEMBERSHIP":

                
                TCPChannel.sendMessage(nodeId, nodePort, null);

            
                break;
        
            default:
                break;
        }
        
    }
    
}
