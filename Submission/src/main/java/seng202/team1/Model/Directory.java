package seng202.team1.Model;

/**
 * Directory enum that stores the names of all the directories necessary for the app.
 * These are used to create them if they don't exist and so the app knows where to
 * get files from.
 *
 * @author Ollie Chick
 */
public enum Directory {

    ROOT(System.getProperty("user.home") + "/.cyc/"),
    USERS(ROOT.directory() + "users/"),
    DATABASES(ROOT.directory() + "databases/");

    private String directory;

    Directory(String directory) {
        this.directory = directory;
    }

    public String directory() {
        return directory;
    }

}
