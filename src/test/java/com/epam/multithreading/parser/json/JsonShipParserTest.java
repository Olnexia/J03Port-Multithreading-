package com.epam.multithreading.parser.json;

import com.epam.multithreading.exception.JsonParsingException;
import com.epam.multithreading.ship.RequestedPortService;
import com.epam.multithreading.ship.Ship;
import com.epam.multithreading.ship.parser.json.JsonShipParser;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class JsonShipParserTest {
    private static final String SHIP_DATA_FILE = "src/test/resources/TestShips.json";
    private static final String NONEXISTENT_DATA_FILE = "src/test/resources/ff.json";

    @Test
    public void shouldParseJsonFileWhenDataValid() throws JsonParsingException {
        //given
        JsonShipParser jsonShipParser = new JsonShipParser();
        //when
        List<Ship> actual = jsonShipParser.parse(SHIP_DATA_FILE);
        //then
        Assert.assertEquals(2,actual.size());
        Ship first = actual.get(0);
        Assert.assertEquals("Flame",first.getName());
        Assert.assertEquals(5,first.getCapacity());
        Assert.assertEquals(3,first.getContainerAmount());
        Assert.assertEquals(RequestedPortService.LOAD,first.getRequestedPortService());
        Ship second = actual.get(1);
        Assert.assertEquals("Golden",second.getName());
        Assert.assertEquals(7,second.getCapacity());
        Assert.assertEquals(4,second.getContainerAmount());
        Assert.assertEquals(RequestedPortService.UNLOAD_LOAD,second.getRequestedPortService());
    }

    @Test(expected = JsonParsingException.class)
    public void shouldThrowParsingExceptionWhenFileNotExist() throws JsonParsingException{
        //given
        JsonShipParser jsonShipParser = new JsonShipParser();
        //when
        jsonShipParser.parse(NONEXISTENT_DATA_FILE);
        //then
    }
}
