package game.Player.Entities;

import game.Castle.Buy;
import game.Map.CellType;
import game.Player.OwnerType;

public class Entity extends Buy {
    protected CellType cellType;
    private int x = 0;
    private int y = 0;
    private int movementPoints;

    public Entity(String name, int cost, int movementPoints, CellType cellType, OwnerType owner) {
        super(name, cost, owner);
        this.movementPoints = movementPoints;
        this.cellType = cellType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return this.y;
    }

    public void setPos(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void setPos(int[] pos) {
        this.y = pos[0];
        this.x = pos[1];
    }

    public int[] getPos() {
        return new int[]{y, x};
    }

    public int getMP() {
        return this.movementPoints;
    }


    public CellType getCellType() {
        return this.cellType;
    }

    public void plusMP(int mpIncrement) {
        this.movementPoints += mpIncrement;
    }
}
