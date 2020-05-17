package com.olexyn.ensync.artifacts;

import com.olexyn.ensync.Execute;
import com.olexyn.ensync.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A SyncDirectory is an occurrence of a particular directory somewhere across the filesystems.
 */
public class SyncDirectory {

    private String flowState;
    private SyncDirectory thisSD = this;


    private SyncMap syncMap;
    public String path = null;

    public List<SyncFile> listCreated = new ArrayList<>();
    public List<SyncFile> listDeleted = new ArrayList<>();
    public List<SyncFile> listModified = new ArrayList<>();


    Tools tools = new Tools();
    Execute x = new Execute();

    /**
     * Create a SyncDirectory from realPath.
     *
     * @see SyncMap
     */
    public SyncDirectory(String path, SyncMap syncMap) {

        this.path = path;
        this.syncMap = syncMap;

    }


    /**
     * Get the current state by using the `find` command.
     */
    public Map<String, SyncFile> readFreshState() {
        //NOTE that the SFile().lastModifiedOld is not set here, so it is 0 by default.
        Map<String, SyncFile> filemap = new HashMap<>();

        Execute.TwoBr find = x.execute(new String[]{"find",
                                                    path});

        List<String> pathList = tools.brToListString(find.output);

        for (String filePath : pathList) {
            SyncFile file = new SyncFile(this, filePath);

            filemap.put(filePath, file);
        }


        return filemap;


    }


    /**
     * READ the contents of StateFile to Map.
     */
    public Map<String, SyncFile> readStateFile() {
        Map<String, SyncFile> filemap = new HashMap<>();
        List<String> lines = tools.fileToLines(new File(tools.stateFilePath(path)));

        for (String line : lines) {
            // this is a predefined format: "modification-time path"
            String modTimeString = line.split(" ")[0];
            long modTime = Long.parseLong(modTimeString);

            String sFilePath = line.replace(modTimeString + " ", "");
            SyncFile sfile = new SyncFile(this, sFilePath);

            sfile.setTimeModifiedFromStateFile(modTime);

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
        Map<String, SyncFile> fromA = readFreshState();
        Map<String, SyncFile> substractB = readStateFile();

        listCreated = tools.mapMinus(fromA, substractB);
    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     */
    public void makeListDeleted() {
        Map<String, SyncFile> fromA = readStateFile();
        Map<String, SyncFile> substractB = readFreshState();

        listDeleted = tools.mapMinus(fromA, substractB);



    }


    /**
     * Compare the OLD and NEW pools.
     * List is cleared and created each time.
     */
    public List<SyncFile> makeListModified() {

        listModified = new ArrayList<>();
        Map<String, SyncFile> stateFileMap = readStateFile();

        for (var freshFileEntry : readFreshState().entrySet()) {

            String freshFileKey = freshFileEntry.getKey();
            SyncFile freshFile = freshFileEntry.getValue();

            // If KEY exists in OLD , thus FILE was NOT created.
            if (stateFileMap.containsKey(freshFileKey)) {

                if (freshFile.getTimeModified() > freshFile.getTimeModifiedFromStateFile()) {
                    listModified.add(freshFile);
                }
            }
        }
        return listModified;
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

        tools.writeStringListToFile(tools.stateFilePath(path), outputList);
    }


    private class Info {

        private String thisFilePath;
        private String otherFilePath;
        private String otherParentPath;
        private File otherParentFile;


        private Info(SyncDirectory thisSD, SyncFile sFile, SyncDirectory otherSD) {
            // Example:
            //  syncDirectory /foo
            //  otherSyncDirectory /bar
            //  createdFile  /foo/hello/created-file.gif
            //  relativePath /hello/created-file.gif
            String relativePath = sFile.getPath().replace(thisSD.path, "");
            this.thisFilePath = sFile.getPath();
            this.otherFilePath = otherSD.path + relativePath;
            File otherFile = new File(otherFilePath);

            this.otherParentPath = otherFile.getParent();
            this.otherParentFile = new File(otherParentPath);







        }
    }


    public void doCreate() {

        for (SyncFile createdFile : listCreated) {

            for (var otherEntry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory otherSD = otherEntry.getValue();

                if (this.equals(otherSD)) { continue; }

                Info info = new Info(this, createdFile, otherSD);

                writeFile(info, this, createdFile, otherSD);
            }
        }
    }


    /**
     *
     */
    public void doDelete() {

        for (SyncFile deletedFile : listDeleted) {

            for (var otherEntry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory otherSD = otherEntry.getValue();

                if (this.equals(otherSD)) { continue; }

                Info info = new Info(thisSD, deletedFile, otherSD);
                deleteFile(info, thisSD, deletedFile, otherSD);


            }
        }
    }

    private void deleteFile(Info info, SyncDirectory thisSD, SyncFile thisFile, SyncDirectory otherSD){

        SyncFile otherFile = new SyncFile(otherSD, otherSD.path + thisFile.relativePath);

        // if the otherFile was created with ensync it will have the == TimeModified.
        if (thisFile.getTimeModified() >= otherFile.getTimeModified()) {
            List<String> cmd = List.of("rm", "-r", info.otherFilePath);
            x.execute(cmd);
        }
    }


    public void doModify() {

        for (SyncFile modifiedFile : this.listModified) {

            for (var otherEntry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory otherSD = otherEntry.getValue();

                if (this.equals(otherSD)) { continue; }

                Info info = new Info(this, modifiedFile, otherSD);

                writeFile(info, this, modifiedFile, otherSD);
            }
        }
    }


    private void writeFile(Info info, SyncDirectory thisSD, SyncFile thisFile, SyncDirectory otherSD) {

        SyncFile otherFile = new SyncFile(otherSD, otherSD.path + thisFile.relativePath);


        if (otherFile.exists() && thisFile.getTimeModified() < otherFile.getTimeModified()) { return;}


        if (thisFile.isDirectory() && !otherFile.exists()) {
            List<String> cmd = List.of("mkdir", "-p", info.otherFilePath);
            x.execute(cmd);
            return;
        }

        if (thisFile.isFile()) {

            if (!info.otherParentFile.exists()) {
                List<String> cmd = List.of("mkdir", "-p", info.otherParentPath);
                x.execute(cmd);
            }

            List<String> cmd = List.of("cp", "-p", info.thisFilePath, info.otherFilePath);
            x.execute(cmd);
        }
    }



}


