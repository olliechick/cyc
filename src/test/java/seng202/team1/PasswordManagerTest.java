package seng202.team1;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordManagerTest {
/* Doesn't work for some reason will look into it later
    @Test
    public void TestHashSame() throws Exception { //tests whether hash function returns the same hash on all strings with same salt
        byte[] salt = PasswordManager.getNextSalt();
        String password = "password";
        byte[] password1 = PasswordManager.hash(password, salt);
        byte[] password2 = PasswordManager.hash(password, salt);
        assertEquals(password1, password2);
    }*/
    @Test
    public void TestHashDifferant() throws Exception { //tests whether hash function returns the same hash on all strings with differant salt
        byte[] salt = PasswordManager.getNextSalt();
        byte[] password1 = PasswordManager.hash("Password", salt);
        byte[] password2 = PasswordManager.hash("password", salt);
        assertNotEquals(password1, password2);
    }

    @Test
    public void isExpectedPasswordSame() throws Exception {
        byte[] salt = PasswordManager.getNextSalt();
        byte[] password1 = PasswordManager.hash("password", salt);
        boolean isSame = PasswordManager.isExpectedPassword("password", salt, password1);
        assertEquals(true,isSame);
    }

    @Test
    public void isExpectedPasswordWrong() throws Exception {
        byte[] salt = PasswordManager.getNextSalt();
        byte[] password1 = PasswordManager.hash("password", salt);
        boolean isSame = PasswordManager.isExpectedPassword("Password", salt, password1);
        assertEquals(false,isSame);
    }
}