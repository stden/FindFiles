
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DirWalkerTest {
    private File tempDir;
    private List<File> foundFiles;
    
    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test_dir").toFile();
        foundFiles = new ArrayList<>();
    }

    @Test
    public void testDirWalkerCreation() {
        FileFoundListener listener = new FileFoundListener() {
            @Override
            public void fileFound(File file) {
                // Do nothing, just testing creation
            }
        };
        DirWalker walker = new DirWalker(listener);
        assertNotNull("DirWalker should be created successfully", walker);
    }

    @Test
    public void testWalkEmptyDirectory() {
        FileFoundListener listener = new FileFoundListener() {
            @Override
            public void fileFound(File file) {
                foundFiles.add(file);
            }
        };
        DirWalker walker = new DirWalker(listener);
        walker.walk(tempDir);
        assertEquals("Empty directory should find no files", 0, foundFiles.size());
    }

    @Test
    public void testFileFoundListenerNotNull() {
        Exception exception = null;
        try {
            new DirWalker(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull("Should throw exception for null listener", exception);
    }

    @Test
    public void testWalkNonExistentDirectory() {
        FileFoundListener listener = file -> foundFiles.add(file);
        DirWalker walker = new DirWalker(listener);
        File nonExistentDir = new File("non_existent_directory");
        walker.walk(nonExistentDir);
        assertEquals("Non-existent directory should find no files", 0, foundFiles.size());
    }
}
