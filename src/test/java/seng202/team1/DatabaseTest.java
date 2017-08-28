package seng202.team1;

import org.junit.*;

import java.nio.file.FileAlreadyExistsException;

import static org.junit.Assert.*;

/**
 * Unit tests for database.
 * @author Ridge Nairn
 */


public class DatabaseTest{

    @Before
    public void SetUp() {
        try {
            DatabaseManager.createNewLocalDatabase();
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @After
    public void TearDown() {
        DatabaseManager.deleteDatabase();
    }

    @Test
    public void createNewDatabase() {
        Assert.assertTrue(DatabaseManager.isDatabaseConnected());
    }

    @Test
    public void deleteDatabase() {
        DatabaseManager.deleteDatabase();
        assertFalse(DatabaseManager.isDatabaseConnected());
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void createExistingDatabase() throws FileAlreadyExistsException{
        DatabaseManager.createNewLocalDatabase();
    }
}
