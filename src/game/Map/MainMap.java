package game.Map;

import game.Player.OwnerType;
import game.Player.Entities.Hero;
import game.Player.Player;
import game.Utils.Menu.GameMenu;

import java.util.*;

public class MainMap extends Map {
    public MainMap(int n, int m, Player person, Player computer) {
        super(n, m, person, computer);
        init();
    }

    public MainMap(int n, int m) {
        super(n, m);
        init();
    }

    public boolean hasPlayers() {
        return this.person != null && this.computer != null;
    }

    public String getSizeInString() {
        return n + "x" + m;
    }

    public void setHeroes(int startY, int startX, Player owner) {
        int[][] directions = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        List<Hero> heroes = owner.getHeroes();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startY, startX});

        boolean[][] visited = new boolean[objects.length][objects[0].length];
        visited[startY][startX] = true;
        int placedHeroes = 0;

        while (!queue.isEmpty() && placedHeroes < heroes.size()) {
            int[] current = queue.poll();
            int y = current[0], x = current[1];

            if ((x != startX || y != startY) && (objects[y][x] == null || objects[y][x].empty())) {
                Hero newHero = heroes.get(placedHeroes++);
                objects[y][x] = new Cell(newHero.getCellType(), owner.getOwnerType());
                newHero.setPos(y, x);
            }

            for (int[] dir : directions) {
                int newY = y + dir[0], newX = x + dir[1];
                if (newX >= 0 && newX < objects.length && newY >= 0 && newY < objects[0].length && !visited[newY][newX]) {
                    visited[newY][newX] = true;
                    queue.add(new int[]{newY, newX});
                }
            }
        }

        if (placedHeroes < heroes.size()) {
            GameMenu.println("Недостаточно свободных клеток для размещения всех героев!");
        }
    }

    private void init() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                terrain[i][j] = new Cell(CellType.GRASS);
            }
        }
        divideMap();

        //Castles
        terrain[0][0] = new Cell(CellType.PERSON_CASTLE);
        terrain[n - 1][m - 1] = new Cell(CellType.COMPUTER_CASTLE);
        createRoad();
        terrain[0][m - 1] = new Cell(CellType.HOTEL);
//        createObstacles(20); // Не симметрично
    }

    public void updateHeroes(int startX, int startY) {
        int[][] directions = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        for (Hero hero : person.getHeroes()) {
            boolean[][] visited = new boolean[objects.length][objects[0].length];

            if (hero.getY() == 0 && hero.getX() == 0) {
                Queue<int[]> queue = new LinkedList<>();
                queue.add(new int[]{0, 0});

                while (!queue.isEmpty()) {
                    int[] current = queue.poll();
                    int y = current[0], x = current[1];
                    if ((x != startX || y != startY) && (objects[y][x] == null || objects[y][x].empty())) {
                        objects[y][x] = new Cell(hero.getCellType(), hero.getOwnerType());
                        hero.setPos(y, x);
                        break;
                    }

                    for (int[] dir : directions) {
                        int newY = y + dir[0], newX = x + dir[1];
                        if (newX >= 0 && newX < objects.length && newY >= 0 && newY < objects[0].length && !visited[newY][newX]) {
                            visited[newY][newX] = true;
                            queue.add(new int[]{newY, newX});
                        }
                    }
                }
            }
        }
    }

    public void registerInvasion(Hero hero) {
        objects[hero.getY()][hero.getX()] = null;
        if (hero.getOwnerType() == OwnerType.PERSON) {
            objects[n - 1][m - 1] = new Cell(hero.getCellType(), hero.getOwnerType());
            hero.setPos(n - 1, m - 1);
        } else {
            objects[0][0] = new Cell(hero.getCellType(), hero.getOwnerType());
            hero.setPos(0, 0);
        }
    }

    public void setPlayers(Player person, Player computer) {
        this.person = person;
        this.computer = computer;
    }
}
