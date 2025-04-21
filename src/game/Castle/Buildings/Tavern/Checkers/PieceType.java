package game.Castle.Buildings.Tavern.Checkers;

public enum PieceType {
    RED("🔴"),
    BLUE("🔵"),
    RED_KING("👑"),
    BLUE_KING("🐝");

    private final String symbol;

    PieceType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

