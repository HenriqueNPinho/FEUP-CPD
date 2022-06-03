package Storage;

import java.util.ArrayList;

import Main.Store;
import Utils.Util;

public class Stabilizer implements Runnable {

    @Override
    public void run() {
        if(Store.isMember()) {
            
            int currD = Util.hashString(Store.nodeId) % 360;
            for(String node : Store.currentNodes) {
                
                if(node.equals(Store.nodeId))
                    continue;
                int nextD = Util.hashString(node) % 360;

                ArrayList<String> keysValuesToSend = new ArrayList<>();
 
                for(String kv : Store.bucket.getKeysValues()) {
                    String key = kv.split("-")[0];
                    int kvD = Util.hashString(key) % 360;
 
                    if(nextD > kvD) {

                        if(nextD - kvD < currD - kvD) {
                            keysValuesToSend.add(kv);
                        }
                    }
                }
                if(keysValuesToSend.size() == 0) {

                    for(String kv : Store.bucket.getKeysValues()) {
                        String key = kv.split("-")[0];
                        int kvD = Util.hashString(key) % 360;
    
                        if(nextD - kvD < currD - kvD) {
                            keysValuesToSend.add(kv);
                        }
                    }
                }
                
                if(keysValuesToSend.size() > 0) {
                    for(String kv : keysValuesToSend) {
                        String k = kv.split("-")[0];
                        Store.bucket.deleteKeyValue(k);
                    }
                    ProtocolReceiver.sendMessage(node, Util.getNodePort(node), keysValuesToSend);
                    System.out.println("> Key-Value sent to node: " + node);
                    return;
                }
            }
        }
        return;
    }
}
