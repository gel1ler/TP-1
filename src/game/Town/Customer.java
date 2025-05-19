package game.Town;

import game.Player.Player;
import game.Utils.GameTime;
import game.Utils.Menu.GameMenu;

import java.io.Serializable;

import static game.Utils.GameTime.GAME_MINUTE_IN_MS;

public class Customer extends Thread implements Serializable {
    private final TownBuilding building;
    Player player;
    String serviceName;
    long endTime;
    private final Runnable onComplete;

    public Customer(String serviceName, long durationMinutes, Player player, TownBuilding building, Runnable onComplete) {
        this.player = player;
        this.serviceName = serviceName;
        this.endTime = GameTime.getMinutes() + durationMinutes;
        this.building = building;
        this.onComplete = onComplete;
        player.makeBusy(this);
    }

    public long getRemains() {
        return endTime - GameTime.getMinutes();
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public void run() {
        try {
            long remainingTime = getRemains();
            if (remainingTime > 0) {
                Thread.sleep(remainingTime * GAME_MINUTE_IN_MS);
            }
            onServiceCompleted();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            GameMenu.println("Service was interrupted for player: " + getPlayerName());
        }
    }

    private void onServiceCompleted() {
        onComplete.run();
        player.makeFree();
        building.removeVisitor(this);
        if (player.isPerson())
            GameMenu.println("Сервис '" + serviceName + "' завершен для игрока: " + getPlayerName());
    }


}