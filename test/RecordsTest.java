import game.Utils.Logs.GameLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static DB.Records.Records.insertRecords;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordsTest {
    private ByteArrayOutputStream outputStream;
    private final Map<String, Long> stats = new HashMap<>();

    @Before
    public void setUp(){
        GameLogger.info("Records test ended");

        stats.put("kills", (long) 1000);
        stats.put("steps", (long) 1);
        stats.put("time", (long) 10);

        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end(){
        GameLogger.info("Records test ended");
    }

    @Test
    public void insertRecordsTest() throws IOException {
        TestUtils.setOutputStream(outputStream);

        insertRecords("Alex", stats);


        assertTrue(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Убийца!"));
        assertTrue(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Лентяй!"));
        assertTrue(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Скороход!"));
    }

    @Test
    public void notReachRecordTest() throws IOException {
        TestUtils.setOutputStream(outputStream);
        stats.put("kills", (long) 1);
        stats.put("steps", (long) 1000000);
        stats.put("time", (long) 10000000);

        insertRecords("Looser", stats);
        assertFalse(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Убийца!"));
        assertFalse(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Лентяй!"));
        assertFalse(TestUtils.logsContains(outputStream, "Ваш рекорд добавлен в таблицу Скороход!"));
    }
}