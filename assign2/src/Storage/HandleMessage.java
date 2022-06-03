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
                String kv = Store.bucket.deleteKeyValue(key);
                System.out.println("> Key-Value deleted: " + kv);
                break;
            
            case "GET":
                String value = Store.bucket.getValue(key);
                System.out.println("> Value: " + value);
                break;
        
            default:
                break;
        }
    }

    private void processMsg(String operation, String key, String value) {
        Store.bucket.addKeyValue(key, value);
        System.out.println("> Key-Value added: " + key + "-" + value);
        
    }

}
