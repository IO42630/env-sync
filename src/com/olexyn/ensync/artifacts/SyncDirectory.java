package com.olexyn.ensync.artifacts;

import com.olexyn.ensync.Execute;
import com.olexyn.ensync.Tools;

import java.io.File;
import java.util.*;

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
    private SyncEntity syncEntity;

    private String state = null;

// For an explanation of what the STATES mean, see the flow.png
    private final List<String>  STATES = new ArrayList<>(Arrays.asList( "NEW-1", "LIST-1" , "LIST-2" , "SYNC-1" , "NEW-2", "OLD-1"));

    Tools tools = new Tools();
    Execute x = new Execute();

    /**
     * Create a SyncDirectory from realPath.
     *
     * @param realPath
     * @see SyncEntity
     */
    public SyncDirectory(String realPath, SyncEntity syncEntity) {


        this.realPath = realPath;
        stateFileBasePath = "/tmp/find" + this.realPath.replace("/", "-");
        stateFileOldPath = stateFileBasePath + "-old";
        stateFileNewPath = stateFileBasePath + "-new";
        stateFileOld = getStateFileOld();
        stateFileNew = getStateFileNew();
        poolOld = getPoolOld();
        poolNew = getPoolNew();
        this.syncEntity = syncEntity;

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
    public void updateStateFileOld() {
        //
        if (state.equals(STATES.get(4))){
            state = STATES.get(5);
        } else {
            return ;
        }
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
        if (state == null || state.equals(STATES.get(5))){
            state = STATES.get(0);
        }else if (state.equals(STATES.get(3))){
            state = STATES.get(4);
        } else {
            return;
        }



        tools.generateStateFile(realPath, stateFileNewPath);


    }


    public Map<String, File> getPoolOld() {
        if (poolOld.isEmpty()) {
            updatePoolOld();
        }
        return poolOld;
    }

    /**
     * UPDATE  PoolOld FROM contents of StateFileOld.
     */
    public void updatePoolOld() {

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
        if (state.equals(STATES.get(0))){
            state = STATES.get(1);
        } else {
            return null;
        }
        updateListCreated();

        return listCreated;
    }

    public void updateListCreated(){

        listCreated = tools.mapMinus(getPoolNew(), getPoolOld());
    }

    public List<File> getListDeleted() {
        if (state.equals(STATES.get(1))){
            state = STATES.get(2);
        } else {
            return null;
        }
        listDeleted = tools.mapMinus(getPoolOld(), getPoolNew());

        return listDeleted;
    }

    public void doSyncOps(){

        if (state.equals(STATES.get(2))){
            state = STATES.get(3);
        } else {
            return ;
        }






        for (File createdFile : listCreated) {
            for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                SyncDirectory otherSyncDirectory = otherEntry.getValue();

                if (!this.equals(otherSyncDirectory)){
                    // Example:
                    //  syncDirectory /foo
                    //  otherSyncDirectory /bar
                    //  createdFile  /foo/hello/created-file.gif
                    //  relativePath /hello/created-file.gif
                    String relativePath = createdFile.getPath().replace(this.realPath, "");
                    String targetPath = otherSyncDirectory.realPath + relativePath;
                    String targetParentPath = new File(targetPath).getParent();
                    if (!new File(targetParentPath).exists()){
                        String[] cmd = new String[]{"mkdir",
                                                    "-p",
                                                    targetParentPath};
                        x.execute(cmd);
                    }

                    String[] cmd = new String[]{"cp",
                                                createdFile.getPath(),
                                                otherSyncDirectory.realPath + relativePath};
                    x.execute(cmd);
                }
            }

        }

        //
        for (File deletedFile : listDeleted) {

            for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                SyncDirectory otherSyncDirectory = otherEntry.getValue();

                if (!this.equals(otherSyncDirectory)){
                    String relativePath = deletedFile.getPath().replace(this.realPath, "");
                    String[] cmd = new String[]{"rm", "-r",
                                                otherSyncDirectory.realPath + relativePath};
                    x.execute(cmd);
                }
            }

        }
    }

}
