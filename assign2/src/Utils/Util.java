package Utils;

import java.io.*;

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
}