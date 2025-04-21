package game.Utils.Menu;

import game.Castle.Buildings.Tavern.Checkers.Piece;
import game.Player.OwnerType;

import java.util.ArrayList;
import java.util.List;

public class CheckersMenu extends Menu {
    public static void showAvailableMoves(List<String> availableMoves) {
        for (int i = 0; i < availableMoves.size(); i++) {
            BuildingMenu.print(i + " - " + availableMoves.get(i) + "\t\t");
        }
        BuildingMenu.print("10 - Выбрать другую шашку\t\t100 - Закончить игру");
        BuildingMenu.println("");
    }

    public static void showPersonPieces(int size, Piece[][] pieces, List<Piece> movablePieces) {
        BuildingMenu.println("");
        for (int rowI = 0; rowI < size; rowI++) {
            List<String> rowArr = new ArrayList<>();
            boolean notNullRow = false;
            for (int colI = 0; colI < size; colI++) {
                Piece temp = pieces[rowI][colI];

                if (temp != null && temp.getPieceType() == OwnerType.PERSON.getPieceType()) {
                    if (movablePieces.contains(temp))
                        rowArr.add(temp.toString() + rowI + colI);
                    else
                        rowArr.add(temp + "xx");
                    notNullRow = true;
                } else {
                    rowArr.add("   ");
                }
            }
            if (notNullRow) {
                for (String i : rowArr) {
                    BuildingMenu.print(i);
                }
                BuildingMenu.println("");
            }
        }
    }
}
