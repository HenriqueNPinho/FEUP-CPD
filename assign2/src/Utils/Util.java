package Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import Main.Store;

public class Util {
    
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static int getNodePort(String nodeId) {
        String[] nodeIdSplit = nodeId.split("\\.");
        int n = Integer.parseInt(nodeIdSplit[3]);
        return 3000+n;
    }

    public static String readFile(String path) throws IOException {

        String s;
        File file = new File(path);
        Scanner reader = new Scanner(file);
        s=reader.nextLine();
       

        reader.close();

        return s;
    }

    public static String getSuccesor() {
        String successor = "";
        int distance = 999;
        int hashId = Util.hashString(Store.nodeId)%360;
        for(String currentNodeId : Store.currentNodes) {
            if(currentNodeId.equals(Store.nodeId))
                continue;
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

    public static int hashString(String s) {
        return Math.floorMod(encryptSha256(s), (int) Math.pow(2, Integer.bitCount(encryptSha256(s))));
        
    }
    
    public static int encryptSha256(String s) {
        MessageDigest digest = null;
    
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    
        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        ByteBuffer wrapped = ByteBuffer.wrap(hash);
    
        return wrapped.getInt();
    }
}
