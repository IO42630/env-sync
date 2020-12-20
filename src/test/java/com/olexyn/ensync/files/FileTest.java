package com.olexyn.ensync.files;

import com.olexyn.ensync.Execute;
import com.olexyn.ensync.Tools;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTest {

    Execute x = new Execute();
    Tools tools = new Tools();

    private static final String PATH = System.getProperty("user.dir");

    private static final String fileAPath = "asdf";
    private static final String fileBPath = "asff";

    private final TestFile a = new TestFile(fileAPath);
    private final TestFile b = new TestFile(fileBPath);

    private List<String> createFile(File file) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            System.out.println("");
        }
        return new ArrayList<String>();
    }

    private List<String> updateFile(File file) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            System.out.println("");
        }
        return new ArrayList<String>();
    }


    private void deleteFile(File file) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            System.out.println("");
        }
    }

    private void cleanDirs() {
        deleteFile(a);
        deleteFile(b);
    }

    /**
     * Perform the 15 test cases in TestCases.xlsx.
     */
    @Test
    public void doFileTests() {

        List<String> sideloadContentA;
        List<String> sideloadContentB;

        // 1
        createFile(a);
        deleteFile(a);
        Assert.assertFalse(a.exists());
        Assert.assertFalse(b.exists());
        cleanDirs();
        // 2
        createFile(b);
        createFile(a);
        deleteFile(a);
        Assert.assertFalse(a.exists());
        Assert.assertFalse(b.exists());
        cleanDirs();
        // 3
        createFile(a);
        createFile(b);
        deleteFile(a);
        deleteFile(b);
        Assert.assertFalse(a.exists());
        Assert.assertFalse(b.exists());
        cleanDirs();
        // 4
        createFile(a);
        deleteFile(a);
        sideloadContentB = createFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        cleanDirs();
        // 5
        createFile(a);
        createFile(b);
        deleteFile(a);
        sideloadContentB = updateFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        cleanDirs();
        // 6
        sideloadContentA = createFile(a);
        Assert.assertEquals(sideloadContentA, b.updateContent().getContent());
        // 7
        createFile(b);
        createFile(a);
        Assert.assertEquals(sideloadContentA, b.updateContent().getContent());
        // 8
        createFile(a);
        createFile(b);
        deleteFile(b);
        Assert.assertFalse(a.exists());
        Assert.assertFalse(b.exists());
        cleanDirs();
        //9
        createFile(a);
        sideloadContentB = createFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        cleanDirs();
        // 10
        createFile(b);
        createFile(a);
        sideloadContentB = updateFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        // 11
        createFile(a);
        sideloadContentA = updateFile(a);
        Assert.assertEquals(sideloadContentA, b.updateContent().getContent());
        // 12
        createFile(a);
        createFile(b);
        sideloadContentA = updateFile(a);
        Assert.assertEquals(sideloadContentA, b.updateContent().getContent());
        // 13
        createFile(a);
        createFile(b);
        updateFile(a);
        deleteFile(b);
        Assert.assertFalse(a.exists());
        Assert.assertFalse(b.exists());
        cleanDirs();
        // 14
        createFile(a);
        updateFile(a);
        sideloadContentB = createFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        cleanDirs();
        // 15
        createFile(a);
        createFile(b);
        updateFile(a);
        sideloadContentB = updateFile(b);
        Assert.assertEquals(sideloadContentB, a.updateContent().getContent());
        cleanDirs();
    }

}
