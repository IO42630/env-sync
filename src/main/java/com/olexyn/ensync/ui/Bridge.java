package com.olexyn.ensync.ui;


import com.olexyn.ensync.artifacts.MapOfSyncMaps;
import com.olexyn.ensync.artifacts.SyncMap;


import java.io.File;



/**
 * Connect the Controller and the Flow
 */
public class Bridge {


    void newCollection(String collectionName) {

        synchronized (MapOfSyncMaps.get()) {
            MapOfSyncMaps.get().put(collectionName, new SyncMap(collectionName));
        }
    }


    void removeCollection(String collectionName) {
        synchronized (MapOfSyncMaps.get()) {
            MapOfSyncMaps.get().remove(collectionName);
        }
    }


    void addDirectory(String collectionName, File diretory) {
        synchronized (MapOfSyncMaps.get()) {
            MapOfSyncMaps.get().get(collectionName).addDirectory(diretory.getAbsolutePath());
        }
        //TODO pause syning when adding
    }


    /**
     * This works, because a directory, which here is an unique ablsolute path,
     * is only supposed to present once across entire SYNC.
     */
    void removeDirectory(String directoryAbsolutePath) {
        //TODO fix ConcurrentModificationException. This will possibly resolve further errors.
        synchronized (MapOfSyncMaps.get()) {
            for (var syncMap : MapOfSyncMaps.get().entrySet()) {
                syncMap.getValue().removeDirectory(directoryAbsolutePath);
            }
        }


    }
}
