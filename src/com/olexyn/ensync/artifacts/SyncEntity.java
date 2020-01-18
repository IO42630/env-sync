package com.olexyn.ensync.artifacts;


import com.olexyn.ensync.Tools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A SyncEntity is a collection of SyncDirectories,
 * that are supposed to be kept in sync.
 */
public class SyncEntity {

    public String name;
    public int syncInterval = 3600;
    public Map<String, SyncDirectory> syncDirectories = new HashMap<>();
    private Map<String, File> mapCreated = new HashMap<>();
    private Map<String, File> mapDeleted = new HashMap<>();
    Tools tools = new Tools();

    /**
     * @see SyncEntity
     */
    public SyncEntity(String name) {
        this.name = name;

    }

    /**
     * Creates a new Syncdirectory.
     * <p>
     * Adds the created SyncDirectory to this SyncEntity.
     *
     * @param realPath the path from which the Syncdirectory is created.
     * @see SyncDirectory
     */
    public void addDirectory(String realPath) {
        if (new File(realPath).isDirectory()) {
            syncDirectories.put(realPath, new SyncDirectory(realPath));
        }
    }

    public Map<String, File> getMapCreated() {
        for (Map.Entry<String, SyncDirectory> entry : syncDirectories.entrySet()) {
            SyncDirectory syncDirectory = entry.getValue();
            for (File file : syncDirectory.getListCreated()) {
                mapCreated.put(file.getPath(), file);
            }

        }


        return mapCreated;
    }

    public Map<String, File> getMapDeleted() {

        for (Map.Entry<String, SyncDirectory> entry : syncDirectories.entrySet()) {
            SyncDirectory syncDirectory = entry.getValue();
            for (File file : syncDirectory.getListDeleted()) {
                mapDeleted.put(file.getPath(), file);
            }

        }


        return mapDeleted;
    }

    public void addToMapCreated(List<File> listCreated) {
        for (File file : listCreated) {
            mapCreated.put(file.getPath(), file);
        }
    }


    public void addToMapDeleted(List<File> listDeleted) {
        for (File file : listDeleted) {
            mapDeleted.put(file.getPath(), file);
        }
    }

}
