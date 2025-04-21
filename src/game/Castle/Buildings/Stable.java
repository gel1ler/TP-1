package game.Castle.Buildings;

import game.Player.OwnerType;
import game.Player.Entities.Hero;
import game.Player.Player;
import game.Utils.InputHandler;
import game.Utils.Menu.BuildingMenu;

import java.util.List;

import static game.Utils.Menu.GameMenu.displayHeroes;

public class Stable extends Building {
    private final Player owner;

    public Stable(Player owner) {
        super("Конюшня", 50, OwnerType.PERSON);
        this.owner = owner;
    }

    @Override
    public void interact() {
        BuildingMenu.println("Вы вошли в Конюшню.");
        List<Hero> heroes = owner.getHeroes();
        displayHeroes(heroes);
        int selected = InputHandler.getIntInput();

        while (selected != 0) {
            upgradeHero(heroes.get(selected - 1));
            displayHeroes(heroes);
            selected = InputHandler.getIntInput();
        }
    }

    private void upgradeHero(Hero hero) {
        BuildingMenu.println("Кол-во золота:" + owner.getGold() + "\t\t10 очков передвижения стоит 1 золото. Введите кол-во очков которое хотите купить:");
        int mp = InputHandler.getIntInput();
        applyUpgrade(hero, mp);
    }

    public void applyUpgrade(Hero hero, int mp){
        if (owner.canAfford(mp / 10)) {
            hero.plusMP(mp);
            owner.minusGold(mp / 10);
            BuildingMenu.println("Улучшение применено");
        }
        else
            BuildingMenu.println("Недостаточно золота(");
    }
}