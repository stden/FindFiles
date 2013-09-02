import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Поиск ключевого слова в файле
 */
public class FileScanner {
    public FileScanner(File file, String keyword, KeywordFoundListener keywordFoundListener) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(file));
        int line = 0;
        try {
            while (scanner.hasNextLine()) {
                line++;
                String s = scanner.nextLine();
                if (s.contains(keyword)) {
                    keywordFoundListener.keywordFound(file, line);
                }
            }
        } finally {
            scanner.close();
        }
    }
}
