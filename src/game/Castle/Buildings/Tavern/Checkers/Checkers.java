package game.Castle.Buildings.Tavern.Checkers;

import game.Player.OwnerType;
import game.Utils.InputHandler;
import game.Utils.Menu.CheckersMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Checkers {
    private final Board board;
    private final int size;
    private Piece[][] pieces;
    private Piece selectedPiece;
    private OwnerType winner = null;
    private final Map<Integer, Integer> piecesRows = Map.of(
            5, 4,
            6, 4,
            7, 4,
            8, 6,
            9, 6,
            10, 6
    );

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

        if (flag)
            CheckersMenu.printFormattedMessage("Игра окончена");

        return flag;
    }

    private boolean isGameOverFor(OwnerType player) {
        CheckersMenu.println(getYourMovablePieces(player).toString());
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

            if (isCoordsOutOfBounds(y, x)) {
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

        if (movablePieces.isEmpty()) {
            winner = OwnerType.PERSON;
            return;
        }

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


    //GLOBAL=======================

    private boolean isMovable(OwnerType owner, Piece piece) {
        return !getAvailableMoves(owner, piece).isEmpty();
    }

    private boolean canEat(OwnerType owner, Piece piece) {
        List<String> availableMoves = getAvailableMoves(owner, piece);
        return availableMoves.contains("eat_left") || availableMoves.contains("eat_right");
    }

    private List<Piece> getYourMovablePieces(OwnerType owner) {
        List<Piece> resArr = new ArrayList<>();
        List<Piece> resEatArr = new ArrayList<>();

        for (Piece[] row : pieces) {
            for (Piece piece : row) {
                if (piece != null
                        &&
                        owner.isYourPiece(piece)
                        &&
                        isMovable(owner, piece)
                ) {
                    if (canEat(owner, piece)) {
                        resEatArr.add(piece);
                    } else {
                        resArr.add(piece);
                    }
                }
            }
        }

        if (!resEatArr.isEmpty())
            return resEatArr;
        return resArr;
    }

    private List<String> getAvailableMoves(OwnerType ownerType, Piece piece) {
        String moveDirection = getMoveDirection(piece);

        int y = piece.getY();
        int x = piece.getX();

        List<String> res = new ArrayList<>();
        List<String> resEat = new ArrayList<>();

        int delta = 1;
        int iterations = 1;

        if (moveDirection.equals("top"))
            delta = -1;
        if (moveDirection.equals("both"))
            iterations = 2;

        int i = 0;
        while (i < iterations) {

            //LEFT
            if (isLegalMove(y, x, delta, "left")) {
                if (pieces[y + delta][x + delta] == null) {
                    res.add("left_" + moveDirection);
                } else if (isLegalMove(y, x, delta * 2, "left") && //Идешь не за пределы
                        !ownerType.isYourPiece(pieces[y + delta][x + delta]) && //Бьешь не свое
                        pieces[y + 2 * delta][x + 2 * delta] == null
                ) {
                    resEat.add("eat_left_" + moveDirection);
                }

            }

            //RIGHT
            if (isLegalMove(y, x, delta, "right")) {
                if (pieces[y + delta][x - delta] == null) {
                    if (arrCantEat(res))
                        res.add("right_" + moveDirection);
                } else if (isLegalMove(y, x, delta * 2, "right") &&
                        !ownerType.isYourPiece(pieces[y + delta][x - delta]) &&
                        pieces[y + 2 * delta][x - 2 * delta] == null) {
                    removeBasic(res);
                    resEat.add("eat_right_" + moveDirection);
                }
            }
            i++;
            delta = -delta;
        }

        if (!resEat.isEmpty())
            return resEat;
        return res;
    }

    private boolean arrCantEat(List<String> arr) {
        for (String i : arr) {
            if (i.contains("eat")) {
                return false;
            }
        }
        return true;
    }

    private void removeBasic(List<String> arr) {
        arr.removeIf(i -> !i.contains("eat"));
    }

    private void move(String direction, Piece piece) {
        String moveDirection = getMoveDirection(piece);
        int delta = 1;

        if (moveDirection.equals("top"))
            delta = -1;

        int y = piece.getY();
        int x = piece.getX();
        int newY, newX;
        switch (direction) {
            case "left_top":
                newY = y + delta;
                newX = x + delta;
                break;
            case "eat_left_top":
                newY = y + 2 * delta;
                newX = x + 2 * delta;
                selectedNullIfEquals(pieces[y + delta][x + delta]);
                pieces[y + delta][x + delta] = null;
                break;
            case "right_top":
                newY = y - delta;
                newX = x - delta;
                break;
            case "eat_right_top":
                newY = y + 2 * delta;
                newX = x - 2 * delta;
                selectedNullIfEquals(pieces[y + delta][x - delta]);
                pieces[y + delta][x - delta] = null;
                break;
            default:
                newY = y;
                newX = x;
        }

        try {
            piece.setPosition(newY, newX);
            pieces[newY][newX] = pieces[y][x];
            if (x != newX && y != newY)
                pieces[y][x] = null;
        } catch (ArrayIndexOutOfBoundsException e) {
            CheckersMenu.println(piece.getY() + ", " + piece.getX());
            CheckersMenu.errorMessage("Попытка выйти за границы доски: (" + newY + ", " + newX + ")");
        }
        if (isOnLastRow(piece) && !piece.isKing()) {
            piece.promoteToKing();
            CheckersMenu.println("Фигура прошла в дамки!");
        }
    }

    private boolean isOnLastRow(Piece piece) {
        if (OwnerType.PERSON.isYourPiece(piece))
            return piece.getY() == 0;
        else {
            return piece.getY() == size - 1;
        }
    }

    //GLOBAL=======================


    //INIT AND UTILS=======================

    private void initPieces() {
        pieces = new Piece[size][size];
//        int rows = piecesRows.get(size);
        int rows = 2;

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

    private boolean isCoordsOutOfBounds(int y, int x) {
        return y < 0 || y >= size || x < 0 || x >= size;
    }

    private String getMoveDirection(Piece piece) {
        if (piece.isKing())
            return "both";
        else if (OwnerType.PERSON.isYourPiece(piece))
            return "top";
        return "bottom";
    }

    private void selectedNullIfEquals(Piece piece) {
        if (selectedPiece == piece)
            selectedPiece = null;
    }

    private boolean isLegalMove(int y, int x, int delta, String dir) {
        if (dir.equals("left"))
            return y + delta >= 0 && y + delta < size &&
                    x + delta >= 0 && x + delta < size;
        else if (dir.equals("right")) {
            return y + delta >= 0 && y + delta < size &&
                    x - delta >= 0 && x - delta < size;
        } else {
            CheckersMenu.errorMessage("Неправильное направление");
            return true;
        }
    }

    //INIT AND UTILS=======================
}
