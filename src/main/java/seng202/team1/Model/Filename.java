package seng202.team1.Model;

public enum Filename {

    ROOT(System.getProperty("user.home") + "/.cyc/"),
    USERS(ROOT.filename() + "users/");
    //DATABASES(ROOT.filename() + "databases/");

    private String filename;

    Filename(String filename) {
        this.filename = filename;
    }

    public String filename() {
        return filename;
    }

}
