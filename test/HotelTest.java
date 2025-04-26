import game.Player.Entities.Hero;
import game.Player.Entities.HeroType;
import game.Player.Entities.Unit;
import game.Player.Entities.UnitType;
import game.Player.OwnerType;
import game.Player.Player;
import game.Town.Buildings.Hotel;
import org.junit.Test;

import static org.junit.Assert.*;

public class HotelTest {
    @Test
    public void testChillEffect() {
        Hotel hotel = new Hotel();
        Player testPlayer = new Player(100, OwnerType.PERSON);
        Hero testHero = new Hero(HeroType.WIZARD, testPlayer);
        testHero.addUnit( new Unit(UnitType.CAVALRYMAN, testPlayer));
        testPlayer.addHero(testHero);

        int initialHp = testPlayer.getHeroes().getFirst().getUnits().getFirst().getHp();

        hotel.chill(testPlayer, 20).run();

        int newHp = testPlayer.getHeroes().getFirst().getUnits().getFirst().getHp();
        assertEquals(initialHp + 20, newHp);
    }
}