package game.Map;

import game.Player.OwnerType;
import game.Utils.Menu.GameMenu;

import java.io.Serializable;

public class Cell implements Serializable {
    private CellType type;
    private int penalty;
    private String icon;
    private OwnerType owner;

    public Cell(CellType type) {
        if (type == CellType.GRASS && Math.random() >= 0.66)
            this.type = CellType.GOLD;
        else
            this.type = type;

        this.penalty = this.type.getPenalty();
        this.icon = this.type.getIcon();
    }

    public Cell(CellType type, OwnerType owner) {
        this.type = type;
        this.owner = owner;
        this.penalty = this.type.getPenalty();
        this.icon = this.type.getIcon();
    }

    public void reverseOwner() {
        if (owner == OwnerType.COMPUTER) owner = OwnerType.PERSON;
        else owner = OwnerType.COMPUTER;
    }

    public CellType getType() {
        try {
            return type;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean empty() {
        return type == null;
    }

    public int getPenalty() {
        return penalty;
    }

    @Override
    public String toString() {
        return icon;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public OwnerType getOwner() {
        return owner;
    }

    public void display(){
        GameMenu.println("Тип (" + getType() + ")");
        GameMenu.println("Иконка (" + this + ")");
        GameMenu.println("Штраф за перемещение (" + getPenalty() + ")");
    }

    public void update(CellType type){
        this.type = type;
        this.penalty = this.type.getPenalty();
        this.icon = this.type.getIcon();
    }
}