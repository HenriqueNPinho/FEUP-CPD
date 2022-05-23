package Storage;

import java.util.ArrayList;

import Main.Store;

public class Stabilizer implements Runnable {

    @Override
    public void run() {
        
        ArrayList<String> keyValues = Store.bucket.getKeysValues();
        ArrayList<String> currentNodes = Store.currentNodes;

        for(String node : currentNodes) {
            ArrayList<String> keysValuesToSend = new ArrayList<>();
            for(String kv : keyValues) {
                if(true) {
                    keysValuesToSend.add(kv);
                    Store.bucket.deleteKeyValue(kv.split("-")[0]);
                }
            }
            if(keysValuesToSend.size() > 0) {

                // SEND TO NODE
            }
        }

        
    }
    
}
