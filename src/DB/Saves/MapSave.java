package DB.Saves;

import DB.DbPaths;
import game.MainGame;
import game.Map.MainMap;

import java.io.*;

import static game.Main.getUserName;

public class MapSave extends Save {
    private static final String folderPath = DbPaths.MAPS.getPath();
    private static final String filePath = folderPath + "save ";

    public static void writeSave(MainMap mainMap) {
        File file = new File(getSaveFileName(filePath, mainMap.getSizeInString()));

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(mainMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MainMap readSave() {
        File saveFile = getSaveFile(folderPath);

        try (FileInputStream fileIn = new FileInputStream(saveFile);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            return (MainMap) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
