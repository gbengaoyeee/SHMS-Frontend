package smart.home.monitor.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
/**
 * @Team_Name: SMARTY
 */
public class User {
    public String name;
    public String email;
    private DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    public String userHash;

    public User(){
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public void writeNewUserToDB(){
        mDB.child("users").child(User.getSha256(email)).setValue(this);
    }
    public static String getSha256(String email) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(email.getBytes());
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
