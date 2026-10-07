import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/jobportal";

    private static final String USERNAME =
            "root";

    private static final String PASSWORD =
            System.getenv("JOBPORTAL_DB_PASSWORD");

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

        } catch (Exception e) {

            System.out.println(
                    "Database Connection Failed!"
            );

            System.out.println(
                    e.getMessage()
            );

            return null;
        }
    }
}