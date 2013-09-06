import java.io.File;
import java.io.FilenameFilter;

/**
 * Рекурсивный обход каталогов
 */
public class DirWalker {
    private final FileFoundListener fileFoundListener;

    public DirWalker(FileFoundListener fileFoundListener) {
        this.fileFoundListener = fileFoundListener;
    }

    public void walk(File root) {
        File[] list = root.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase().endsWith(".txt");
            }
        });

        if (list == null) return;

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f);
                System.out.println("Dir: " + f.getAbsoluteFile());
            } else {
                System.out.println("File: " + f.getAbsoluteFile());
                fileFoundListener.fileFound(f);
            }
        }
    }
}
