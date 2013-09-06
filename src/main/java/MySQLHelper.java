
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Работа с БД MySQL
 */
public class MySQLHelper {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private final Connection connection;

    /**
     * @param connectionString Строка соединения с БД
     * @param userName Имя пользователя MySQL
     * @param password Пароль пользователя MySQL
     * @throws SQLException
     */
    public MySQLHelper(String connectionString, String userName, char[] password) throws SQLException {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        connection = DriverManager.getConnection(connectionString, userName, String.valueOf(password));
    }

    public void saveToDB(String keyword, String path, int line) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO FindResult(keyword, path, line) VALUES (?, ?, ?)");
            statement.setString(1, keyword);
            statement.setString(2, path);
            statement.setInt(3, line);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }
}
