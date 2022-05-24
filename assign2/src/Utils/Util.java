package Utils;

import java.io.*;
import java.security.*;
import java.util.Scanner;
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
        String[] nodeIdSplit = nodeId.split(".");
        int n = Integer.parseInt(nodeIdSplit[3]);
        return 3000+n;
    }

    public static String hashData(String data) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get algorithm instance.", e);
        }

        messageDigest.update(data.getBytes());

        return new String(messageDigest.digest());
    }

    public static String readFile(String path) {
        String value = "";

        File file = new File(path);
        if (!file.exists()) {
            return value;
        }

        try (Scanner scanner = new Scanner(file)) {
            
            value = scanner.nextLine();
            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return value;
    }

}
