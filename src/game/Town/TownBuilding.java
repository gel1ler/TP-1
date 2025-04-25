package game.Town;

import game.Castle.Shop;
import game.Player.Player;
import game.Utils.GameTime;
import game.Utils.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class TownBuilding extends Shop<Service> {
    private final String name;
    protected int maxVisitors;
    protected List<Customer> currentVisitors;

    public TownBuilding(String name, int maxVisitors, List<Service> availableServices) {
        super(availableServices);
        this.maxVisitors = maxVisitors;
        this.currentVisitors = new ArrayList<>();
        this.name = name;
    }

    protected void chooseService(Player player){
        showAvailableItems();
        int selected = InputHandler.getIntInput();
        Service service = getAvailableItems().get(selected);
        visit(service.getName(), service.getDuration(), player);
    }

    protected void visit(String serviceName, long durationMinutes, Player player) {
        currentVisitors.add(new Customer(serviceName, durationMinutes, player));
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" - ").append(currentVisitors.size())
                .append("/").append(maxVisitors).append(" посетителей\n");

        for (Customer customer : currentVisitors) {
            long remainingTime = customer.endTime - GameTime.getMinutes();
            sb.append("- ").append(customer.player.getName()).append(": ")
                    .append(customer.serviceName).append(" (осталось ")
                    .append(remainingTime).append(" минут)\n");
        }

        return sb.toString();
    }
}
