import game.Battle;
import game.Map.MainMap;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.*;

public class BattleTest {
    private Player murderer, victim;
    private Hero murdererHero, victimHero;
    private Unit MHUnit, VHUnit;
    private MainMap gameMap;
    private Battle battle;

    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        GameLogger.info("Battle test started");
        murderer = new Player(185, OwnerType.PERSON);
        victim = new Player(185, OwnerType.COMPUTER);
        murdererHero = new Hero(HeroType.BARBARIAN,murderer);
        victimHero = new Hero(HeroType.BARBARIAN, victim);
        MHUnit = new Unit(UnitType.SWORDSMAN, murderer);
        VHUnit = new Unit(UnitType.CAVALRYMAN, victim);
        murdererHero.addUnit(MHUnit);
        victimHero.addUnit(VHUnit);
        gameMap = new MainMap(5, 5, murderer, victim);
        battle = new Battle(5, 5, murderer, victim, murdererHero, victimHero);

        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end(){
        GameLogger.info("Battle test ended");
    }

    @Test
    public void attackTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestUtils.setOutputStream(outputStream);
        assertEquals(60, VHUnit.getHp());

        Method attackMethod = battle.getClass().getDeclaredMethod("attack", MHUnit.getClass(), VHUnit.getClass());
        attackMethod.setAccessible(true);

        attackMethod.invoke(battle, MHUnit, VHUnit);

        assertTrue(TestUtils.logsContains(outputStream, "У вражеского Юнита Кавалерист осталось 45 HP"));
        assertEquals(45, VHUnit.getHp());
    }

    @Test
    public void killTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestUtils.setOutputStream(outputStream);
        assertTrue(VHUnit.getIsAlive());

        Method attackMethod = battle.getClass().getDeclaredMethod("attack", MHUnit.getClass(), VHUnit.getClass());
        attackMethod.setAccessible(true);

        for (int i = 0; i < 4; i++) {
            attackMethod.invoke(battle, MHUnit, VHUnit);
        }

        assertTrue(TestUtils.logsContains(outputStream, "Юнит Кавалерист убит"));
        assertFalse(TestUtils.logsContains(outputStream, "Юнит Паладин убит"));
        assertFalse(VHUnit.getIsAlive());
    }

    @Test
    public void superAbilityTest() {
        TestUtils.setOutputStream(outputStream);

        int[] coords = {2, 3};
        Unit rascal = new Unit(UnitType.RASCAL, murderer);
        VHUnit.setPos(coords);
        murdererHero.addUnit(rascal);

        assertEquals(OwnerType.COMPUTER, VHUnit.getOwnerType());

        while (VHUnit.getOwnerType() == OwnerType.COMPUTER) {
            battle.useSuperAbility(rascal, VHUnit);
        }

        assertTrue(TestUtils.logsContains(outputStream, "Кавалерист успешно завербован!"));
        assertEquals(OwnerType.PERSON, VHUnit.getOwnerType());
    }

    @Test
    public void distanceTest() {
        //Дальность мечника = 1
        battle.setEntityPos(MHUnit, battle.getMap(), new int[]{2, 3});
        battle.setEntityPos(VHUnit, battle.getMap(), new int[]{2, 2});
        assertNotNull(battle.canPersonAttack(MHUnit.getPos(), MHUnit.getFightDist()));

        battle.setEntityPos(VHUnit, battle.getMap(), new int[]{1, 1});
        assertNull(battle.canPersonAttack(MHUnit.getPos(), MHUnit.getFightDist()));

        //Дальность арбалетчика = 3
        MHUnit = new Unit(UnitType.CROSSBOWMAN, murderer);
        battle.setEntityPos(MHUnit, battle.getMap(), new int[]{2, 3});
        battle.setEntityPos(VHUnit, battle.getMap(), new int[]{2, 2});
        assertNotNull(battle.canPersonAttack(MHUnit.getPos(), MHUnit.getFightDist()));

        battle.setEntityPos(VHUnit, battle.getMap(), new int[]{0, 1});
        assertNotNull(battle.canPersonAttack(MHUnit.getPos(), MHUnit.getFightDist()));
    }
}
