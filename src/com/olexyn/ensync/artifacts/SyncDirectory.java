package com.olexyn.ensync.artifacts;

import com.olexyn.ensync.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncDirectory {

    public String realPath;
    public String stateFileBasePath;
    public String stateFileOldPath;
    public String stateFileNewPath;
    public File stateFileOld;
    public File stateFileNew;
    private Map<String, File> poolOld = new HashMap<>();
    private Map<String, File> poolNew = new HashMap<>();
    private List<File> listCreated = new ArrayList<>();
    private List<File> listDeleted = new ArrayList<>();


    Tools tools = new Tools();

    /**
     * Create a SyncDirectory from realPath.
     *
     * @param realPath
     * @see SyncEntity
     */
    public SyncDirectory(String realPath) {
        this.realPath = realPath;
        stateFileBasePath = "/tmp/find" + this.realPath.replace("/", "-");
        stateFileOldPath = stateFileBasePath + "-old";
        stateFileNewPath = stateFileBasePath + "-new";
        stateFileOld = getStateFileOld();
        stateFileNew = getStateFileNew();
        poolOld = getPoolOld();
        poolNew = getPoolNew();

    }

    /**
     * IF NOT EXISTS the StateFileOld for this SyncDirectory,<p>
     * - THEN create the File on Disk.
     *
     * @return the StateFileOld.
     */
    public File getStateFileOld() {
        stateFileOld = new File(stateFileOldPath);
        if (!stateFileOld.exists()) {
            stateFileOld = tools.generateStateFile(realPath, stateFileOldPath);
        }
        return stateFileOld;
    }

    /**
     * READ directory contents.<p>
     * WRITE a new StateFileNew to Disk. This is IMPORTANT in order to make sure that StateFileOld is NEVER newer than StateFileNew.<p>
     * WRITE a new StateFileOld to Disk.
     */
    public void updateStateFileBoth() {
        //
        tools.generateStateFile(realPath, stateFileNewPath);
        tools.generateStateFile(realPath, stateFileOldPath);
    }

    public File getStateFileNew() {
        stateFileNew = new File(stateFileNewPath);
        if (!stateFileNew.exists()) {
            stateFileNew = tools.generateStateFile(realPath, stateFileNewPath);
        }
        return stateFileNew;
    }

    /**
     * READ directory contents.<p>
     * WRITE a new StateFileNew Disk.
     */
    public void updateStateFileNew() {
        //
        tools.generateStateFile(realPath, stateFileNewPath);
    }


    public Map<String, File> getPoolOld() {
        if (poolOld.isEmpty()) {
            updatePoolBoth();
        }
        return poolOld;
    }

    /**
     * UPDATE  PoolOld FROM contents of StateFileOld.
     */
    public void updatePoolBoth() {
        poolNew = tools.fileToPool(getStateFileNew(), "all");
        poolOld = tools.fileToPool(getStateFileOld(), "all");

    }


    public Map<String, File> getPoolNew() {
        if (poolNew.isEmpty()) {
            updatePoolNew();
        }
        return poolNew;
    }

    /**
     * UPDATE  PoolNew FROM contents of StateFileNew.
     */
    public void updatePoolNew() {
        poolNew = tools.fileToPool(getStateFileNew(), "all");

    }

    public List<File> getListCreated() {

        listCreated = tools.mapMinus(getPoolNew(), getPoolOld());

        return listCreated;
    }

    public List<File> getListDeleted() {

        listDeleted = tools.mapMinus(getPoolOld(), getPoolNew());

        return listDeleted;
    }

}
