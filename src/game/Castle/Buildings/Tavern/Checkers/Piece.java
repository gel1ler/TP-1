package game.Castle.Buildings.Tavern.Checkers;

public class Piece {
    private PieceType type;
    private int x, y;
    private boolean isKing;

    public Piece(PieceType type, int y, int x) {
        this.type = type;
        this.y = y;
        this.x = x;
        this.isKing = (type == PieceType.RED_KING || type == PieceType.BLUE_KING);
    }

    // Геттеры
    public PieceType getPieceType() {
        return type;
    }

    public String getSymbol() {
        return type.getSymbol();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Сеттеры
    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public boolean isKing() {
        return isKing;
    }

    public void promoteToKing() {
        if (type == PieceType.RED) {
            type = PieceType.RED_KING;
        } else if (type == PieceType.BLUE) {
            type = PieceType.BLUE_KING;
        }
        isKing = true;
    }

    @Override
    public String toString() {
        return type.getSymbol();
    }
}