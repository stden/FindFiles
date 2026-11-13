
import java.io.File;

/**
 * Обработчик события "Файл найден"
 */
public interface FileFoundListener {

    /**
     * Найден файл
     *
     * @param file файл
     */
    void fileFound(File file);
}
