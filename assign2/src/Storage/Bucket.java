package Storage;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Bucket {

    private ArrayList<String> keyValue; //key-value

    private ConcurrentHashMap<String, Integer> storedOccurrences;



    public Bucket() {
        keyValue = new ArrayList<>();
        storedOccurrences = new ConcurrentHashMap<>();
    }


    public String getValue(String key) {
        for(int i = 0; i < keyValue.size(); i++) {
            String[] key_value = keyValue.get(i).split("-");
            if(key.equals(key_value[0])) {
                return key_value[1];
            }
        }
        return null;
    }

    public void addKeyValue(String key, String value) {
        keyValue.add(key+"-"+value);
        return;
    }

    public void deleteKeyValue(String key) {
        for(String kv : keyValue) {
            String k = kv.split("-")[0];
            if(k.equals(key)) {
                keyValue.remove(kv);
                return;
            }
        }
        return;
    }

    public ArrayList<String> getKeysValues() {
        return keyValue;
    }

    public void addKeysValues(ArrayList<String> keysValues) {
        for(String kv : keysValues) {
            keyValue.add(kv);
        }
    }

    public void printBucket() {
        if(keyValue.size() == 0) 
            System.out.println("> Empty Bucket");
        for(String kv : keyValue) {
            System.out.println(kv);
        }
        return;
    }
    
}
