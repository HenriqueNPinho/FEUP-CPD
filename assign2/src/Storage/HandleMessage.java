package Storage;

import Main.Store;

public class HandleMessage {

    
    public HandleMessage(String message) {
        String[] msgSplit = message.trim().split(" ");

        if(msgSplit.length == 3) {
            proceesMsg(msgSplit[0], msgSplit[1], msgSplit[2]);
        }
        else {
            proceesMsg(msgSplit[0], msgSplit[1]);
        }

    }


    private void proceesMsg(String operation, String key) {
        switch (operation) {
            case "DELETE":
                Store.bucket.deleteKeyValue(key);
                break;
            
            case "GET":
                String value = Store.bucket.getValue(key);
                
                break;
        
            default:
                break;
        }
    }

    private void proceesMsg(String operation, String key, String value) {
        Store.bucket.addKeyValue(key, value);
    }

    
}
