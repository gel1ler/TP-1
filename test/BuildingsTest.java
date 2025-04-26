import game.Castle.Buildings.Building;
import game.Castle.Buildings.Hub;
import game.Castle.Buildings.Stable;
import game.Castle.Buildings.Tavern.Tavern;
import game.Player.OwnerType;
import game.Player.Entities.Hero;
import game.Player.Entities.HeroType;
import game.Player.Entities.Unit;
import game.Player.Entities.UnitType;
import game.Player.Player;
import game.Utils.Logs.GameLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BuildingsTest {
    private Player player;
    private Hero testHero;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        GameLogger.info("Battle test started");

        player = new Player(185, OwnerType.PERSON);
        testHero = new Hero(HeroType.KNIGHT, player);

        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end(){
        GameLogger.info("Buildings test ended");
    }

    @Test
    public void buyBuildingTest() {
        TestUtils.setOutputStream(outputStream);

        assertFalse(player.hasTavern());
        Building item = new Tavern(player);
        player.getCastle().addBuildingToCastle(item);

        assertTrue(TestUtils.logsContains(outputStream, "Куплено: Таверна"));
        assertTrue(player.hasTavern());
    }

    @Test
    public void tavernTest() {
        TestUtils.setOutputStream(outputStream);
        assertFalse(player.hasHeroes());

        Tavern tavern = new Tavern(player);
        tavern.addHeroToOwner(testHero);

        assertTrue(TestUtils.logsContains(outputStream, "Куплено: Рыцарь"));
        assertTrue(player.hasHeroes());
    }

    @Test
    public void hubTest() {
        player.addHero(testHero);
        assertEquals(0, testHero.getUnitsCount());

        Hub hub = new Hub(player);
        Unit testUnit = new Unit(UnitType.RASCAL, player);
        hub.addUnitToHero(testUnit, testHero);

        assertEquals(1, testHero.getUnitsCount());
    }

    @Test
    public void stableTest() {
        TestUtils.setOutputStream(outputStream);

        assertEquals(480, testHero.getMP());
        Stable stable = new Stable(player);
        stable.applyUpgrade(testHero, 100);

        assertTrue(TestUtils.logsContains(outputStream, "Улучшение применено"));
        assertEquals(580, testHero.getMP());
    }
}
