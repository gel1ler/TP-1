import game.Castle.Buildings.Hub;
import game.Castle.Buildings.Tavern.Tavern;
import game.MainGame;
import game.Player.OwnerType;
import game.Player.Entities.Hero;
import game.Player.Entities.HeroType;
import game.Player.Entities.Unit;
import game.Player.Entities.UnitType;
import game.Player.Player;
import game.Utils.Logs.GameLogger;
import game.Utils.InputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertArrayEquals;

public class GameTest {
    MainGame mainGame;
    Player person, computer;
    Hero personTestHero, computerTestHero;
    private ByteArrayOutputStream outputStream;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        GameLogger.info("Game test started");

        person = new Player(185, OwnerType.PERSON);
        computer = new Player(185, OwnerType.COMPUTER);
        personTestHero = new Hero(HeroType.BARBARIAN, person);
        computerTestHero = new Hero(HeroType.KNIGHT, computer);
        person.addHero(personTestHero);
        computer.addHero(computerTestHero);
        personTestHero.addUnit(new Unit(UnitType.SWORDSMAN, person));
        computerTestHero.addUnit(new Unit(UnitType.RASCAL, computer));
        mainGame = new MainGame(10, 10, person, computer, true);
        mainGame.getMap().setHeroes(0, 0, person);
        mainGame.getMap().setHeroes(10 - 1, 10 - 1, computer);

        if (!testName.getMethodName().equals("isGameOverBecauseOfBuildingsTest")) {
            Tavern PersonTavern = new Tavern(person);
            Hub PersonHub = new Hub(person);
            Tavern CTavern = new Tavern(person);
            Hub CHub = new Hub(person);

            PrintStream originalOut = System.out;
            TestUtils.preventConsoleOutput();
            person.getCastle().addBuildingToCastle(PersonTavern);
            person.getCastle().addBuildingToCastle(PersonHub);
            computer.getCastle().addBuildingToCastle(CTavern);
            computer.getCastle().addBuildingToCastle(CHub);
            //Возврат оригинального потока ввода
            System.setOut(originalOut);
        }

        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void end(){
        GameLogger.info("Game test ended");
    }

    @Test
    public void isGameOverBecauseOfBuildingsTest() {
        assertTrue(mainGame.isGameOverFor(person));

        Tavern tavern = new Tavern(person);
        Hub hub = new Hub(person);

        person.getCastle().addBuildingToCastle(tavern);
        person.getCastle().addBuildingToCastle(hub);

        assertFalse(mainGame.isGameOverFor(person));
    }

    @Test
    public void isGameOverBecauseOfHeroesTest() {
        assertFalse(mainGame.isGameOverFor(person));
        assertFalse(mainGame.isGameOverFor(computer));

        person.kill(personTestHero);

        assertTrue(mainGame.isGameOverFor(person));
        assertFalse(mainGame.isGameOverFor(computer));

        computer.kill(computerTestHero);

        assertTrue(mainGame.isGameOverFor(person));
        assertTrue(mainGame.isGameOverFor(computer));
    }

    @Test
    public void gameEndingByPersonInvasionTest() {
        assertFalse(mainGame.isCastleInvaded());
        mainGame.startInvasion(personTestHero);
        assertTrue(mainGame.isCastleInvaded());

        for (int i = 0; i < 3; i++) {
            mainGame.decrementTurnsInCastle();
        }
        assertTrue(mainGame.isGameOverFor(computer));
    }

    @Test
    public void gameEndingByComputerInvasionTest() {
        assertFalse(mainGame.isCastleInvaded());
        mainGame.startInvasion(personTestHero);
        assertTrue(mainGame.isCastleInvaded());

        for (int i = 0; i < 3; i++) {
            mainGame.decrementTurnsInCastle();
        }
        assertTrue(mainGame.isGameOverFor(computer));
    }

    @Test
    public void getDirectionToPositionTest() {
        int[] newCoords = mainGame.getDirectionToPosition(6, 3, 3, 8);

        assertArrayEquals(new int[]{5, 4}, newCoords);
    }

    @Test
    public void MPTest() {
        //Поставить на диагональ (дорогу)
        mainGame.setEntityPos(computerTestHero, mainGame.getMap(), new int[]{7, 7});
        //480 - у рыцаря
        //Один шаг по дороге стоит 75 * sqrt(2) = 106
        //480 - 106 * n => n = 4
        //(7, 7) -> (3, 3)
        mainGame.moveTest(computerTestHero, true);
        assertArrayEquals(new int[]{3, 3}, computerTestHero.getPos());
    }

    @Test
    public void personMoveTest() {
//       Initial pos (0, 1)
        personTestHero.display();
        InputHandler.setScanner(TestUtils.createScanner("3\n2\n3\n0"));
        TestUtils.preventConsoleOutput();
        mainGame.moveTest(personTestHero, false);

        assertArrayEquals(new int[]{3, 3}, personTestHero.getPos());
    }

    @Test
    public void moveOnOccupiedCellTest() {
        TestUtils.setOutputStream(outputStream);

        mainGame.setEntityPos(computerTestHero, mainGame.getMap(), new int[]{4, 4});
        mainGame.setEntityPos(personTestHero, mainGame.getMap(), new int[]{2, 2});
        mainGame.moveTest(computerTestHero, true);

        assertTrue(TestUtils.logsContains(outputStream, "Клетка занята другим юнитом."));
        assertArrayEquals(new int[]{3, 3}, computerTestHero.getPos());
    }

    @Test
    public void moveOutOfBoundsTest() {
        //Initial pos = (0, 1)
        mainGame.setEntityPos(personTestHero, mainGame.getMap(), new int[]{11, 11});
        assertArrayEquals(new int[]{0, 1}, personTestHero.getPos());
    }
}