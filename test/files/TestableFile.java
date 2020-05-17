package files;

import com.olexyn.ensync.Tools;

import java.io.File;

public class TestableFile extends File {

    Tools tools = new Tools();


    public TestableFile(String pathname) {
        super(pathname);
    }

    public boolean hasContent(String s){

        String line  = tools.fileToLines(this).get(0);
        return line.equals(s);
    }
}
