package seng202.team1;



import java.io.*;

/**
 * Static class to serialize and deserialize the users.
 *
 * @author Josh Burt
 */
public final class SerializerImplementation {

    private final static String USER_DIR = "src/main/resources/users/";
    private final static String USER_EXT = ".user";

    /**
     * Takes a UserAccountModel object and saves them in a .ser file in the resources/tmp folder.
     * File path is /src/main/resources/tmp/{username}.ser
     * @param user UserAcccountModel user to be serialized.
     */
    public static void serializeUser(UserAccountModel user){
        try {
            FileOutputStream fileOut = new FileOutputStream(USER_DIR + user.getUserName() +
                    USER_EXT);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
            System.out.println("User successfully serialized and stored.");
        } catch (IOException e){

            System.out.println("IO exception occurred while serializing User " + user.getUserName());
        }
    }

    /**
     * Takes a userName as a string and returns the already serialized object. .ser file must be
     * in resources/tmp and named as userName.ser.
     * Throws an IOException if the .ser file cannot be found.
     * @param userName String userName to be deserialized
     * @return UserAccountModel
     * @throws IOException When .ser does not exist in USER_DIR
     * deserialized with this method.
     */
    public static UserAccountModel deserializeUser(String userName) throws IOException {
        UserAccountModel user = null;
        try {
            FileInputStream inFile = new FileInputStream(USER_DIR + userName + USER_EXT);
            ObjectInputStream ois = new ObjectInputStream(inFile);
            user = (UserAccountModel) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
