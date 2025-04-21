package game.Map;

import DB.Saves.MapSave;
import game.Utils.InputHandler;
import game.Utils.Menu.MainMenu;

import java.util.List;

public class MapEditor {
    private MainMap mainMap;

    public void start() {
        MainMenu.println("1 - Создать новую карту");
        MainMenu.println("2 - Редактировать существующую");

        int selected = InputHandler.getIntInput();

        switch (selected) {
            case 1:
                generateNewMap();
                break;
            case 2:
                mainMap = MapSave.readSave();
                break;
        }

        assert mainMap != null;
        mainMap.renderWithCords();
        while (true) {
            startEdit();
            mainMap.render();

            MainMenu.print("1 - Продолжить\t\t0 - Сохранить карту и выйти");
            int i = InputHandler.getIntInput();
            if (i == 0) break;
        }

        MapSave.writeSave(mainMap);
    }

    private void generateNewMap() {
//        MainMenu.println("Введите высоту");
//        int height = InputHandler.getIntInput();
//        MainMenu.println("Введите ширину");
//        int width = InputHandler.getIntInput();
//
//        this.mainMap = new MainMap(height, width);

//        TEST
        this.mainMap = new MainMap(5, 5);
    }

    public MainMap getMainMap(){
        return mainMap;
    }

    private int[] selectCellCords() {
        MainMenu.println("Введите координаты клетки которую хотите поменять:");
        MainMenu.print("y");
        int y = InputHandler.getIntInput();
        MainMenu.print("x");
        int x = InputHandler.getIntInput();

        return new int[]{y, x};
    }

    private void startEdit() {
        int[] pos = selectCellCords();
        Cell cell = mainMap.getCell(pos);

        MainMenu.println("Выбранная клетка:");
        cell.display();

        MainMenu.println("\nВыберите новый тип клетки:");
        List<CellType> list = CellType.getEditableTypes();
        MainMenu.displayArrays(list);

        int selected = InputHandler.getIntInput();
        cell.update(list.get(selected - 1));
    }
}
