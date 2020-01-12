package com.olexyn.ensync;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    private final Execute x;

    public Tools() {
        x = new Execute();
    }


    /**
     */
    public void rsync(String param , String source , String destination) {
        //
        BufferedReader foo = x.execute(new String[]{"rsync", param, source, destination}).output;
    }

    public String getConf(){
        BufferedReader output = x.execute(new String[]{"cat", System.getProperty("user.dir")+"/src/com/olexyn/ensync/sync.conf"}).output;
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
}
