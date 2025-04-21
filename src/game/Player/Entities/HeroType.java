package game.Player.Entities;

import game.Map.Cell;
import game.Map.CellType;

public enum HeroType {
    KNIGHT("Рыцарь", 80, 480, CellType.KNIGHT),
    WIZARD("Маг", 100, 450, CellType.WIZARD),
    BARBARIAN("Варвар", 60, 550, CellType.BARBARIAN);

    private final String name;
    private final int cost;
    private final int movementPoints;
    private final CellType cellType;


    HeroType(String name, int cost, int movementPoints, CellType cellType) {
        this.name = name;
        this.cost = cost;
        this.movementPoints=movementPoints;
        this.cellType = cellType;
    }

    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public int getMovementPoints() {
        return movementPoints;
    }
    public CellType getCellType() {
        return cellType;
    }
}