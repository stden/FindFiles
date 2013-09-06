
import java.io.File;

/**
 * Обработчик события "Найдено ключевое слово"
 */
public interface KeywordFoundListener {

    /**
     * Найдено ключевое слово
     *
     * @param file Файл
     * @param line Номер строки
     */
    void keywordFound(File file, int line);
}
