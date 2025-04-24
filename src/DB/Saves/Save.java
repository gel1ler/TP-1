package DB.Saves;

import game.Utils.InputHandler;
import game.Utils.Menu.Menu;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static DB.DBUtils.getFiles;
import static DB.DBUtils.getFilesNames;
import static game.Utils.Menu.Menu.displayArrays;

public class Save {
    protected static File getSaveFile(String folderPath) {
        File[] saveFiles = getFiles(folderPath);
        Menu.println("Доступные сохранения:");
        List<String> fileNames = getFilesNames(folderPath);
        if (fileNames != null && !fileNames.isEmpty())
            displayArrays(fileNames);
        else {
            Menu.errorMessage("Файлы сохранений отсутсвуют");
            return null;
        }
        int selected = InputHandler.getIntInput();
        return saveFiles[selected - 1];
    }

    protected static String getSaveFileName(String filePath) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy (HH-mm)"));

        return filePath + timestamp + ".ser";
    }

    protected static String getSaveFileName(String filePath, String keys) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy (HH-mm)");
        String formattedDateTime = now.format(formatter);

        return filePath + formattedDateTime + " " + keys + ".ser";
    }
}