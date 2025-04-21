package game.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CellType {
    GRASS(150, "🌿"),
    ROAD(75, "🟫"),
    OBSTACLE(Integer.MAX_VALUE, "🚧"),
    PERSON_CASTLE(0, "🏰"),
    COMPUTER_CASTLE(0, "\uD83C\uDFEF"),
    PERSON_ZONE(100, "\uD83D\uDD34"), // синий
    COMPUTER_ZONE(100, "\uD83D\uDD35"),
    //Heroes
    KNIGHT(0, "\uD83D\uDEE1"),
    WIZARD(0, "\uD83E\uDDD9\u200D"),
    BARBARIAN(0, "\uD83E\uDE93"),
    SWORDSMAN(0, "⚔️"),
    SPEARMAN(0, "\uD83E\uDE96"),
    PALADIN(0, "✝️"),
    CROSSBOWMAN(0, "\uD83C\uDFF9"),
    CAVALRYMAN(0, "\uD83D\uDC0E"),
    RASCAL(0, "\uD83E\uDD8A"),
    FOG(150, "\uD83C\uDF2B"),
    GOLD(150, "\uD83C\uDF15");

    private final int penalty;
    private final String icon;

    CellType(int penalty, String icon) {
        this.penalty = penalty;
        this.icon = icon;
    }

    public int getPenalty() {
        return penalty;
    }

    public String getIcon() {
        return icon;
    }

    public static List<CellType> getEditableTypes(){
        List<CellType> elementsToAdd = Arrays.asList(GRASS, GOLD, FOG, COMPUTER_ZONE, PERSON_ZONE);
        return new ArrayList<>(elementsToAdd);
    }
}