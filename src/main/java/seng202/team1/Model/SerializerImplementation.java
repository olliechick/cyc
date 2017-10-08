package seng202.team1.Model;



import seng202.team1.UserAccountModel;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

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
            String userPath = Directory.USERS.directory() + user.getUserName() + USER_EXT;
            System.out.println("Saving to file: " + userPath);
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
            String userPath = Directory.USERS.directory() + userName + USER_EXT;
            FileInputStream inFile = new FileInputStream(userPath);
            ObjectInputStream ois = new ObjectInputStream(inFile);
            user = (UserAccountModel) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Permanently delete the serialized user file associated with this user name.
     *
     * @param userName The userName of the account to delete.
     */
    public static void deleteUserAccountModel(String userName){
        Path userPath = FileSystems.getDefault().getPath(Directory.USERS.directory() + userName + USER_EXT); //Directory.USERS.directory() + userName + USER_EXT;
        try {
            Files.delete(userPath);
            System.out.println("User Successfully deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
