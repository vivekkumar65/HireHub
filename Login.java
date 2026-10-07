import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

    public static int userId;
    public static String role;

    public static boolean login(Scanner sc) {

        System.out.println("\n===== ONLINE JOB PORTAL LOGIN =====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Enter a valid email!");
            return false;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!");
            return false;
        }

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                userId = rs.getInt("user_id");
                role = rs.getString("role");

                System.out.println("\nLogin Successful!");
                System.out.println("Welcome " + rs.getString("name"));

                con.close();

                return true;

            } else {

                System.out.println("Invalid Email or Password!");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Login Failed!");
            System.out.println(e.getMessage());
        }

        return false;
    }
}