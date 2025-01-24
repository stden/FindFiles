
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Поиск ключевого слова в файле
 */
public class FileScanner {
    /**
     * Этот интерфейс обеспечивает метод для обработки событий нахождения ключевого слова.
     */
    public interface KeywordFoundListener {

        /**
         * Этот метод вызывается, когда ключевое слово найдено в файле.
         * 
         * @param file   Файл, в котором найдено ключевое слово
         * @param line   Линия, на которой найдено ключевое слово
         */
        void keywordFound(File file, int line);
    }

    /**
     * @param file Файл, в котором искать
     * @param keyword Ключевое слово (что искать)
     * @param keywordFoundListener Обработчик, когда ключевое слово найдено
     * @throws FileNotFoundException Ошибка: файл не найден
     */
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
