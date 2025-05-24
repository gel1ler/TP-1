package game;

import java.util.*;

import DB.Saves.MapSave;
import game.Map.MainMap;
import game.Player.Entities.*;
import game.Player.OwnerType;
import game.Player.Player;
import game.Town.Customer;
import game.Town.Service;
import game.Utils.GameTime;
import game.Utils.InputHandler;
import game.Utils.Menu.GameMenu;
import game.Utils.Menu.MainMenu;

import static game.Main.saveGame;
import static game.Utils.Menu.GameMenu.busyPersonMessage;
import static game.Utils.Menu.GameMenu.chooseMapSave;

public class MainGame extends Game {
    private MainMap gameMap;
    private final Player person, computer;
    private Hero selectedHero;
    private int turnsInCastle = Integer.MAX_VALUE;
    private Player invader;
    private Battle battle;
    private GameStatus status;

    public MainGame(int n, int m) {
        super(n, m);
        person = new Player(185, OwnerType.PERSON);
        computer = new Player(185, OwnerType.COMPUTER);
        setGameMap(false);
    }

    public MainGame(int n, int m, Player person, Player computer, boolean auto) {
        super(n, m);
        this.person = person;
        this.computer = computer;
        setGameMap(auto);
    }


    // INIT MAP
    private void setGameMap(boolean auto) {
        try {
            if (!auto && userWantsToLoadSave()) {
                loadSavedMap();
            } else {
                createNewMap();
            }

            ensurePlayersAreSet();
        } catch (Exception e) {
            GameMenu.errorMessage(e.getMessage());
        }
    }

    private boolean userWantsToLoadSave() {
        //TEST
//        chooseMapSave();
//        return InputHandler.getIntInput() == 1;
        return false;
        //TEST
    }

    private void createNewMap() {
        gameMap = new MainMap(n, m, person, computer);
    }

    private void loadSavedMap() {
        gameMap = MapSave.readSave();
        if (gameMap == null) {
            createNewMap();
        }
    }

    private void ensurePlayersAreSet() {
        if (gameMap != null && !gameMap.hasPlayers()) {
            gameMap.setPlayers(person, computer);
        }
    }
    // INIT MAP


    //START GAME
    public boolean start() {
        if (status == null) initializeGame();
        else if (status == GameStatus.BATTLE) {
            continueBattle();
        }

        person.getCastle().enter();
        if (isGameOverFor(person)) {
            MainMenu.printGameEnd(OwnerType.PERSON);
        } else {
            gamePlay();
        }
        return checkGameOver(computer);
    }

    private void initializeGame() {
//        computer.buyRandom();
        //TEST
        person.addHero(new Hero(HeroType.BARBARIAN, person));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.RASCAL, person)));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.CAVALRYMAN, person)));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.SWORDSMAN, person)));

        computer.addHero(new Hero(HeroType.KNIGHT, computer));
        computer.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.PALADIN, computer)));
        computer.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.SPEARMAN, computer)));
        //TEST
        gameMap.setHeroes(0, 0, person);
        gameMap.setHeroes(n - 1, m - 1, computer);
        setStatus(GameStatus.MAINGAME);
    }

    private void gamePlay() {
        if (status == GameStatus.BATTLE) {
            battle.start();
        }
        gameMap.render();

        while (turnsInCastle >= 0) {
            personTurn();
            gameMap.render();
            if (checkGameOver(computer)) {
                break;
            }

            computerTurn();
            gameMap.render();
            if (checkGameOver(person)) {
                break;
            }

            if (isCastleInvaded()) {
                decrementTurnsInCastle();
                if (turnsInCastle < 0) {
                    MainMenu.printGameEnd(this.invader.getOwnerType().getEnemy());
                    break;
                }
            }
        }
    }
    //START GAME


    //END GAME
    public boolean isGameOverFor(Player owner) {
        boolean ownerHasHeroes = owner.hasHeroes(); //Есть ли герои
        boolean allHeroesHaveUnits = owner.getHeroes().stream().allMatch(i -> i.getUnitsCount() > 0); // У всех ли героев есть юниты
        boolean ownerHasAllBuildings = owner.hasTavern() && owner.hasHub(); // Есть ли все здания
        boolean isLostByInvasion = turnsInCastle < 0 && invader != owner; // Не проиграл ли вторжением

        return !ownerHasHeroes || !allHeroesHaveUnits || !ownerHasAllBuildings || isLostByInvasion;
    }

    private boolean checkGameOver(Player player) {
        if (isGameOverFor(player)) {
            MainMenu.printGameEnd(player.getOwnerType());
            return true;
        }
        return false;
    }
    //END GAME


    //CASTLE INVASION
    public void decrementTurnsInCastle() {
        turnsInCastle--;
    }

    public boolean isCastleInvaded() {
        return turnsInCastle <= (invader != null ? invader.getInvasionTime() : 2);
    }

    public void startInvasion(Hero hero) {
        setStatus(GameStatus.INVASION);
        GameMenu.printInvasion(hero);
        if (this.invader == null) this.invader = hero.getOwner();
        this.turnsInCastle = invader.getInvasionTime();
        gameMap.registerInvasion(hero);
    }
    //CASTLE INVASION


    //PERSON TURN
    private void waitServiceEnd() {
        int i = 0;
        while (person.isBusy()) {
            try {
                i++;
                if (i > 5) {
                    busyPersonMessage(person);
                    i = 0;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void personTurn() {
        if (person.isBusy()) {
            busyPersonMessage(person);
            waitServiceEnd();
        }

        boolean canAtack = false, canInvade = false;
        while (selectedHero == null) {
            selectedHero = personSelectHero();
        }

        int y = selectedHero.getY();
        int x = selectedHero.getX();

        HashMap<String, int[]> nearby = checkEnemies(y, x, gameMap, OwnerType.PERSON, 1);
        int[] enemyCords = nearby.get("enemy");
        int[] castleCords = nearby.get("castle");

        if (enemyCords != null) {
            canAtack = true;
        }
        if (castleCords != null) {
            canInvade = true;
        }
        GameMenu.showAvailableHeroActions(enemyCords, castleCords);
        int action = InputHandler.getIntInput();

        switch (action) {
            case 1:
                move(selectedHero, gameMap, false);
                break;
            case 2:
                GameMenu.println("Ход пропущен.");
                break;
            case 3:
                if (canAtack) {
                    startBattle(person, computer, new int[]{selectedHero.getY(), selectedHero.getX()}, enemyCords);
                    break;
                }
                GameMenu.wrongChoice();
            case 4:
                if (canInvade) {
                    startInvasion(selectedHero);
                    break;
                }
                GameMenu.wrongChoice();
                break;
            case 0:
                selectedHero = null;
                personTurn();
                return;
            case 10:
                enterCastle();
                personTurn();
                break;
            default:
                GameMenu.wrongChoice();
                personTurn();
                break;
        }
    }

    private Hero personSelectHero() {
        List<Hero> heroes = person.getHeroes();
        if (heroes.isEmpty()) {
            return null;
        }

        GameMenu.chooseEntity(heroes, "Героя");

        int selected = InputHandler.getIntInput() - 1;
        if (selected >= 0 && selected < heroes.size()) {
            return heroes.get(selected);
        } else {
            GameMenu.wrongChoice();
            return null;
        }
    }

    private void enterCastle() {
        setStatus(GameStatus.INCASTLE);
        person.getCastle().enter();
        //FIX - установка если позиция 0, 0, но он может быть в замке. Надо добавить boolean поле isPlaced
        gameMap.updateHeroes(0, 0);
    }
    //PERSON TURN


    //COMPUTER TURN
    private void computerTurn() {
        GameMenu.println("Ход компьютера");
        Hero computerHero = selectComputerHero();
        if (computerHero == null) {
            GameMenu.println("У компьютера нет героев для хода.");
            return;
        }

        int y = computerHero.getY();
        int x = computerHero.getX();

        HashMap<String, int[]> nearby = checkEnemies(y, x, gameMap, OwnerType.COMPUTER, 1);
        int[] enemyCords = nearby.get("enemy");
        int[] castleCords = nearby.get("castle");

        if (enemyCords != null) {
            GameMenu.println("Компьютер атакует врага!");
            startBattle(computer, person, new int[]{y, x}, enemyCords);

        } else if (castleCords != null) {
            startInvasion(computerHero);
        } else {
            GameMenu.println("Компьютер перемещает героя.");
            move(computerHero, gameMap, true);
        }
    }

    private Hero selectComputerHero() {
        List<Hero> heroes = computer.getHeroes();
        if (heroes.isEmpty()) {
            return null;
        }

        return heroes.getFirst();
    }
    //COMPUTER TURN


    //BATTLE
    private void startBattle(Player murderer, Player victim, int[] murdererCords, int[] victimCords) {
        setStatus(GameStatus.BATTLE);
        Hero murdererHero = murderer.getHeroByCords(murdererCords);
        Hero victimHero = victim.getHeroByCords(victimCords);
        battle = new Battle(n, m, murderer, victim, murdererHero, victimHero);
        status = GameStatus.BATTLE;
        continueBattle(); // Start
        status = GameStatus.MAINGAME;
        saveGame(true);
    }

    private void continueBattle() {
        Hero looser = battle.start();
        gameMap.kill(looser.getY(), looser.getX());
        if (looser.getOwnerType() == OwnerType.PERSON) selectedHero = null;
    }
    //BATTLE


    //UTILS
    private void setStatus(GameStatus status) {
        this.status = status;
    }

    public void moveTest(Hero hero, boolean auto) {
        move(hero, gameMap, auto);
    }

    public MainMap getMap() {
        return this.gameMap;
    }
    //UTILS
}