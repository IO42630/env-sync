package files;

import com.olexyn.ensync.Execute;
import com.olexyn.ensync.Tools;

public class FileTest {

    Execute x = new Execute();
    Tools tools = new Tools();

    private static final String PATH = System.getProperty("user.dir");;

    public static void main(String... args){



    }

    /**
     * Simulates user activity on disk.
     */
    void createFiles() throws InterruptedException {
        StringBuilder sbA = new StringBuilder("a");
        StringBuilder sbB = new StringBuilder("b");

        // dv (deleted-void)
        // TODO

        // dd
        tools.writeSbToFile(PATH+"/a/dd", sbA);
        Thread.sleep(10);
        tools.writeSbToFile(PATH+"/b/dd", sbB);
        Thread.sleep(10);Thread.sleep(10);
        x.execute(new String[]{"rm", PATH+"/a/dd"});
        Thread.sleep(10);
        x.execute(new String[]{"rm", PATH+"/b/dd"});
        Thread.sleep(10);

        // dc
        tools.writeSbToFile(PATH+"/a/dc", sbA);
        Thread.sleep(10);
        x.execute(new String[]{"rm", PATH+"/a/dc"});
        Thread.sleep(10);
        tools.writeSbToFile(PATH+"/b/dc", sbB);
        Thread.sleep(10);

        // dm
        tools.writeSbToFile(PATH+"/a/dm", sbA);
        Thread.sleep(10);
        x.execute(new String[]{"rm", PATH+"/a/dm"});
        Thread.sleep(10);
        tools.writeSbToFile(PATH+"/b/dm", sbB);
        Thread.sleep(10);

        // dv (deleted-void)
        // TODO

        // cd
        // TODO

        // cc
        // TODO

        // cm
        // TODO

        // cv (created-void)
        // TODO

        // md
        // TODO

        // mc
        // TODO

        // mm
        // TODO

    }


    /**
     * Checks if end-state is as desired.
     * @throws Exception otherwise.
     */
    void fileTest() throws Exception {







        // Files where the second (= the newer) file was deleted. Thus both files should not exist in the end-state.
        String[] arrayToDelete = {"/a/dd", "/b/dd" , "/a/cd", "/b/cd", "/a/md", "/b/md"};
        for (String path : arrayToDelete){
            if (new TestableFile(path).exists()) throw new Exception();
        }

        // Files where the second (= the newer) file was created or modified. Thus both files should contain "b" in the end-state.
        String[] arrayToB = {"/a/dc", "/b/dc" , "/a/dm", "/b/dm", "/a/cc", "/b/cc"};
        for (String path : arrayToB){
            if (!new TestableFile(path).hasContent("b")) throw new Exception();
        }




    }


    // Assertion Exception






}
