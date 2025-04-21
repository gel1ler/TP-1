package game.Player.Entities;

import game.Map.CellType;
public enum UnitType {
    SWORDSMAN("Мечник", 10, 80, 15, 1, 150, CellType.SWORDSMAN, null),
    SPEARMAN("Копейщик", 20, 50, 10, 2, 200, CellType.SPEARMAN, null),
    PALADIN("Паладин", 30, 100, 50, 3, 300, CellType.PALADIN, null),
    CROSSBOWMAN("Арбалетчик", 40, 30, 20, 3, 350, CellType.CROSSBOWMAN, null),
    CAVALRYMAN("Кавалерист", 50, 60, 25, 2, 400, CellType.CAVALRYMAN, null),
    RASCAL("Плут", 70, 25, 0, 200, 0, CellType.RASCAL, "recruiting");

    private final String name;
    private final int cost;
    private final int hp;
    private final int damage;
    private final int fightDist;
    private final int movementPoints;
    private final CellType cellType;
    private final String superAbility;

    UnitType(String name, int cost, int hp, int damage, int fightDist, int movementPoints, CellType cellType, String superAbility) {
        this.name = name;
        this.cost = cost;
        this.hp = hp;
        this.damage = damage;
        this.fightDist = fightDist;
        this.movementPoints = movementPoints;
        this.cellType = cellType;
        this.superAbility = superAbility;
    }

    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public int getHp() {
        return hp;
    }
    public int getDamage() {
        return damage;
    }
    public int getFightDist() {
        return fightDist;
    }
    public int getMovementPoints() {
        return movementPoints;
    }
    public CellType getCellType() {
        return cellType;
    }
    public String getSuperAbility() {
        return superAbility;
    }
}