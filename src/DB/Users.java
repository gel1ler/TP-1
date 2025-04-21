package DB;

import game.Utils.Menu.GameMenu;
import game.Utils.Menu.Menu;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

import static DB.DBUtils.createDirectory;

public class Users {
    private static final String[] keyWords = {"test"};

    public static boolean register(String name) throws IOException {
        if (Arrays.asList(keyWords).contains(name)) {
            Menu.errorMessage("Вы не можете зарегистрироваться под именем " + name + ". Оно явялется системным.");
            return false;
        }
        boolean isRegistered = checkIsRegistered(name);

        if (isRegistered) Menu.println("Вы вошли под именем " + name);
        else {
            FileWriter usersFile = new FileWriter(DbPaths.USERS.getPath(), true);
            usersFile.append(name).append("\n");
            usersFile.close();

            createDirectory(DbPaths.SAVES.getUserPath());

            Menu.println("Вы зарегистрировались под именем " + name);
        }
        return true;
    }

    private static boolean checkIsRegistered(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(DbPaths.USERS.getPath()));
        String line;
        while ((line = br.readLine()) != null) {
            if (Objects.equals(name, line)) {
                return true;
            }
        }

        return false;
    }
}
