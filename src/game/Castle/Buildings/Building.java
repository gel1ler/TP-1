package game.Castle.Buildings;

import game.Castle.Buy;
import game.Town.Customer;
import game.Player.Player;

import java.util.ArrayList;
import java.util.List;

import game.Utils.GameTime;

public abstract class Building extends Buy {
    protected int maxVisitors;
    protected List<Customer> currentVisitors;
    protected Player owner;

    public Building(String name, int cost, int maxVisitors, Player owner) {
        super(name, cost, owner);
        this.owner = owner;
        this.maxVisitors = maxVisitors;
        this.currentVisitors = new ArrayList<>();
    }

    protected void visit(String serviceName, long durationMinutes) {
        currentVisitors.add(new Customer(serviceName, durationMinutes, owner));
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" - ").append(currentVisitors.size())
                .append("/").append(maxVisitors).append(" посетителей\n");

        for (Customer customer : currentVisitors) {
            long remainingTime = customer.getEndTime() - GameTime.getMinutes();
            sb.append("- ").append(customer.getPlayerName()).append(": ")
                    .append(customer.getServiceName()).append(" (осталось ")
                    .append(remainingTime).append(" минут)\n");
        }

        return sb.toString();
    }

    public abstract void interact();

}
