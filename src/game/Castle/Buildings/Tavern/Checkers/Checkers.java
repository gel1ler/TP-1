package game.Castle.Buildings.Tavern.Checkers;

import game.Player.OwnerType;
import game.Utils.InputHandler;
import game.Utils.Menu.CheckersMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Checkers {
    private final Board board;
    private final int size;
    private Piece[][] pieces;
    private Piece selectedPiece;
    private final Map<Integer, Integer> piecesRows = Map.of(5, 4, 6, 4, 7, 4, 8, 6, 9, 6, 10, 6);

    public Checkers(int size) {
        this.size = size;
        initPieces();
        this.board = new Board(size);
    }

    //MAIN=======================
    public void start() {
        initPieces();
        gamePlay();
    }

    public void gamePlay() {
        while (true) {
            board.display(pieces);
            if (!personTurn()) break;
            if (isGameOver()) break;

            board.display(pieces);
            computerTurn();
            if (isGameOver()) break;

            CheckersMenu.println("\nComputer is thinking...");
        }
    }

    private boolean isGameOver() {
        boolean flag = false;
        if (isGameOverFor(OwnerType.PERSON)) {
            CheckersMenu.println("Вы проиграли!");
            flag = true;
        }
        if (isGameOverFor(OwnerType.COMPUTER)) {
            CheckersMenu.println("Вы выиграли!");
            flag = true;
        }

        if (flag) CheckersMenu.printFormattedMessage("Игра окончена");

        return flag;
    }

    private boolean isGameOverFor(OwnerType player) {
        return getYourMovablePieces(player).isEmpty();
    }
    //MAIN=======================


    //PERSON=======================
    private boolean personTurn() {
        do {
            if (selectedPiece == null) {
                selectedPiece = selectPersonPiece();
            }

            List<String> availableMoves = getAvailableMoves(OwnerType.PERSON, selectedPiece);
            CheckersMenu.showAvailableMoves(availableMoves);

            int choice = InputHandler.getIntInput();
            if (choice == 10) {
                selectedPiece = null;
            } else if (choice == 100) {
                return false;
            } else if (choice >= 0 && choice < availableMoves.size()) {
                move(availableMoves.get(choice), selectedPiece);
                return true;
            } else {
                CheckersMenu.wrongChoice();
            }
        } while (true);
    }

    private Piece selectPersonPiece() {
        while (true) {
            List<Piece> movablePieces = getYourMovablePieces(OwnerType.PERSON);
            CheckersMenu.showPersonPieces(size, pieces, movablePieces);

            String choice = InputHandler.getStringInput();

            if (choice.length() != 2) {
                CheckersMenu.wrongChoice();
                continue;
            }

            int y = Character.getNumericValue(choice.charAt(0));
            int x = Character.getNumericValue(choice.charAt(1));

            if (!isWithinBoard(y, x)) {
                CheckersMenu.wrongChoice();
                continue;
            }

            Piece selectedPiece = pieces[y][x];
            if (!isMovable(OwnerType.PERSON, selectedPiece)) {
                CheckersMenu.wrongChoice();
                continue;
            }

            return selectedPiece;
        }
    }
    //PERSON=======================


    //COMPUTER=======================
    private void computerTurn() {
        List<Piece> movablePieces = getYourMovablePieces(OwnerType.COMPUTER);

//        int pieceIndex = (int) (Math.random() * movablePieces.size());
        int pieceIndex = 0;

        Piece selected = movablePieces.get(pieceIndex);

        List<String> availableMoves = getAvailableMoves(OwnerType.COMPUTER, selected);
//        int actionIndex = (int) (Math.random() * availableMoves.size());
        int actionIndex = 0;
        String action = availableMoves.get(actionIndex);

        move(action, selected);
    }
    //COMPUTER=======================


    //MOVE=======================
    private boolean isMovable(OwnerType owner, Piece piece) {
        return !getAvailableMoves(owner, piece).isEmpty();
    }

    private boolean canEat(OwnerType owner, Piece piece) {
        List<String> availableMoves = getAvailableMoves(owner, piece);
        return availableMoves.stream().anyMatch(s -> s.contains("eat"));
    }

    private List<Piece> getYourMovablePieces(OwnerType owner) {
        List<Piece> resArr = new ArrayList<>();
        List<Piece> resEatArr = new ArrayList<>();

        for (Piece[] row : pieces) {
            for (Piece piece : row) {
                if (piece != null && owner.isYourPiece(piece) && isMovable(owner, piece)) {
                    if (canEat(owner, piece)) {
                        resEatArr.add(piece);
                    } else {
                        resArr.add(piece);
                    }
                }
            }
        }

        if (!resEatArr.isEmpty()) return resEatArr;
        return resArr;
    }

    private List<String> getAvailableMoves(OwnerType ownerType, Piece piece) {
        List<String> normalMoves = new ArrayList<>();
        List<String> captureMoves = new ArrayList<>();
        boolean isKing = piece.isKing();

        int y = piece.getY();
        int x = piece.getX();

        checkDirection(ownerType, y, x, 1, "bottom", normalMoves, captureMoves);
        if (isKing || getMoveDirection(piece).equals("top")) {
            checkDirection(ownerType, y, x, -1, "top", normalMoves, captureMoves);
        }

        if (!captureMoves.isEmpty())
            return captureMoves;

        if (isKing)
            return normalMoves;

        //Оставить ходы только в основном направлении
        return normalMoves.stream().filter(move -> move.endsWith(getMoveDirection(piece))).collect(Collectors.toList());
    }

    private void checkDirection(OwnerType ownerType, int y, int x, int delta, String direction, List<String> normalMoves, List<String> captureMoves) {
        checkDiagonal(ownerType, y, x, delta, delta, "left", direction, normalMoves, captureMoves);
        checkDiagonal(ownerType, y, x, delta, -delta, "right", direction, normalMoves, captureMoves);
    }

    private void checkDiagonal(OwnerType ownerType, int y, int x, int deltaY, int deltaX, String side, String direction, List<String> normalMoves, List<String> captureMoves) {
        int newY = y + deltaY;
        int newX = x + deltaX;

        if (!isWithinBoard(newY, newX)) return;

        if (pieces[newY][newX] == null) normalMoves.add(side + "_" + direction);

        else if (!ownerType.isYourPiece(pieces[newY][newX])) {
            int jumpY = y + 2 * deltaY;
            int jumpX = x + 2 * deltaX;

            if (isWithinBoard(jumpY, jumpX) && pieces[jumpY][jumpX] == null) {
                captureMoves.add("eat_" + side + "_" + direction);
            }
        }
    }

    private void move(String direction, Piece piece) {
        int y = piece.getY();
        int x = piece.getX();
        int delta = direction.contains("top") ? -1 : 1;
        int newY = y, newX = x;

        switch (direction) {
            case "left_top", "left_bottom":
                newY += delta;
                newX += delta;
                break;
            case "eat_left_top", "eat_left_bottom":
                newY += 2 * delta;
                newX += 2 * delta;
                removeCapturedPiece(y + delta, x + delta);
                break;
            case "right_top", "right_bottom":
                newY += delta;
                newX -= delta;
                break;
            case "eat_right_top", "eat_right_bottom":
                newY += 2 * delta;
                newX -= 2 * delta;
                removeCapturedPiece(y + delta, x - delta);
                break;
            default:
                CheckersMenu.errorMessage("Неизвестное направление: " + direction);
                return;
        }

        if (!isWithinBoard(newY, newX)) {
            CheckersMenu.errorMessage("Попытка выйти за границы доски: (" + newY + ", " + newX + ")");
            return;
        }

        executeMove(piece, y, x, newY, newX);
        checkPromotionToKing(piece);
    }

    private void executeMove(Piece piece, int oldY, int oldX, int newY, int newX) {
        piece.setPosition(newY, newX);
        pieces[newY][newX] = piece;
        pieces[oldY][oldX] = null;
    }

    private void removeCapturedPiece(int newY, int newX) {
        if (isWithinBoard(newY, newX)) {
            selectedNullIfEquals(pieces[newY][newX]);
            pieces[newY][newX] = null;
        }
    }

    private boolean isOnLastRow(Piece piece) {
        if (OwnerType.PERSON.isYourPiece(piece)) return piece.getY() == 0;
        else {
            return piece.getY() == size - 1;
        }
    }

    private void checkPromotionToKing(Piece piece) {
        if (isOnLastRow(piece) && !piece.isKing()) {
            piece.promoteToKing();
            CheckersMenu.println("Фигура прошла в дамки!");
        }
    }
    //MOVE=======================


    //INIT AND UTILS=======================
    private void initPieces() {
        pieces = new Piece[size][size];
        int rows = piecesRows.get(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                piecesRows.get(size);
                if ((i + j) % 2 == 0) {
                    if (i <= (rows / 2 - 1)) {
                        pieces[i][j] = new Piece(PieceType.RED, i, j);
                    }
                    if (i > size - rows / 2 - 1) {
                        pieces[i][j] = new Piece(PieceType.BLUE, i, j);
                    }
                }
            }
        }
    }

    private String getMoveDirection(Piece piece) {
        if (piece.isKing()) return "both";
        else if (OwnerType.PERSON.isYourPiece(piece)) return "top";
        return "bottom";
    }

    private void selectedNullIfEquals(Piece piece) {
        if (selectedPiece == piece) selectedPiece = null;
    }

    private boolean isWithinBoard(int y, int x) {
        return y >= 0 && y < size && x >= 0 && x < size;
    }
    //INIT AND UTILS=======================
}
