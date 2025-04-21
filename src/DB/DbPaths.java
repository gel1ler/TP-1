package DB;

import static game.Main.getUserName;

public enum DbPaths {
    USERS("db/users.txt"),
    SAVES("db/saves/"),
    MAPS("db/maps/"),
    RECORDS("db/records/");

    private final String path;

    DbPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getUserPath() {
        String name = getUserName() != null ? getUserName() : "test";
        return path + name + "/";
    }
}
