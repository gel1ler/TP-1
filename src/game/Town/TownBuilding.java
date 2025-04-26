package game.Town;

import game.Castle.Shop;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.GameTime;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TownBuilding extends Shop<Service> {
    private final String name;
    private final List<Service> availableServices;
    protected int maxVisitors;
    protected List<Customer> currentVisitors;

    public TownBuilding(String name, int maxVisitors, List<Service> availableServices) {
        super(availableServices);
        this.availableServices = availableServices;
        this.maxVisitors = maxVisitors;
        this.currentVisitors = new ArrayList<>();
        this.name = name;
    }

    public Service getRandomService() {
        if (availableServices.isEmpty()) return null;
        return availableServices.get(new Random().nextInt(availableServices.size()));
    }

    protected Service selectService(Player player) {
        int selected;
        List<Service> services = getAvailableItems();
        if (player.getOwnerType() == OwnerType.PERSON) {
            BuildingMenu.println("Вы вошли в " + name);
            BuildingMenu.println(getStatus());
            showAvailableItems(player);
            selected = InputHandler.getIntInput();
            if (selected == 0) return null;
        } else {
            selected = (int) (Math.random() * services.size());
        }

        return services.get(selected - 1);
    }

    protected void interact(Player player, Service service, Runnable onComplete) {
        if (player.isPerson()) buyItem(service, player);
        Customer customer = new Customer(service.getName(), service.getDuration(), player, this, onComplete);
        customer.start();
        currentVisitors.add(customer);
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

    public synchronized void removeVisitor(Customer customer) {
        currentVisitors.remove(customer);
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable(){
        return currentVisitors.size() < maxVisitors;
    }

    public List<Customer> getCurrentVisitors() {
        return currentVisitors;
    }
}
