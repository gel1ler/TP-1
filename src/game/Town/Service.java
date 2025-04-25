package game.Town;

import game.Castle.Buy;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.Menu.GameMenu;

public class Service extends Buy {
    private long duration;

    public Service(String name, int cost, long duration, Player owner) {
        super(name, cost, owner);
        this.duration = duration;
    }

    public void use() {
        GameMenu.println("Вы используюте сервис " + name);
        //thread
    }

    public long getDuration() {
        return duration;
    }
}
