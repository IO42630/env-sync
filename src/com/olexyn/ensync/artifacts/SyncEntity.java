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







}
