package DB;


import game.Utils.InputHandler;
import game.Utils.Menu.Menu;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static game.Utils.Menu.Menu.displayArrays;

public class DBUtils {
    public static void createDirectory(String folderPath) {
        File directory = new File(folderPath);
        directory.mkdirs();
    }

    public static File[] getFiles(String folderPath) {
        return new File(folderPath).listFiles();
    }

    public static List<String> getFilesNames(String folderPath) {
        try {
            return Arrays.stream(getFiles(folderPath))
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
        } catch (NullPointerException e){
            return null;
        }
    }
}