import game.Player.OwnerType;
import game.Player.Player;
import game.Town.Buildings.Hotel;
import game.Town.Customer;
import game.Town.TownBuilding;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void testServiceCompletion() throws InterruptedException {
        Hotel hotel = new Hotel();
        Player player = new Player(100, OwnerType.PERSON);

        Customer customer = new Customer("test", 1, player, hotel, hotel.chill(player, 20));
        customer.start();

        assertTrue(player.isBusy());
        customer.join(2000);
        assertFalse(player.isBusy());
    }
}