import game.Town.Buildings.Hotel;
import game.Town.NpcManager;
import game.Town.TownBuilding;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NpcManagerTest {
    private List<TownBuilding> buildings;

    @Before
    public void setUp() {
        buildings = new ArrayList<>();
        buildings.add(new Hotel());
    }

    @Test
    public void testNpcInitialization() {
        NpcManager.init(buildings);
        assertFalse(NpcManager.getFreeNpcs().isEmpty());
        assertEquals(10, NpcManager.getFreeNpcs().size());
    }

    @Test
    public void testNpcVisitingBuilding() throws InterruptedException {
        NpcManager.init(buildings);
        int initialFree = NpcManager.getFreeNpcs().size();

        Thread.sleep(3000);

        assertTrue(NpcManager.getFreeNpcs().size() < initialFree);
    }
}