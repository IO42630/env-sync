package com.olexyn.ensync;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    private final Execute x;

    public Tools() {
        x = new Execute();
    }


    /**
     *
     */
    public void rsync(String param,
                      String source,
                      String destination) {
        //
        BufferedReader foo = x.execute(new String[]{"rsync",
                                                    param,
                                                    source,
                                                    destination}).output;
    }

    public String getConf() {
        BufferedReader output = x.execute(new String[]{"cat",
                                                       System.getProperty("user.dir") + "/src/com/olexyn/ensync/sync.conf"}).output;
        return brToString(output);
    }

    public String brToString(BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        Object[] br_array = br.lines().toArray();
        for (int i = 0; i < br_array.length; i++) {
            sb.append(br_array[i].toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * StateFile -> FilePool
     */
    public Map<String, File> fileToPool(File file,
                                        String type) {
        List<String> lines = fileToLines(file);
        return linesToFilePool(lines, type);
    }


    /**
     * CREATE a StateFile from realPath. <p>
     * WRITE the StateFle to stateFilePath.
     * @param realPath the path of the directory the StateFile is created for.
     * @param stateFilePath the desired path for the created Statefile.
     */
    public File generateStateFile(String realPath, String stateFilePath) {
        String[] cmd = new String[]{System.getProperty("user.dir") + "/src/com/olexyn/ensync/shell/toFile.sh",
                                    "find",
                                    realPath,
                                    stateFilePath};
        x.execute(cmd);
        return new File(realPath);
    }


    public List<String> fileToLines(File file) {
        String filePath = file.getPath();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public Map<String, File> linesToFilePool(List<String> lines,
                                             String type) {

        Map<String, File> filepool = new HashMap<>();

        for (String line : lines) {
            File file = new File(line);

            if (type.equals("all") || type.equals("dir") && file.isDirectory() || type.equals("file") && file.isFile()) {
                filepool.put(line, file);
            }
        }
        return filepool;
    }


    public List<File> mapMinus(Map<String, File> fromA,
                               Map<String, File> substractB) {

        List<File> difference = new ArrayList<>();
        Iterator iterator = fromA.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();

            File file = fromA.get(key);


            if (fromA.containsKey(key) && !substractB.containsKey(key)) {
                difference.add(file);
            }


        }
        return difference;
    }
}
