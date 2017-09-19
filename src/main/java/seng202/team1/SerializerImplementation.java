package seng202.team1;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;

/**
 *Static class to serialize the users.
 *
 * @author Josh Burt
 */
public final class SerializerImplementation {


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
