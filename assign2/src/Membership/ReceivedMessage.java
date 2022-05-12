package Membership;

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

            
        
            default:
                break;
        }
        
    }
}
