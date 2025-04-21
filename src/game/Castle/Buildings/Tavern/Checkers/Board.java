package game.Castle.Buildings.Tavern.Checkers;

import game.Utils.Menu.BuildingMenu;

public class Board {
    private int size;

    public Board(int size) {
        this.size = size;
    }

    protected void display(Piece[][] pieces) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 == 1) {
                    BuildingMenu.print("⬜\uFE0F ");
                } else {
                    BuildingMenu.print(pieces[i][j] == null ? "⬛\uFE0F " : pieces[i][j] + " ");
                }
            }
            BuildingMenu.println("");
        }
    }
}
