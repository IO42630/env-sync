package com.olexyn.ensync;

import java.util.ArrayList;
import java.util.List;

public class Routines {


    public List<String[]> parseConfToCmdBuffer(String conf) {


        List<String[]> cmdBuffer = new ArrayList<>();

        String[] confLines = conf.split("\n");
        for (int i = 0; i < confLines.length; i++) {
            String line = confLines[i];
            if (!line.startsWith("#")) {

                if (line.contains("----")) {
                    // dirA <- urvtW ---- urvtW -> dirB
                    String dirA = line.split(" ---- ")[0].split(" <- ")[0];
                    String optA = line.split(" ---- ")[0].split(" <- ")[1];
                    String optB = line.split(" ---- ")[1].split(" -> ")[0];
                    String dirB = line.split(" ---- ")[1].split(" -> ")[1];

                    cmdBuffer.add(new String[]{"rsync",
                                               "-" + optA,
                                               dirB,
                                               dirA});
                    cmdBuffer.add(new String[]{"rsync",
                                               "-" + optB,
                                               dirA,
                                               dirB});
                } else if (line.contains("->")) {
                    // dirA -- urvtW -> dirB
                    String dirA = line.split(" -- ")[0];
                    String optA = line.split(" -- ")[1].split(" -> ")[0];
                    String dirB = line.split(" -> ")[1];

                    cmdBuffer.add(new String[]{"rsync",
                                               "-" + optA,
                                               dirA,
                                               dirB});
                }


            }
        }
        return cmdBuffer;
    }
}