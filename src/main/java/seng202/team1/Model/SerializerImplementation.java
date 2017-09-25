package seng202.team1.Model;



import seng202.team1.Model.Filename;
import seng202.team1.UserAccountModel;

import java.io.*;

/**
 * Static class to serialize and deserialize the users.
 *
 * @author Josh Burt
 */
public final class SerializerImplementation {

    private final static String USER_EXT = ".user";

    /**
     * Takes a UserAccountModel object and saves them in a .user file.
     *
     * @param user UserAccountModel user to be serialized.
     */
    public static void serializeUser(UserAccountModel user) {
        try {
            String userPath = Filename.USERS.filename() + user.getUserName() + USER_EXT;
            System.out.println("fileOut: " + userPath);
            FileOutputStream fileOut = new FileOutputStream(userPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
            System.out.println("User successfully serialized and stored.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception occurred while serializing User " + user.getUserName());
        }
    }

    /**
     * Takes a userName as a string and returns the already serialized object. .ser file must be
     * in resources/tmp and named as userName.ser.
     * Throws an IOException if the .ser file cannot be found.
     *
     * @param userName String userName to be deserialized
     * @return UserAccountModel
     * @throws IOException When .user does not exist in USER_DIR
     *                     deserialized with this method.
     */
    public static UserAccountModel deserializeUser(String userName) throws IOException {
        UserAccountModel user = null;
        try {
            String userPath = Filename.USERS.filename() + userName + USER_EXT;
            FileInputStream inFile = new FileInputStream(userPath);
            ObjectInputStream ois = new ObjectInputStream(inFile);
            user = (UserAccountModel) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
