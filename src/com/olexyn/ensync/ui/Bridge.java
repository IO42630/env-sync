package com.olexyn.ensync.ui;


import com.olexyn.ensync.artifacts.SyncMap;

import static com.olexyn.ensync.Main.SYNC;
import static com.olexyn.ensync.Main.FLOW_THREAD;
import static com.olexyn.ensync.Main.UI_THREAD;

import java.io.File;

/**
 * Connect the Controller and the Flow
 */
public class Bridge {



    void newCollection(String collectionName){
        SYNC.put(collectionName, new SyncMap(collectionName));
    }


    void removeCollection(String collectionName){
        SYNC.remove(collectionName);
    }


    void addDirectory(String collectionName, File diretory){
        SYNC.get(collectionName).addDirectory(diretory.getAbsolutePath());
        //TODO pause syning when adding
    }


    /**
     * This works, because a directory, which here is an unique ablsolute path,
     * is only supposed to present once across entire SYNC.
     */
    void removeDirectory(String directoryAbsolutePath){
        //TODO fix ConcurrentModificationException. This will possibly resolve further errors.
        while (true) {
            if (FLOW_THREAD.getState().equals(Thread.State.TIMED_WAITING)) {
                try {
                    FLOW_THREAD.wait();
                } catch (InterruptedException e) {
                    for (var syncMap : SYNC.entrySet()){
                        syncMap.getValue().removeDirectory(directoryAbsolutePath);
                    }
                    FLOW_THREAD.notify();
                    break;
                }
            }
        }

    }
}
