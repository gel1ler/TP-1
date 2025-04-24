package game.Castle.Buildings;

import game.Castle.Buy;
import game.Player.OwnerType;
import game.Player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Building extends Buy {
    protected int maxVisitors;
    protected List<Visitor> currentVisitors;

    public Building(String name, int cost, int maxVisitors, OwnerType owner) {
        super(name, cost, owner);
        this.maxVisitors = maxVisitors;
        this.currentVisitors = new ArrayList<>();
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" - ").append(currentVisitors.size())
                .append("/").append(maxVisitors).append(" посетителей\n");

        for (Visitor visitor : currentVisitors) {
            long remainingTime = visitor.endTime - gameTime.getCurrentGameTime();
            sb.append("- ").append(visitor.unit.getName()).append(": ")
                    .append(visitor.serviceName).append(" (осталось ")
                    .append(remainingTime).append(" минут)\n");
        }

        return sb.toString();
    }

    public abstract void interact();

    protected class Visitor {
        Player player;
        String serviceName;
        long endTime;

        public Visitor(Player player, String serviceName, long durationMinutes) {
            this.player = player;
            this.serviceName = serviceName;
            this.endTime = gameTime.getCurrentGameTime() + durationMinutes;
        }
    }
}
