package game.Town;

import game.Battle;
import game.Castle.Shop;
import game.Player.Entities.Hero;
import game.Player.OwnerType;
import game.Player.Player;
import game.Utils.GameTime;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

import static game.Town.NpcManager.addToFreeNpc;

public abstract class TownBuilding extends Shop<Service> {
    private final String name;
    private final List<Service> availableServices;
    protected int maxVisitors;
    protected List<Customer> currentVisitors;
    private final Queue<Player> queue = new LinkedList<>();

    protected abstract void performAction(Player player, Service service);

    public TownBuilding(String name, int maxVisitors, List<Service> availableServices) {
        super(availableServices);
        this.availableServices = availableServices;
        this.maxVisitors = maxVisitors;
        this.currentVisitors = new ArrayList<>();
        this.name = name;
    }

    public void enter(Player player, boolean auto) {
        if (player.isBusy()) {
            if (!auto) BuildingMenu.println("Вы уже пользуетесь какой-то услугой. Приходите, когда завершите её.");
            return;
        }

        if (!isAvailable()) {
            getInLine(player);
            if (!auto) {
                BuildingMenu.println(getName() + " в данный момент недоступен. Вы встали в очередь.");
                BuildingMenu.checkQueuePosition(queue);
                waitInQueue(player);
            }
        } else {
            Service service = selectService(player);
            if (service == null) return;
            interact(player, service, (SerializableRunnable) () -> {
                performAction(player, service);
                if(!player.isPerson()) addToFreeNpc((Npc) player);
            });
        }
    }

    private synchronized void waitInQueue(Player person) {
        try {
            while (!isAvailable() || queue.peek() != person) {
                if (!queue.contains(person)) {
                    BuildingMenu.println("Вы покинули очередь.");
                    return;
                }

                wait(1000);
                if (isAvailable() && !queue.isEmpty() && queue.peek() == person) {
                    break;
                }

                if (Math.random() < 0.1 && queue.size() > 1) {
                    GameTime.pause();
                    startBattle(person);
                    GameTime.resume();
                }
            }

            if (isAvailable() && !queue.isEmpty() && queue.peek() == person) {
                queue.poll();
                Service service = selectService(person);
                if (service != null) {
                    interact(person, service, (SerializableRunnable) () -> performAction(person, service));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            BuildingMenu.println("Ожидание прервано.");
            queue.remove(person);
        }
    }

    public void startBattle(Player person) {
        List<Player> npcsInQueue = queue.stream()
                .filter(p -> p.getOwnerType() != OwnerType.PERSON) // Только NPC
                .toList();
        Player npc = npcsInQueue.get(new Random().nextInt(npcsInQueue.size()));

        Hero personHero = person.getHeroes().getFirst();
        Hero npcHero = npc.getHeroes().getFirst();

        Battle battle = new  Battle(5, 5, person, npc, personHero, npcHero);
        Hero looser = battle.start();

        while (!battle.getIsBattleOver()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (looser.getOwnerType() != OwnerType.PERSON) {
            queue.remove(npc);
            BuildingMenu.println("Вы победили " + npc.getName() + "! Он покидает очередь.");
        } else {
            queue.remove(person);
            BuildingMenu.println("Вы проиграли " + npc.getName() + " и покидаете очередь.");
        }
    }

    public Service getRandomService() {
        if (availableServices.isEmpty()) return null;
        return availableServices.get(new Random().nextInt(availableServices.size()));
    }

    protected Service selectService(Player player) {
        if (player.getOwnerType() != OwnerType.PERSON) {
            return getRandomService();
        }

        BuildingMenu.println("Вы вошли в " + name);
        BuildingMenu.println(getStatus());
        showAvailableItems(player);

        int selected = InputHandler.getIntInput();
        if (selected == 0) return null; // Выход

        try {
            return getAvailableItems().get(selected - 1);
        } catch (IndexOutOfBoundsException e) {
            BuildingMenu.errorMessage("Неверный выбор");
            return selectService(player);
        }
    }

    protected void interact(Player player, Service service, SerializableRunnable onComplete) {
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
            long remainingTime = customer.getRemains();
            sb.append("- ").append(customer.player.getName()).append(": ")
                    .append(customer.serviceName).append(" (осталось ")
                    .append(remainingTime).append(" минут)\n");
        }

        return sb.toString();
    }

    public synchronized void removeVisitor(Customer customer) {
        currentVisitors.remove(customer);
        notifyAll();
        getNextFromQueue();
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return currentVisitors.size() < maxVisitors;
    }

    public List<Customer> getCurrentVisitors() {
        return currentVisitors;
    }

    public synchronized void getInLine(Player player) {
        queue.offer(player);
    }

    private synchronized void getNextFromQueue() {
        if (!queue.isEmpty() && isAvailable()) {
            Player nextPlayer = queue.peek();
            if (nextPlayer != null) {
                if (queue.stream().anyMatch(Player::isPerson))
                    BuildingMenu.checkQueuePosition(queue);
                if (!nextPlayer.isPerson()) {
                    queue.poll();
                    enter(nextPlayer, true);
                }
                notifyAll();
            }
        }
    }
}
