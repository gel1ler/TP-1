import game.Player.OwnerType;
import game.Player.Player;
import game.Town.Buildings.Hotel;
import game.Town.Service;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

public class TownBuildingTest {
    @Test
    public void testConcurrentAccess() throws InterruptedException {
        Hotel hotel = new Hotel();
        int threadCount = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        // Подготовим тестовые данные
        Player player = new Player(100, OwnerType.PERSON);
        Service service = hotel.getAvailableItems().get(0);
        Runnable onComplete = () -> {};

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await(); // Ждем стартового сигнала
                    hotel.testInteract(player, service, onComplete);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown(); // Запускаем все потоки одновременно
        finishLatch.await();   // Ждем завершения всех потоков

        assertEquals(threadCount, hotel.getCurrentVisitors().size());
    }
}