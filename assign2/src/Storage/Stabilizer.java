package Storage;

import java.util.ArrayList;

import KVStorage.Crypt;
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

    private String getSuccessor() {
        String successor = "";
        int distance = 999;
        int hashId = Crypt.hashString(Store.nodeId)%360;
        for(String currentNodeId : Store.currentNodes) {
            int currHashId = Crypt.hashString(currentNodeId)%360;
            if (currHashId < hashId) 
                currHashId = currHashId+360; 
            if(currHashId - hashId < distance) {
                successor = currentNodeId;                 
            }
        
        }
        return successor;
    }
    
}
