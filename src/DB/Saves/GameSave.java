package DB.Saves;

import DB.DbPaths;
import game.MainGame;
import game.Map.MainMap;
import game.Utils.InputHandler;
import game.Utils.Menu.Menu;

import java.io.*;

import static game.Main.getUserName;

public class GameSave extends Save {
    private static final String folderPath = DbPaths.SAVES.getUserPath();

    public static void writeSave(MainGame game, boolean auto) {
        int selected = 1;
        if (!auto) {
            Menu.println("1 - Сохранить в новый файл");
            Menu.println("2 - Перезаписать сохранение");
            selected = InputHandler.getIntInput();
        }

        File file;
        file = switch (selected) {
            case 1 -> new File(getSaveFileName(auto ? folderPath + "autosave " : folderPath + "save "));
            case 2 -> {
                file = getSaveFile(folderPath);

                File renamedFile = new File(getSaveFileName(folderPath + "save "));

                boolean isRenamed = file.renameTo(renamedFile);

                if (isRenamed) {
                    Menu.println("Файл успешно переименован в: " + renamedFile.getName());
                } else {
                    Menu.errorMessage("Не удалось переименовать файл!");
                }

                yield renamedFile;
            }
            default -> throw new IllegalStateException("Unexpected value: " + selected);
        };


        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MainGame readSave() {
        File saveFile = getSaveFile(folderPath);

        try (FileInputStream fileIn = new FileInputStream(saveFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            return (MainGame) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
