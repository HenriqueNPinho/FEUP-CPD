package Membership;

public class MulticastChannel implements Runnable {

    private String hostName;
    private int port;

    public MulticastChannel(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}
