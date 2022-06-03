package Storage;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Main.Store;

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
        return "";
    }

    public void addKeyValue(String key, String value) {
        keyValue.add(key+"-"+value);
        return;
    }

    public String deleteKeyValue(String key) {
        for(String kv : keyValue) {
            String k = kv.split("-")[0];
            if(k.equals(key)) {
                keyValue.remove(kv);
                
                return kv;
            }
        }
        return "not found";
    }

    public void deleteAll() {
        keyValue.removeAll(keyValue);
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

    public void saveBucket() {
        try {
            String filename = "Nodes/" + Store.nodeId + "/bucket.txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            PrintWriter out = new PrintWriter(filename);

            for(String kv : keyValue) {
                out.println(kv);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadBucket() {
        try {
            String filename = "Nodes/" + Store.nodeId + "/bucket.txt";
            File file = new File(filename);
            keyValue = new ArrayList<>();
            if (!file.exists()) {
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                while((line = br.readLine()) != null) {
                    keyValue.add(line);
                }
            }
            
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        } 
    }
    
}
