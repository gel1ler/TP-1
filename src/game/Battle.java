package game;

import game.Map.BattleMap;
import game.Map.Map;
import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.InputHandler;
import game.Utils.Menu.GameMenu;

import java.util.HashMap;
import java.util.List;

public class Battle extends Game {
    private final BattleMap battleMap;
    private final Player person, computer;
    private final Hero personHero, computerHero;
    private Unit selectedUnit;
    private boolean isBattleOver;
    private OwnerType looser;
    private final Map mainMap;

    public Battle(int n, int m, Player murderer, Player victim, Hero murdererHero, Hero victimHero, Map mainMap) {
        super(n, m);
        boolean isPlayerMurderer = murderer.getOwnerType().equals(OwnerType.PERSON);
        this.person = isPlayerMurderer ? murderer : victim;
        this.computer = isPlayerMurderer ? victim : murderer;
        this.personHero = isPlayerMurderer ? murdererHero : victimHero;
        this.computerHero = isPlayerMurderer ? victimHero : murdererHero;
        this.battleMap = new BattleMap(n, m, person, computer, personHero, computerHero);
        this.mainMap = mainMap;
    }

    public OwnerType start() {
        GameMenu.printFormattedMessage("битва");

        battleMap.render();
        while (!isBattleOver) {
            personTurn();
            battleMap.render();
            if (isBattleOver) break;
            computerTurn();
            battleMap.render();
        }
        return looser;
    }

    protected void setBattleEnded(Player killedPlayer, Hero killedHero) {
        killedPlayer.kill(killedHero);
        mainMap.kill(killedHero.getY(), killedHero.getX());
        isBattleOver = true;
        looser = killedPlayer.getOwnerType();
        GameMenu.printFightEnd(looser);
    }

    private Unit selectUnit() {
        List<Unit> units = personHero.getUnits();
        if (units.isEmpty()) {
            return null;
        }

        GameMenu.chooseEntity(units, "Юнита");

        int selected = InputHandler.getIntInput() - 1;
        if (selected >= 0 && selected < units.size()) {
            return units.get(selected);
        } else {
            GameMenu.wrongChoice();
            return null;
        }
    }

    private void personTurn() {
        boolean canAtack = false;
        if (personHero.getUnitsCount() == 0) {
            setBattleEnded(person, personHero);
            return;
        }
        while (selectedUnit == null) {
            selectedUnit = selectUnit();
        }
        int y = selectedUnit.getY();
        int x = selectedUnit.getX();

        int[] enemyCords = canPersonAttack(new int[]{y, x}, selectedUnit.getFightDist());

        if (enemyCords != null) {
            canAtack = true;
        }

        GameMenu.showAvailableUnitActions(enemyCords, selectedUnit);
        int action = InputHandler.getIntInput();

        switch (action) {
            case 1:
                while (move(selectedUnit, battleMap, false)) ;
                break;
            case 2:
                GameMenu.println("Ход пропущен.");
                break;
            case 3:
                if (canAtack) {
                    Unit computerUnit = computerHero.getUnit(enemyCords);
                    attack(selectedUnit, computerUnit);
                    break;
                }
                GameMenu.wrongChoice();
            case 4:
                if (selectedUnit.haveSuperAbility() && canAtack) {
                    Unit computerUnit = computerHero.getUnit(enemyCords);
                    useSuperAbility(selectedUnit, computerUnit);
                    break;
                } else {
                    GameMenu.wrongChoice();
                }
            case 0:
                selectedUnit = null;
                personTurn();
            default:
                GameMenu.wrongChoice();
                break;
        }
    }

    public int[] canPersonAttack(int[] pos, int fightDist) {
        HashMap<String, int[]> nearby = checkEnemies(pos[0], pos[1], battleMap, OwnerType.PERSON, fightDist);

        int[] enemyCords = nearby.get("enemy");

        if (enemyCords != null) {
            return enemyCords;
        }
        return null;
    }

    private void computerTurn() {
        GameMenu.println("Ход компьютера");

        Unit computerUnit = selectComputerUnit();

        if (computerUnit == null) {
            setBattleEnded(computer, computerHero);
            return;
        }

        int y = computerUnit.getY();
        int x = computerUnit.getX();

        HashMap<String, int[]> nearby = checkEnemies(y, x, battleMap, OwnerType.COMPUTER, computerUnit.getFightDist());
        int[] enemyCoords = nearby.get("enemy");

        if (enemyCoords != null) {
            if (computerUnit.haveSuperAbility()) {
                GameMenu.println("Компьютер пытается завербовать вашего юнита!");
                Unit personUnit = personHero.getUnit(enemyCoords);
                try {
                    useSuperAbility(computerUnit, personUnit);
                } catch (NullPointerException e) {
                    GameMenu.println("");
                    GameMenu.errorMessage("Не удалось получить юнита по координатам");
                }
                return;
            }

            GameMenu.println("Компьютер атакует врага!");
            Unit personUnit = personHero.getUnit(enemyCoords);
            attack(computerUnit, personUnit);
        } else {
            GameMenu.println("Компьютер перемещает юнита.");
            moveComputerUnit(computerUnit);
        }
    }

    private Unit selectComputerUnit() {
        List<Unit> units = computerHero.getUnits();
        if (units.isEmpty()) {
            return null;
        }

        return units.getFirst();
    }

    private void moveComputerUnit(Unit unit) {
        int y = unit.getY();
        int x = unit.getX();

        HashMap<String, int[]> nearby = checkEnemies(y, x, battleMap, OwnerType.COMPUTER, Math.max(3, unit.getFightDist() + 1));
        int[] enemyCoords = nearby.get("enemy");

        if (enemyCoords != null) {
            GameMenu.println("Двигается к игроку");
            int[] newPos = getDirectionToPosition(y, x, enemyCoords[0], enemyCoords[1]);
            if (battleMap.isCellAvailable(newPos[0], newPos[1], false)) {
                setEntityPos(unit, battleMap, new int[]{y, x});
            }
        } else {
            GameMenu.println("Двигается рандомно");
            move(unit, battleMap, true);
        }
    }

    private void attack(Unit murderer, Unit victim) {
        murderer.attack(victim);
        boolean isAlive = victim.getIsAlive();

        if (!isAlive) {
            GameMenu.println("Юнит " + victim.getName() + " убит");
            Player killedPlayer;
            Hero killedHero;
            if (victim.getOwnerType() == OwnerType.PERSON) {
                killedPlayer = person;
                killedHero = personHero;
            } else {
                killedPlayer = computer;
                killedHero = computerHero;
            }

            killedHero.kill(victim);
            battleMap.kill(victim.getY(), victim.getX());

            if (killedHero.getUnitsCount() == 0) {
                setBattleEnded(killedPlayer, killedHero);
            }
        }
    }

    public void useSuperAbility(Unit recruiter, Unit recruit) {
        String superAbility = recruiter.getUnitType().getSuperAbility();
        if (superAbility != null) {
            switch (superAbility) {
                case "recruiting":
                    int recruitedNumber = recruiter.getRecruitedNumber();
                    double chance = (double) (35 + (9 * recruitedNumber)) / 100;
//                    Вывод кол-ва завербованных и текущий шанс вербовки
//                    GameMenu.println(recruitedNumber + " " + chance);
                    if (Math.random() < chance) {
                        if (recruit.getOwnerType() == OwnerType.PERSON) {
                            if (selectedUnit != null && selectedUnit.equals(recruit)) selectedUnit = null;
                            personHero.kill(recruit);
                            computerHero.addUnit(recruit);
                        } else {
                            computerHero.kill(recruit);
                            personHero.addUnit(recruit);
                        }
                        GameMenu.println(recruit.getName() + " успешно завербован!");
                        recruiter.incrementRecruitedNumber();

                        recruit.reverseOwner();
                        battleMap.reverseCellOwner(recruit.getPos());

                        for (int i = 0; i < person.getHeroes().size(); i++) {
                            person.getHeroes().get(i).display();
                        }

                        for (int i = 0; i < computer.getHeroes().size(); i++) {
                            computer.getHeroes().get(i).display();
                        }

                        return;
                    } else {
                        GameMenu.println("Завербовать Юнита " + recruit.getName() + " не получилось");
                    }
                    break;
                default:
                    GameMenu.errorMessage("ERROR");
            }
        } else {
            GameMenu.println("У этого юнита нет суперспособностей.");
        }
    }

    public Map getMap() {
        return this.battleMap;
    }
}