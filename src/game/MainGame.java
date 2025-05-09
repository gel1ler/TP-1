package game;

import java.io.IOException;
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
import static game.Utils.Menu.GameMenu.chooseMapSave;

public class MainGame extends Game {
    private MainMap gameMap;
    private final Player person, computer;
    private Hero selectedHero;
    private int turnsInCastle = Integer.MAX_VALUE;
    private Player invader;
    private Battle battle;
    private GameStatus status;
    private Customer currentCustomer = null;

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

    private void createNewMap() {
        gameMap = new MainMap(n, m, person, computer);
    }

    private boolean shouldLoadSavedMap(boolean autoGenerate) {
        return !autoGenerate && userWantsToLoadSave();
    }

    private boolean userWantsToLoadSave() {
        chooseMapSave();
        return InputHandler.getIntInput() == 1;
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

    private void setGameMap(boolean auto) {
        try {
            if (shouldLoadSavedMap(auto)) {
                loadSavedMap();
            } else {
                createNewMap();
            }

            ensurePlayersAreSet();
        } catch (Exception e) {
            GameMenu.errorMessage(e.getMessage());
        }
    }

    public boolean start() {
        if (status == null) initializeGame();
        else if (status == GameStatus.BATTLE) {
            continueBattle();
        }

        person.getCastle().enter(); // Покупка и найм

        if (isGameOverFor(person)) {
            MainMenu.printGameEnd(OwnerType.PERSON);
        } else {
            gamePlay();
        }
        return checkGameOver(computer);
    }

    private void setStatus(GameStatus status) {
        this.status = status;
    }

    private void gamePlay() {
        if (status == GameStatus.BATTLE) {
            battle.start();
        }
        gameMap.render();

        while (turnsInCastle >= 0) {
            if (person.isBusy()) {
                currentCustomer = person.getCustomer();
                GameMenu.println("У вас сейчас " + currentCustomer.getServiceName() + ". Осталось еще " + GameTime.formatMinutes(currentCustomer.getRemains()));
                GameMenu.println("Введите 10 чтобы войти в замок или любое другое число для пропуска хода");
                int selected = InputHandler.getIntInput();
                if (person.isBusy()) {
                    if (selected == 10) {
                        enterCastle();
                    } else GameMenu.println("Ход пропущен.");
                }
            }
            if (!person.isBusy()) {
                personTurn();
                gameMap.render();
                if (checkGameOver(computer)) {
                    break;
                }
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

    private boolean checkGameOver(Player player) {
        if (isGameOverFor(player)) {
            MainMenu.printGameEnd(player.getOwnerType());
            return true;
        }
        return false;
    }

    public boolean isGameOverFor(Player owner) {
        boolean ownerHasHeroes = owner.hasHeroes(); //Есть ли герои
        boolean allHeroesHaveUnits = owner.getHeroes().stream().allMatch(i -> i.getUnitsCount() > 0); // У всех ли героев есть юниты
        boolean ownerHasAllBuildings = owner.hasTavern() && owner.hasHub(); // Есть ли все здания
        boolean isLostByInvasion = turnsInCastle < 0 && invader != owner; // Не проиграл ли вторжением

        return !ownerHasHeroes || !allHeroesHaveUnits || !ownerHasAllBuildings || isLostByInvasion;
    }

    public void decrementTurnsInCastle() {
        turnsInCastle--;
    }

    public boolean isCastleInvaded() {
        return turnsInCastle <= (invader != null ? invader.getInvasionTime() : 2);
    }

    private void initializeGame() {
//        computer.buyRandom();
        //TEST
        person.addHero(new Hero(HeroType.BARBARIAN, person));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.RASCAL, person)));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.CAVALRYMAN, person)));
        person.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.SWORDSMAN, person)));

        computer.addHero(new Hero(HeroType.KNIGHT, computer));
        computer.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.RASCAL, computer)));
//        computer.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.PALADIN, OwnerType.COMPUTER)));
//        computer.getHeroes().forEach(i -> i.addUnit(new Unit(UnitType.SPEARMAN, OwnerType.COMPUTER)));
        //TEST
        gameMap.setHeroes(0, 0, person);
        gameMap.setHeroes(n - 1, m - 1, computer);
        setStatus(GameStatus.MAINGAME);
    }

    public void startInvasion(Hero hero) {
        setStatus(GameStatus.INVASION);
        GameMenu.printInvasion(hero);
        if (this.invader == null) this.invader = hero.getOwner();
        this.turnsInCastle = invader.getInvasionTime();
        gameMap.registerInvasion(hero);
    }

    private void personTurn() {
        boolean canAtack = false, canInvade = false;
        while (selectedHero == null) {
            selectedHero = selectHero();
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

    private void enterCastle() {
        setStatus(GameStatus.INCASTLE);
        person.getCastle().enter();
        //FIX - установка если позиция 0, 0, но он может быть в замке. Надо добавить boolean поле isPlaced
        gameMap.updateHeroes(0, 0);
    }

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

    private Hero selectHero() {
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

    private Hero selectComputerHero() {
        List<Hero> heroes = computer.getHeroes();
        if (heroes.isEmpty()) {
            return null;
        }

        return heroes.getFirst();
    }

    private void startBattle(Player murderer, Player victim, int[] murdererCords, int[] victimCords) {
        setStatus(GameStatus.BATTLE);
        Hero murdererHero = murderer.getHeroByCords(murdererCords);
        Hero victimHero = victim.getHeroByCords(victimCords);
        battle = new Battle(n, m, murderer, victim, murdererHero, victimHero, gameMap);
        status = GameStatus.BATTLE;
        OwnerType looser = battle.start();
        if (looser == OwnerType.PERSON) selectedHero = null;
        status = GameStatus.MAINGAME;
        saveGame(true);
    }

    private void continueBattle() {
        OwnerType looser = battle.start();
        if (looser == OwnerType.PERSON) selectedHero = null;
    }

    public void moveTest(Hero hero, boolean auto) {
        move(hero, gameMap, auto);
    }

    public MainMap getMap() {
        return this.gameMap;
    }
}