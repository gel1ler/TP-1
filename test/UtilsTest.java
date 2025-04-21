import game.Utils.Logs.GameLogger;
import game.Utils.Menu.Menu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static junit.framework.TestCase.assertTrue;

public class UtilsTest {
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        GameLogger.info("Utils test started");
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end(){
        GameLogger.info("Utils test ended");
    }

    @Test
    public void printFormattedMessageTest() {
        TestUtils.setOutputStream(outputStream);
        Menu.printFormattedMessage("ПрИвет");
        assertTrue(TestUtils.logsContains(outputStream, "============\n" + "П Р И В Е Т\n" + "============"));
    }
}