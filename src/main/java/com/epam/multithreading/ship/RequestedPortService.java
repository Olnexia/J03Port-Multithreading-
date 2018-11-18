package com.epam.multithreading.ship;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RequestedPortService {
    @JsonProperty("unload")
    UNLOAD,
    @JsonProperty("load")
    LOAD,
    @JsonProperty("unload load")
    UNLOAD_LOAD
}
