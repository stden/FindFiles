import java.io.File;

/**
 * Найдено ключевое слово
 */
public interface KeywordFoundListener {
    void keywordFound(File file, int line);
}
