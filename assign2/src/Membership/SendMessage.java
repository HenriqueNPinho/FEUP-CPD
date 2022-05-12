package Membership;

import Main.Store;

public class SendMessage implements Runnable {

    private String msg;

    public SendMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        
        Store.mcChannel.sendMessage(msg);
    }
    
}
