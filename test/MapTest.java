import DB.Saves.MapSave;
import game.Map.MainMap;
import game.Map.MapEditor;
import game.Utils.Logs.GameLogger;
import game.Utils.InputHandler;
import game.Utils.Menu.MainMenu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MapTest {
    private MainMap mainMap;
    private InputHandler inputHandler;
    private MainMenu mainMenu;
    private MapSave mapSave;
    private MapEditor mapEditor;

    @Before
    public void setUp() {
        GameLogger.info("Map test ended");
//        mainMap = new MainMap(5, 5);
//        mainMap, inputHandler, mainMenu, mapSave;
    }

    @After
    public void end(){
        GameLogger.info("Map test ended");
    }

    @Test
    public void testGenerateNewMapPrivateMethod() throws Exception {
        MapEditor editor = new MapEditor();

        Method generateNewMap = MapEditor.class.getDeclaredMethod("generateNewMap");
        Method getMainMap = MapEditor.class.getDeclaredMethod("getMainMap");
        generateNewMap.setAccessible(true);
        getMainMap.setAccessible(true);

        generateNewMap.invoke(editor);

        assertNotNull(editor.getMainMap());
    }

//
//    @Test
//    public void testEditExistingMap() {
//        try (MockedStatic<InputHandler> mockedInput = mockStatic(InputHandler.class);
//             MockedStatic<MapSave> mockedSave = mockStatic(MapSave.class)) {
//
//            // Arrange
//            mockedInput.when(InputHandler::getIntInput).thenReturn(2);
//            mockedSave.when(MapSave::readSave).thenReturn(mainMap);
//
//            // Act
//            mapEditor.start();
//
//            // Assert
//            verify(mainMap).renderWithCords();
//            verify(mainMap, atLeastOnce()).render();
//        }
//    }
//
//    @Test
//    public void testSelectCellCoordinates() {
//        try (MockedStatic<InputHandler> mockedInput = mockStatic(InputHandler.class)) {
//            // Arrange
//            mockedInput.when(InputHandler::getIntInput)
//                    .thenReturn(2)  // y coordinate
//                    .thenReturn(3); // x coordinate
//
//            // Act
//            int[] result = mapEditor.selectCellCords();
//
//            // Assert
//            assertArrayEquals(new int[]{2, 3}, result);
//            verify(mainMenu, times(1)).println("Введите координаты клетки которую хотите поменять:");
//        }
//    }
//
//    @Test
//    public void testStartEdit() {
//        try (MockedStatic<InputHandler> mockedInput = mockStatic(InputHandler.class)) {
//            // Arrange
//            Cell mockCell = mock(Cell.class);
//            List<CellType> mockTypes = List.of(CellType.GRASS, CellType.WATER);
//
//            when(mainMap.getCell(any())).thenReturn(mockCell);
//            when(CellType.getEditableTypes()).thenReturn(mockTypes);
//            mockedInput.when(InputHandler::getIntInput)
//                    .thenReturn(1)  // y coordinate
//                    .thenReturn(1)  // x coordinate
//                    .thenReturn(1); // cell type selection
//
//            // Act
//            mapEditor.startEdit();
//
//            // Assert
//            verify(mockCell).display();
//            verify(mockCell).update(mockTypes.get(0));
//            verify(mainMenu).println("Выбранная клетка:");
//        }
//    }
//
//    @Test
//    public void testSaveAndExit() {
//        try (MockedStatic<InputHandler> mockedInput = mockStatic(InputHandler.class);
//             MockedStatic<MapSave> mockedSave = mockStatic(MapSave.class)) {
//
//            // Arrange
//            mockedInput.when(InputHandler::getIntInput)
//                    .thenReturn(1)  // create new map
//                    .thenReturn(0); // save and exit
//
//            // Act
//            mapEditor.start();
//
//            // Assert
//            verify(mainMap).renderWithCords();
//            verify(mainMap).render();
//            mockedSave.verify(() -> MapSave.writeSave(any(MainMap.class)));
//        }
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void testNullMapHandling() {
//        // Arrange
//        mapEditor.mainMap = null;
//
//        // Act
//        mapEditor.startEdit();
//
//        // Should throw NullPointerException
//    }
}