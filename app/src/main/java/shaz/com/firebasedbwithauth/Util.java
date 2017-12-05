package shaz.com.firebasedbwithauth;

/**
 * Created by ${Shahbaz} on 18-11-2017
 */

public class Util {

    public static String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
