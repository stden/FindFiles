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
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/FindFiles";

    public static Connection connectToDB() throws ClassNotFoundException, SQLException, IOException {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
    }

    public static void saveToDB(String keyword, String path, int line) {
        try {
            Connection connection = connectToDB();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO FindResult(keyword, path, line) VALUES (?, ?, ?)");
            statement.setString(1, keyword);
            statement.setString(2, path);
            statement.setInt(3, line);
            statement.executeUpdate();

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
