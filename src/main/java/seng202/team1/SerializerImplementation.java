package seng202.team1;



import java.io.*;

/**
 *Static class to serialize  and deserialize the users.
 *
 * @author Josh Burt
 */
public final class SerializerImplementation {

    /**
     * Takes a UserAccountModel object and saves them in a .ser file in the resources/tmp folder.
     * File path is /src/main/resources/tmp/{username}.ser
     * @param user UserAcccountModel user to be serialized.
     * @throws IOException
     */
    public static void serializeUser(UserAccountModel user) throws IOException{
        try {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/tmp/"+user.getUserName()+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
            System.out.println("User successfully serialized and stored in Tmp");
        } catch (IOException e){
            System.out.println("IO exception has occured while serializing User: "+ user.getUserName());
        }
    }

    /**
     * Takes a userName as a string and returns the already serialized object. .ser file must be in resources/tmp and named
     * as userName.ser. Throws and IOException if the .ser file cannot be found.
     * @param userName String userName to be deserialized
     * @return UserAccountModel
     * @throws IOException When .ser doesnot exist or is in wrong dir
     * @throws ClassNotFoundException When anyother object other than UserAccountModel is deserialized with this method.
     */
    public static UserAccountModel deserializeUser(String userName) throws IOException{
        UserAccountModel user = null;
        try {
            FileInputStream inFile = new FileInputStream("src/main/resources/tmp/" + userName + ".ser");
            ObjectInputStream ois = new ObjectInputStream(inFile);
            user = (UserAccountModel)ois.readObject();
        } catch (IOException ex) {
            System.out.println("User: " + userName+" was not read. Does it Exist?");
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
