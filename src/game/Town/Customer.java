package game.Town;

import game.Player.Player;
import game.Utils.GameTime;

public class Customer extends Thread {
    Player player;
    String serviceName;
    long endTime;

    public Customer(String serviceName, long durationMinutes, Player player) {
        this.player = player;
        this.serviceName = serviceName;
        this.endTime = GameTime.getMinutes() + durationMinutes;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void run() {
        super.run();
    }

    public String getPlayerName() {
        return player.getName();
    }
}