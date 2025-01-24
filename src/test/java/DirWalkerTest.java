
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class DirWalkerTest {
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
}
