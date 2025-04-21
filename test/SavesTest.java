import DB.Saves.GameSave;
import game.MainGame;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.InputHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SavesTest {
    private static final String TEST_SAVE_PATH = "db/saves/test";
    private MainGame testGame;

    @Before
    public void setUp() {
        new File(TEST_SAVE_PATH).mkdirs();
        Player person = new Player(185, OwnerType.PERSON);
        Player computer = new Player(185, OwnerType.COMPUTER);
        testGame = new MainGame(10, 10, person, computer, true);
    }

    @Test
    public void testWriteNewSave() {
        GameSave.writeSave(testGame, true);
        File[] saveFiles = new File(TEST_SAVE_PATH).listFiles();
        assertNotNull(saveFiles);
        assertTrue(saveFiles.length > 0);
    }

    @Test
    public void testReadSave() {
        InputHandler.setScanner(TestUtils.createScanner("1\n"));
        MainGame loadedGame = GameSave.readSave();
        assertNotNull(loadedGame);
    }
}