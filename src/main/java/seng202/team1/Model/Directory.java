package seng202.team1.Model;

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
