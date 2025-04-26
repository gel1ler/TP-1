package game.Castle.Buildings.Tavern;

import game.Castle.Buildings.Building;
import game.Castle.Buildings.Tavern.Checkers.Checkers;
import game.Castle.Shop;
import game.Player.Entities.Hero;
import game.Player.Entities.Entity;
import game.Player.Entities.HeroType;
import game.Player.Player;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;

import java.util.ArrayList;
import java.util.List;

public class Tavern extends Building {
    private final Shop<Entity> shop;
    private final Player owner;

    public Tavern(Player owner) {
        super("Таверна", 50, owner); // Вызов конструктора Building
        this.shop = new Shop<>(owner, createAvailableItems());
        this.owner = owner;
    }

    private List<Entity> createAvailableItems() {
        List<Entity> availableHeroes = new ArrayList<>();
        availableHeroes.add(new Hero(HeroType.BARBARIAN, null));
        availableHeroes.add(new Hero(HeroType.KNIGHT, null));
        availableHeroes.add(new Hero(HeroType.WIZARD, null));
        return availableHeroes;
    }

    public void buyHero() {
        shop.showAvailableItems();

        int selected = InputHandler.getIntInput();
        while (selected != 0) {
            Entity item = shop.getAvailableItems().get(selected - 1);
            addHeroToOwner((Hero) item);
            shop.showAvailableItems();
            selected = InputHandler.getIntInput();
        }
    }

    public void addHeroToOwner(Hero item) {
        if (owner.canAfford(item)) {
            shop.buyItem(item);
            owner.addHero(new Hero((item).getHeroType(), owner));
        } else {
            BuildingMenu.println("Недостаточно золота для покупки: " + item.getName());
        }
    }

    @Override
    public void interact() {
        BuildingMenu.println("Вы вошли в Таверну.");

        BuildingMenu.println("1 - Купить героя\t\t2 - Поиграть в шашки\t\t0 - Выход");
        int selected = InputHandler.getIntInput();

        while (selected != 0) {
            switch (selected) {
                case 1:
                    buyHero();
                    break;
                case 2:
                    BuildingMenu.println("Играть в Шашки");
                    new Checkers(6).start();
                    break;
            }
            BuildingMenu.println("1 - Купить героя\t\t2 - Поиграть в шашки\t\t0 - Выход");
            selected = InputHandler.getIntInput();
        }
    }
}