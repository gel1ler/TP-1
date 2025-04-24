package game.Player;

import game.Castle.Buildings.Tavern.Checkers.Piece;
import game.Castle.Buildings.Tavern.Checkers.PieceType;

import java.util.Arrays;

public enum OwnerType {
    PERSON("person", new PieceType[]{PieceType.BLUE, PieceType.BLUE_KING}),
    COMPUTER("computer", new PieceType[]{PieceType.RED, PieceType.RED_KING});

    private final String owner;
    private final PieceType[] pieceType;

    OwnerType(String owner, PieceType[] pieceType) {
        this.owner = owner;
        this.pieceType = pieceType;
    }

    public String getOwner() {
        return owner;
    }

    public OwnerType getEnemy() {
        return this == OwnerType.PERSON ? OwnerType.COMPUTER : OwnerType.PERSON;
    }

    public boolean isYourPiece(Piece piece) {
        return Arrays.stream(pieceType).anyMatch(i -> i == piece.getPieceType());
    }
}