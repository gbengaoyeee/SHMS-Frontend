package smart.home.monitor.models;

import java.security.MessageDigest;

public class User {
    public String name;
    public String email;

    public User(){
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public static String getSha256(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest()).substring(0, 8);
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

}
