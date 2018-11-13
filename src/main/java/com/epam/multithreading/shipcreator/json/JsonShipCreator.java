package com.epam.multithreading.shipcreator.json;

import com.epam.multithreading.exception.ShipCreatingException;
import com.epam.multithreading.ship.Ship;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonShipCreator {

    public List<Ship> createShips(String path) throws ShipCreatingException {
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonShips = (JSONArray) parser.parse(new FileReader(path));
            List<Ship> parsedShips = new ArrayList <>();
            for(Object element:jsonShips){
                JSONObject ship = (JSONObject) element;
                String name = (String) ship.get("name");
                String capacityContent = (String) ship.get("capacity");
                int capacity = Integer.parseInt(capacityContent);
                parsedShips.add(new Ship(name,capacity));
            }
            return parsedShips;
        } catch (ParseException | IOException e){
            throw new ShipCreatingException(e.getMessage(),e);
        }
    }
}
