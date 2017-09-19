package seng202.team1;



import java.io.*;

/**
 *Static class to serialize  and deserialize the users.
 *
 * @author Josh Burt
 */
public final class SerializerImplementation {

    private final static String USER_DIR = "src/main/resources/tmp/";

    /**
     * Takes a UserAccountModel object and saves them in a .ser file in the resources/tmp folder.
     * File path is /src/main/resources/tmp/{username}.ser
     * @param user UserAcccountModel user to be serialized.
     * @throws IOException When it fails to write to the file.
     */
    public static void serializeUser(UserAccountModel user) throws IOException{
        try {
            FileOutputStream fileOut = new FileOutputStream(USER_DIR + user.getUserName() +
                    ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
            System.out.println("User successfully serialized and stored in Tmp");
        } catch (IOException e){
            System.out.println("IO exception occurred while serializing User "+ user.getUserName());
        }
    }

    /**
     * Takes a userName as a string and returns the already serialized object. .ser file must be
     * in resources/tmp and named as userName.ser.
     * Throws an IOException if the .ser file cannot be found.
     * @param userName String userName to be deserialized
     * @return UserAccountModel
     * @throws IOException When .ser does not exist or is in wrong dir
     * @throws ClassNotFoundException When any object other than UserAccountModel is
     * deserialized with this method.
     */
    public static UserAccountModel deserializeUser(String userName) throws IOException,
            ClassNotFoundException{
        UserAccountModel user = null;
        try {
            FileInputStream inFile = new FileInputStream(USER_DIR + userName + ".ser");
            ObjectInputStream ois = new ObjectInputStream(inFile);
            user = (UserAccountModel)ois.readObject();
        } catch (IOException ex) {
            System.out.println("User: " + userName + " was not read. Does it Exist?");
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
