package Storage;

import java.util.ArrayList;

import Main.Store;
import Utils.Util;

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

    private String getSuccesor() {
        String successor = "";
        int distance = 999;
        int hashId = Util.hashString(Store.nodeId)%360;
        for(String currentNodeId : Store.currentNodes) {
            int currHashId = Util.hashString(currentNodeId)%360;
            if (currHashId < hashId) 
                currHashId = currHashId+(360-hashId);
            if(currHashId - hashId < distance) {
                successor = currentNodeId;
                distance = currHashId - hashId;
            }
        }
        return successor;
    }

    private String getSuccessor(String key){

        int distance = Integer.MAX_VALUE;
        int lastDist=Integer.MAX_VALUE;
        int hashId=0;
        int hashKey=0;
        for(String node : Store.currentNodes) {
            hashId= Util.hashString(node)%360;
            System.out.println(node + ": "+hashId);
            hashKey = Integer.parseInt(key)%360;
            System.out.println(hashKey);

            if(hashId >= hashKey) {
                distance = hashId-hashKey;
                distance=Integer.min(distance, lastDist);
                lastDist = distance;
            }
        }
        
        hashId=distance+hashKey;
        /// 
        if(distance==Integer.MAX_VALUE){
            for(String node : Store.currentNodes) {
                hashId= Util.hashString(node)%360;
                distance=Integer.min(hashId,distance);
            }
        }
        
        String thisNode="";
        for(String node : Store.currentNodes){
            if(Util.hashString(node)%360==hashId){
                thisNode=node;
            }
        }

        return thisNode;
    }
    
}
