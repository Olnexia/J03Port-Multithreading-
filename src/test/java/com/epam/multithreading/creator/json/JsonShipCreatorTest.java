package com.epam.multithreading.creator.json;

import com.epam.multithreading.exception.ShipCreatingException;
import com.epam.multithreading.ship.Ship;
import com.epam.multithreading.ship.Target;
import com.epam.multithreading.ship.creator.json.JsonShipCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JsonShipCreatorTest {
    private static final String SHIP_DATA_FILE = "src/test/resources/TestShips.json";

    @Test
    public void shouldCreateShipsWhenDataValid() throws ShipCreatingException {
        //given
        JsonShipCreator jsonShipCreator = new JsonShipCreator();
        //when
        List<Ship> actual = jsonShipCreator.createShips(SHIP_DATA_FILE);
        //then
        Assert.assertEquals(2,actual.size());
        Ship first = actual.get(0);
        Assert.assertEquals("Flame",first.getName());
        Assert.assertEquals(5,first.getCapacity());
        Assert.assertEquals(3,first.getContainerAmount());
        Assert.assertEquals(Target.LOAD,first.getTarget());
        Ship second = actual.get(1);
        Assert.assertEquals("Golden",second.getName());
        Assert.assertEquals(7,second.getCapacity());
        Assert.assertEquals(4,second.getContainerAmount());
        Assert.assertEquals(Target.UNLOAD_LOAD,second.getTarget());
    }
}
