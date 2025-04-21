import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import game.Player.OwnerType;
import game.Player.Entities.Hero;
import game.Player.Entities.HeroType;
import game.Player.Player;
import game.Utils.Logs.GameLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Player player;
    private Hero testHero;
    private Hero testHero2;
    private int[] testCoords;

    @Before
    public void setUp() {
        player = new Player(185, OwnerType.PERSON); // Предположим, что у вас есть конструктор по умолчанию
        testHero = new Hero(HeroType.BARBARIAN, OwnerType.PERSON);
        testHero2 = new Hero(HeroType.BARBARIAN, OwnerType.PERSON);
        testCoords = new int[]{2, 2};
        GameLogger.info("Player test ended");
    }

    @After
    public void end(){
        GameLogger.info("Player test ended");
    }

    @Test
    public void minusGoldTest() {
        assertEquals(185, player.getGold());

        player.minusGold(100);
        assertEquals(85, player.getGold());
    }

    @Test
    public void setGoldTest() {
        assertEquals(185, player.getGold());

        player.setGold(100);
        assertEquals(100, player.getGold());
    }


    @Test
    public void testPlayerInitiallyHasNoHeroes() {
        assertFalse(player.hasHeroes());
    }

    @Test
    public void testAddHero() {
        player.addHero(testHero);
        assertTrue(player.hasHeroes());
        assertTrue(player.getHeroes().contains(testHero));
    }

    @Test
    public void testGetHeroByCoordinates() {
        player.addHero(testHero);
        testHero2.setPos(testCoords);
        player.addHero(testHero2);

        assertNotEquals(testHero, player.getHeroByCords(testCoords));
        assertEquals(testHero2, player.getHeroByCords(testCoords));
    }

    @Test
    public void testKillHero() {
        player.addHero(testHero);
        assertTrue(player.getHeroes().contains(testHero));
        player.kill(testHero);
        assertFalse(player.getHeroes().contains(testHero));
    }
}