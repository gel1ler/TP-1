import game.Utils.InputHandler;
import game.Utils.Logs.GameLogger;
import game.Utils.Menu.Menu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import static junit.framework.TestCase.assertTrue;

public class UtilsTest {
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        GameLogger.info("Utils test started");
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end() {
        GameLogger.info("Utils test ended");
    }

    @Test
    public void printFormattedMessageTest() {
        TestUtils.setOutputStream(outputStream);
        Menu.printFormattedMessage("ПрИвет");
        assertTrue(TestUtils.logsContains(outputStream, "============\r\n" + "П Р И В Е Т\r\n" + "============"));
    }

    @Test(expected = StackOverflowError.class)
    public void testIntInputStackOverflow() {
        // Создаем сканер с большим количеством невалидных входных данных
        String infiniteInvalidInput = "invalid\n".repeat(1);
        InputHandler.setScanner(TestUtils.createScanner(infiniteInvalidInput));

        // Вызываем метод, который должен упасть с StackOverflowError
        InputHandler.getIntInputOld();
    }
}