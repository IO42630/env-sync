package com.olexyn.ensync.ui;


import com.olexyn.ensync.artifacts.SyncMap;


import java.io.File;

 import static com.olexyn.ensync.Main.MAP_OF_SYNCMAPS;

/**
 * Connect the Controller and the Flow
 */
public class Bridge {


    void newCollection(String collectionName) {

        synchronized (MAP_OF_SYNCMAPS) {
            MAP_OF_SYNCMAPS.put(collectionName, new SyncMap(collectionName));
        }
    }


    void removeCollection(String collectionName) {
        synchronized (MAP_OF_SYNCMAPS) {
            MAP_OF_SYNCMAPS.remove(collectionName);
        }
    }


    void addDirectory(String collectionName, File diretory) {
        synchronized (MAP_OF_SYNCMAPS) {
            MAP_OF_SYNCMAPS.get(collectionName).addDirectory(diretory.getAbsolutePath());
        }
        //TODO pause syning when adding
    }


    /**
     * This works, because a directory, which here is an unique ablsolute path,
     * is only supposed to present once across entire SYNC.
     */
    void removeDirectory(String directoryAbsolutePath) {
        //TODO fix ConcurrentModificationException. This will possibly resolve further errors.
        synchronized (MAP_OF_SYNCMAPS) {
            for (var syncMap : MAP_OF_SYNCMAPS.entrySet()) {
                syncMap.getValue().removeDirectory(directoryAbsolutePath);
            }
        }


    }
}
