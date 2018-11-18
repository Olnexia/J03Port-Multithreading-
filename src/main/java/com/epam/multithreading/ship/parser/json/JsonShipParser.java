package com.epam.multithreading.ship.parser.json;

import com.epam.multithreading.exception.JsonParsingException;
import com.epam.multithreading.ship.Ship;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.List;

public class JsonShipParser {

    public List<Ship> parse(String path) throws JsonParsingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream inputStream = new FileInputStream(path);
            return objectMapper.readValue(inputStream, new TypeReference <List<Ship>>(){});
        }catch (IOException e) {
            throw new JsonParsingException(e.getMessage(), e);
        }
    }
}
