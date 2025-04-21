package game.Player;

import game.Castle.Buildings.Tavern.Checkers.PieceType;

public enum OwnerType {
    PERSON("person", PieceType.BLUE),
    COMPUTER("computer", PieceType.RED);

    private final String owner;
    private final PieceType pieceType;

    OwnerType(String owner, PieceType pieceType) {
        this.owner = owner;
        this.pieceType = pieceType;
    }

    public String getOwner() {
        return owner;
    }

    public OwnerType getEnemy() {
        return this == OwnerType.PERSON ? OwnerType.COMPUTER : OwnerType.PERSON;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}