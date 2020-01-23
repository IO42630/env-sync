package com.olexyn.ensync.artifacts;

import com.olexyn.ensync.Execute;
import com.olexyn.ensync.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncDirectory {


    private SyncEntity syncEntity;
    public String path = null;

    public List<SyncFile> listCreated = new ArrayList<>();
    public List<SyncFile> listDeleted = new ArrayList<>();
    public List<SyncFile> listModified = new ArrayList<>();


    Tools tools = new Tools();
    Execute x = new Execute();

    /**
     * Create a SyncDirectory from realPath.
     *
     * @see SyncEntity
     */
    public SyncDirectory(String path,
                         SyncEntity syncEntity) {

        this.path = path;
        this.syncEntity = syncEntity;

    }


    /**
     * NOTE that the SFile().lastModifiedOld is not set here, so it is 0 by default.
     */
    public Map<String, SyncFile> readState(String path) {
        Map<String, SyncFile> filemap = new HashMap<>();

        Execute.TwoBr find = x.execute(new String[]{"find",
                                                    path});

        List<String> pathList = tools.brToListString(find.output);

        for (String filePath : pathList) {
            SyncFile file = new SyncFile(filePath);

            filemap.put(filePath, file);
        }


        return filemap;


    }


    /**
     * READ the contents of StateFile to Map.
     */
    public Map<String, SyncFile> readStateFile(String path) {
        Map<String, SyncFile> filemap = new HashMap<>();
        List<String> lines = tools.fileToLines(new File(stateFilePath(path)));

        for (String line : lines) {
            // this is a predefined format: "modification-time path"
            String modTimeString = line.split(" ")[0];
            long modTime = Long.parseLong(modTimeString);

            String sFilePath = line.replace(modTimeString + " ", "");
            SyncFile sfile = new SyncFile(sFilePath);

            sfile.setLastModifiedOld(modTime);

            filemap.put(sFilePath, sfile);
        }

        return filemap;

    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     *
     * @return
     */
    public void makeListCreated(String path) {
        listCreated = new ArrayList<>();
        Map<String, SyncFile> fromA = readState(path);
        Map<String, SyncFile> substractB = readStateFile(path);

        listCreated = tools.mapMinus(fromA, substractB);
    }


    public String stateFilePath(String path) {
        return "/tmp/ensync/state" + path.replace("/", "-");
    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     *
     * @return
     */
    public void makeListDeleted(String path) {
        listDeleted = new ArrayList<>();
        Map<String, SyncFile> fromA = readStateFile(path);
        Map<String, SyncFile> substractB = readState(path);


        listDeleted = tools.mapMinus(fromA, substractB);
    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     *
     * @return
     */
    public void makeListModified(String path) {

        listModified = new ArrayList<>();
        Map<String, SyncFile> oldMap = readStateFile(path);

        for (Map.Entry<String, SyncFile> newFileEntry : readState(path).entrySet()) {
            // If KEY exists in OLD , thus FILE was NOT created.
            String newFileKey = newFileEntry.getKey();


            if (oldMap.containsKey(newFileKey)) {

                SyncFile newFile = newFileEntry.getValue();
                long lastModifiedNew = newFile.lastModified();


                long lastModifiedOld = oldMap.get(newFileKey).lastModifiedOld();

                if (lastModifiedNew > lastModifiedOld) {
                    listModified.add(newFile);
                }
            }
        }

    }


    /**
     * QUERY state of the filesystem at realPath.
     * WRITE the state of the filesystem to file.
     */
    public void writeStateFile(String path) {
        List<String> outputList = new ArrayList<>();


        Execute.TwoBr find = x.execute(new String[]{"find",
                                                    path});

        List<String> pathList = tools.brToListString(find.output);


        for (String filePath : pathList) {
            long lastModified = new File(filePath).lastModified();
            outputList.add("" + lastModified + " " + filePath);
        }

        tools.writeStringListToFile(stateFilePath(path), outputList);
    }


    public void doCreate() {


        for (File createdFile : listCreated) {
            for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                SyncDirectory otherSyncDirectory = otherEntry.getValue();

                if (!this.equals(otherSyncDirectory)) {
                    // Example:
                    //  syncDirectory /foo
                    //  otherSyncDirectory /bar
                    //  createdFile  /foo/hello/created-file.gif
                    //  relativePath /hello/created-file.gif
                    String relativePath = createdFile.getPath().replace(this.path, "");
                    String targetPath = otherSyncDirectory.path + relativePath;
                    String targetParentPath = new File(targetPath).getParent();
                    if (!new File(targetParentPath).exists()) {
                        String[] cmd = new String[]{"mkdir",
                                                    "-p",
                                                    targetParentPath};
                        x.execute(cmd);
                    }

                    String[] cmd = new String[]{"cp",
                                                createdFile.getPath(),
                                                otherSyncDirectory.path + relativePath};
                    x.execute(cmd);
                }
            }

        }
    }


    public void doDelete() {

        for (File deletedFile : listDeleted) {

            for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                SyncDirectory otherSyncDirectory = otherEntry.getValue();

                if (!this.equals(otherSyncDirectory)) {
                    String relativePath = deletedFile.getPath().replace(this.path, "");
                    String[] cmd = new String[]{"rm",
                                                "-r",
                                                otherSyncDirectory.path + relativePath};
                    x.execute(cmd);
                }
            }

        }


    }

    public void doModify() {

        for (SyncFile modifiedFile : this.listModified) {

            if (modifiedFile.isFile()) {

                for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                    SyncDirectory otherSyncDirectory = otherEntry.getValue();

                    if (!this.equals(otherSyncDirectory)) {

                        String relativePath = modifiedFile.getPath().replace(this.path, "");


                        String otherFilePath = otherSyncDirectory.path + relativePath;
                        SyncFile otherFile = new SyncFile(otherFilePath);

                        if (otherFile.exists()) {
                            if (modifiedFile.lastModified() > otherFile.lastModified()) {
                                // IF both Files exist, and this File NEWER -> UPDATE the other File
                                String[] cmd = new String[]{"cp",
                                                            modifiedFile.getPath(),
                                                            otherFilePath};
                                x.execute(cmd);
                            }
                        } else {
                            // IF other file does NOT exist -> UPDATE (i.e. create) the other File
                            String[] cmd = new String[]{"mkdir",
                                                        "-p",
                                                        otherFilePath};
                            x.execute(cmd);

                            cmd = new String[]{"cp",
                                               modifiedFile.getPath(),
                                               otherFilePath};
                            x.execute(cmd);
                        }


                    }


                }
            }

        }
    }


}
