package seng202.team1;

import junit.framework.TestCase;
import seng202.team1.Model.PasswordManager;
import seng202.team1.Model.SerializerImplementation;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;

/**
 * @author Josh Burt
 * @author Ollie Chick
 */
public class SerializerImplementationTest extends TestCase {

    LocalDate birthday;
    UserAccountModel user;
    String expectedFilePath;
    String username;
    String password;
    char gender;
    File f;

    public void setUp() throws Exception {
        super.setUp();
        Main.createDirectories();
        birthday = LocalDate.of(1993, Month.JANUARY,1);
        username = "testUser";
        password = "password";
        user = new UserAccountModel(gender, birthday, username, password);
        expectedFilePath = System.getProperty("user.home") + "/.cyc/users/testUser.user";
        f = new File(expectedFilePath);
    }

    public void testSerializeUser() throws Exception {
        SerializerImplementation.serializeUser(user);
        System.out.println(expectedFilePath);
        assertEquals(true, f.exists());
    }

    public void testDeserializeUser() throws Exception {
        UserAccountModel user = SerializerImplementation.deserializeUser(username);
        assertEquals(username, user.getUserName());
        assertEquals(true, PasswordManager.isExpectedPassword(password, user.getSalt(), user
                .getPassword()));
        assertEquals(gender, user.getGender());
        assertEquals(true, f.delete());
    }

}