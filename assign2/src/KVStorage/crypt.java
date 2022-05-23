package KVStorage;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 
class crypt {
static int h=0;// 
public static int hashString(String s) {
    return Math.floorMod(encryptSha256(s), (int) Math.pow(2, h));
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