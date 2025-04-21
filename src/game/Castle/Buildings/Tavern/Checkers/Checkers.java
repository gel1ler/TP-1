package game.Castle.Buildings.Tavern.Checkers;

import game.Player.OwnerType;
import game.Utils.InputHandler;
import game.Utils.Menu.CheckersMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Checkers {
    private Board board;
    private int size;
    private Piece[][] pieces;
    private Piece selectedPiece;
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

            if (!personTurn())
                break;
            board.display(pieces);

            computerTurn();
            CheckersMenu.println("\nComputer is thinking...");
            CheckersMenu.sleep(1500);
        }
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
                if (isOnLastRow(selectedPiece)) {
                    selectedPiece.promoteToKing();
                    CheckersMenu.println("Ваша фигура прошла в дамки!");
                }
                ;
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
            for (Piece i : row) {
                if (i != null
                        &&
                        i.getPieceType() == owner.getPieceType()
                        &&
                        isMovable(owner, i)
                ) {
                    if (canEat(owner, i)) {
                        resEatArr.add(i);
                    } else {
                        resArr.add(i);
                    }
                }
            }
        }

        if (!resEatArr.isEmpty())
            return resEatArr;
        return resArr;
    }

    private List<String> getAvailableMoves(OwnerType ownerType, Piece piece) {
        int m = getBoardDirection(ownerType);

        int y = piece.getY();
        int x = piece.getX();

        List<String> res = new ArrayList<>();

        int iterations = piece.isKing() ? 2 : 1;

        int i = 0;
        while (i < iterations) {
            String direction = m == 1 ? "_bottom" : "_top";
            String temp = iterations == 2 ? direction : "";
            try {
                if (pieces[y + m][x + m] == null) {
                    if (arrCantEat(res))
                        res.add("left" + temp);
                } else if (pieces[y + m][x + m].getPieceType() != ownerType.getPieceType() && pieces[y + 2 * m][x + 2 * m] == null) {
                    removeBasic(res);
                    res.add("eat_left" + temp);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            try {
                if (pieces[y + m][x - m] == null) {
                    if (arrCantEat(res))
                        res.add("right" + temp);
                } else if (pieces[y + m][x - m].getPieceType() != ownerType.getPieceType() && pieces[y + 2 * m][x - 2 * m] == null) {
                    removeBasic(res);
                    res.add("eat_right" + temp);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            i++;
            m = -m;
        }

        return res;
    }

    private boolean arrCantEat(List<String> arr) {
        for (String i:arr) {
            if(i.contains("eat")){
                return false;
            }
        }
        return true;
    }

    private void removeBasic(List<String> arr) {
        arr.removeIf(i -> !i.contains("eat"));
    }

    private void move(String direction, Piece piece) {
        int m = getBoardDirection(piece);

        int y = piece.getY();
        int x = piece.getX();
        int newY, newX;
        switch (direction) {
            case "left":
                newY = y + m;
                newX = x + m;
                break;
            case "eat_left":
                newY = y + 2 * m;
                newX = x + 2 * m;
                if (selectedPiece == pieces[y + m][x + m])
                    selectedPiece = null;
                pieces[y + m][x + m] = null;
                break;
            case "right":
                newY = y + m;
                newX = x - m;
                break;
            case "eat_right":
                newY = y + 2 * m;
                newX = x - 2 * m;
                if (selectedPiece == pieces[y + m][x - m])
                    selectedPiece = null;
                pieces[y + m][x - m] = null;
                break;
            default:
                newY = y;
                newX = x;
        }

        piece.setPosition(newY, newX);
        pieces[newY][newX] = pieces[y][x];
        if (x != newX && y != newY)
            pieces[y][x] = null;
    }

    private boolean isOnLastRow(Piece piece) {
        if (piece.getPieceType() == OwnerType.PERSON.getPieceType())
            return piece.getY() == 0;
        else {
            return piece.getY() == size - 1;
        }
    }

    //GLOBAL=======================


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

    private boolean isCoordsOutOfBounds(int y, int x) {
        return y < 0 || y >= size || x < 0 || x >= size;
    }

    private int getBoardDirection(OwnerType ownerType) {
        if (ownerType == OwnerType.PERSON)
            return -1;
        return 1;
    }

    private int getBoardDirection(Piece piece) {
        if (piece.getPieceType() == OwnerType.PERSON.getPieceType())
            return -1;
        return 1;
    }

    //INIT AND UTILS=======================
}
