package seng202.team1;

import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by jbu71 on 19/09/17.
 */
public class SerializerImplementationTest {
    @Test
    public void TestSerializeUser() throws Exception {
        LocalDate birthday = LocalDate.of(1993, Month.JANUARY,1);
        UserAccountModel u = new UserAccountModel('m', birthday,"Josh", "Password");
        SerializerImplementation.serializeUser(u);
        File f = new File("src/main/resources/tmp/Josh.ser");
        assertEquals(true, f.exists());
    }

    @Test
    public void TestDeserialzeUser() throws Exception {
        UserAccountModel user = SerializerImplementation.deserializeUser("Josh");
        assertEquals("Josh", user.getUserName());
        assertEquals(true,PasswordManager.isExpectedPassword("Password",user.getSalt(), user.getPassword()));
        assertEquals('m', user.getGender());
    }
}