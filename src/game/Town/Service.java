package game.Town;

import game.Castle.Buy;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.Menu.GameMenu;

public class Service extends Buy {
    private final long duration;

    public Service(String name, int cost, long duration) {
        super(name, cost, null);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
