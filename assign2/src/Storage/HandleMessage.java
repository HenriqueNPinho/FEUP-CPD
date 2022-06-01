package Storage;

import Main.Store;

public class HandleMessage {

    
    public HandleMessage(String message) {
        String[] msgSplit = message.trim().split(" ");

        if(msgSplit.length == 3) {
            processMsg(msgSplit[0], msgSplit[1], msgSplit[2]);
        }
        else {
            processMsg(msgSplit[0], msgSplit[1]);
        }

    }


    private void processMsg(String operation, String key) {
        switch (operation) {
            case "DELETE":
                Store.bucket.deleteKeyValue(key);
                break;
            
            case "GET":
                String value = Store.bucket.getValue(key);
                System.out.println(value);
                break;
        
            default:
                break;
        }
    }

    private void processMsg(String operation, String key, String value) {
        Store.bucket.addKeyValue(key, value);
    }

}
