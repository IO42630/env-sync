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
    public Map<String, SyncFile> readState() {
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
    public Map<String, SyncFile> readStateFile() {
        Map<String, SyncFile> filemap = new HashMap<>();
        List<String> lines = tools.fileToLines(new File(stateFilePath(path)));

        for (String line : lines) {
            // this is a predefined format: "modification-time path"
            String modTimeString = line.split(" ")[0];
            long modTime = Long.parseLong(modTimeString);

            String sFilePath = line.replace(modTimeString + " ", "");
            SyncFile sfile = new SyncFile(sFilePath);

            sfile.setStateFileTime(modTime);

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
    public void makeListCreated() {
        listCreated = new ArrayList<>();
        Map<String, SyncFile> fromA = readState();
        Map<String, SyncFile> substractB = readStateFile();

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
    public void makeListDeleted() {
        listDeleted = new ArrayList<>();
        Map<String, SyncFile> fromA = readStateFile();
        Map<String, SyncFile> substractB = readState();


        listDeleted = tools.mapMinus(fromA, substractB);
    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     *
     * @return
     */
    public void makeListModified() {

        listModified = new ArrayList<>();
        Map<String, SyncFile> oldMap = readStateFile();

        for (Map.Entry<String, SyncFile> newFileEntry : readState().entrySet()) {
            // If KEY exists in OLD , thus FILE was NOT created.
            String newFileKey = newFileEntry.getKey();


            if (oldMap.containsKey(newFileKey)) {

                SyncFile newFile = newFileEntry.getValue();
                long lastModifiedNew = newFile.lastModified();


                long lastModifiedOld = oldMap.get(newFileKey).stateFileTime();

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


    private class Info {

        private String relativePath = null;
        private String thisFilePath = null;
        private String otherFilePath = null;
        private File otherFile = null;
        private String otherParentPath = null;
        private File otherParentFile = null;
        private long thisStateFileTime = 0;
        private long thisTimeModified = 0;
        private long otherTimeModified = 0;


        private Info(SyncDirectory thisSD,
                     SyncFile sFile,
                     SyncDirectory otherSD) {
            // Example:
            //  syncDirectory /foo
            //  otherSyncDirectory /bar
            //  createdFile  /foo/hello/created-file.gif
            //  relativePath /hello/created-file.gif
            this.relativePath = sFile.getPath().replace(thisSD.path, "");
            this.thisFilePath = sFile.getPath();
            this.otherFilePath = otherSD.path + relativePath;
            this.otherFile = new File(otherFilePath);

            this.otherParentPath = otherFile.getParent();
            this.otherParentFile = new File(otherParentPath);

            if (thisSD.readStateFile().get(thisFilePath) != null) {
                this.thisStateFileTime = thisSD.readStateFile().get(thisFilePath).stateFileTime();
            } else {
                // thisFile does not exist in StateFile, a value of 0 can be seen as equal to "never existed".
                this.thisStateFileTime = 0;
            }
            this.thisTimeModified = sFile.lastModified();


            if (otherFile.exists()) {

                this.otherTimeModified = otherFile.lastModified();

            } else {
                if (otherSD.readStateFile().get(otherFilePath) != null) {
                    this.otherTimeModified = otherSD.readStateFile().get(otherFilePath).stateFileTime();
                } else {
                    this.otherTimeModified = 0;
                }
            }

        }


    }


    public void doCreate() {

        for (SyncFile createdFile : listCreated) {

            if (createdFile.isFile()) {

                for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                    SyncDirectory otherSyncDirectory = otherEntry.getValue();

                    if (!this.equals(otherSyncDirectory)) {

                        Info info = new Info(this, createdFile, otherSyncDirectory);

                        createFile(info);
                    }
                }
            }
        }
    }


    /**
     *
     */
    public void doDelete() {

        for (SyncFile deletedFile : listDeleted) {


            for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                SyncDirectory otherSyncDirectory = otherEntry.getValue();

                if (!this.equals(otherSyncDirectory)) {

                    Info info = new Info(this, deletedFile, otherSyncDirectory);
                    if (info.otherFile.isFile()) {

                        // if the otherFile was created with ensync it will have the == TimeModified.
                        if (info.thisStateFileTime >= info.otherTimeModified) {
                            String[] cmd = new String[]{"rm",
                                                        "-r",
                                                        info.otherFilePath};
                            x.execute(cmd);
                        }
                    }

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

                        Info info = new Info(this, modifiedFile, otherSyncDirectory);


                        createFile(info);


                    }


                }
            }

        }
    }


    private void createFile(Info info) {
        if (!info.otherFile.exists() || info.thisTimeModified > info.otherTimeModified) {
            if (!info.otherParentFile.exists()) {
                String[] cmd = new String[]{"mkdir",
                                            "-p",
                                            info.otherParentPath};
                x.execute(cmd);
            }
            String[] cmd = new String[]{"cp",
                                        "-p",
                                        info.thisFilePath,
                                        info.otherFilePath};
            x.execute(cmd);
        }
    }

}
