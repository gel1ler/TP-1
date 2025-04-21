package game.Map;

import game.Player.Entities.Hero;
import game.Player.Entities.Unit;
import game.Player.Player;
import game.Utils.Menu.GameMenu;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BattleMap extends Map {
    public BattleMap(int n, int m, Player person, Player computer, Hero personHero, Hero compHero) {
        super(n, m, person, computer);
        init(personHero, compHero);
        if (personHero == null || compHero == null) {
            throw new IllegalArgumentException("Hero objects cannot be null");
        }
        setUnits(0, 0, personHero);
        setUnits(m - 1, m - 1, compHero);
    }

    private void setUnits(int startX, int startY, Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Hero cannot be null");
        }

        List<Unit> units = hero.getUnits();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startY, startX});

        boolean[][] visited = new boolean[objects.length][objects[0].length];
        visited[startY][startX] = true;
        int placedUnits = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty() && placedUnits < units.size()) {
            int[] current = queue.poll();
            int y = current[0];
            int x = current[1];

            if (x == startX && y == startY) {
                for (int[] dir : directions) {
                    int newY = y + dir[0];
                    int newX = x + dir[1];

                    if (newX >= 0 && newX < objects.length && newY >= 0 && newY < objects[0].length && !visited[newY][newX]) {
                        visited[newY][newX] = true;
                        queue.add(new int[]{newY, newX});
                    }
                }
                continue;
            }

            if (objects[y][x] == null || objects[y][x].empty()) {
                Unit newUnit = units.get(placedUnits);
                objects[y][x] = new Cell(newUnit.getCellType(), newUnit.getOwner());
                units.get(placedUnits).setPos(y, x);
                placedUnits++;
            }

            // Добавляем соседние клетки в очередь
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // Проверяем, что новые координаты в пределах массива и клетка не посещена
                if (newX >= 0 && newX < objects.length && newY >= 0 && newY < objects[0].length && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.add(new int[]{newX, newY});
                }
            }
        }

        // Если героев больше, чем свободных клеток
        if (placedUnits < units.size()) {
            GameMenu.println("Недостаточно свободных клеток для размещения всех Юнитов!");
        }
    }

    protected void init(Hero personHero, Hero compHero) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                terrain[i][j] = new Cell(CellType.FOG);
            }
        }
        divideMap();

        //Heroes
        terrain[0][0] = new Cell(personHero.getCellType());
        terrain[n - 1][m - 1] = new Cell(compHero.getCellType());
        createRoad();
    }


}
