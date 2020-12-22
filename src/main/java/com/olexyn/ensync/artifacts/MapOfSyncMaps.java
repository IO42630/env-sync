package com.olexyn.ensync.artifacts;

import java.util.HashMap;

public class MapOfSyncMaps {

    private static HashMap<String, SyncMap> mapOfSyncMaps;

    private MapOfSyncMaps() {}

    public static HashMap<String, SyncMap> get() {

        if (mapOfSyncMaps == null) {
            mapOfSyncMaps = new HashMap<>();
        }
        return mapOfSyncMaps;

    }
}
