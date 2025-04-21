package game.Castle.Buildings;

import game.Castle.Shop;
import game.Player.Entities.Entity;
import game.Player.Entities.Hero;
import game.Player.Entities.UnitType;
import game.Player.Player;
import game.Player.Entities.Unit;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;
import game.Utils.Menu.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static game.Utils.Menu.GameMenu.displayHeroes;

public class Hub extends Building {
    private final Shop<Entity> shop;
    private final Player owner;

    public Hub(Player owner) {
        super("Хаб", 25, owner.getOwnerType());
        this.shop = new Shop<>(owner, createAvailableItems());
        this.owner = owner;
    }

    private List<Entity> createAvailableItems() {
        List<Entity> availableUnits = new ArrayList<>();
        availableUnits.add(new Unit(UnitType.CAVALRYMAN, null));
        availableUnits.add(new Unit(UnitType.CROSSBOWMAN, null));
        availableUnits.add(new Unit(UnitType.PALADIN, null));
        availableUnits.add(new Unit(UnitType.SPEARMAN, null));
        availableUnits.add(new Unit(UnitType.SWORDSMAN, null));
        availableUnits.add(new Unit(UnitType.RASCAL, null));

        return availableUnits;
    }

    public void buyUnit(Hero hero) {
        shop.showAvailableItems();
        int selected = InputHandler.getIntInput();
        while (selected != 0) {
            Entity item = shop.getAvailableItems().get(selected - 1);
            addUnitToHero((Unit) item, hero);
            shop.showAvailableItems();
            selected = InputHandler.getIntInput();
        }
    }

    public void addUnitToHero(Unit item, Hero hero){
        if (owner.canAfford(item)) {
            shop.buyItem(item);
            hero.addUnit(item);
        } else {
            BuildingMenu.println("Недостаточно золота для покупки: " + item.getName());
        }
    }

    @Override
    public void interact() {
        BuildingMenu.println("Вы вошли в Хаб.");
        List<Hero> heroes = owner.getHeroes();
        displayHeroes(heroes);
        int selected = InputHandler.getIntInput();

        while (selected != 0) {
            buyUnit(heroes.get(selected - 1));
            displayHeroes(heroes);
            selected = InputHandler.getIntInput();
        }
    }
}