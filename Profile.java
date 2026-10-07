import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Profile {

    public static void showProfile(int userId) {

        System.out.println("\n===== MY PROFILE =====");

        String sql = "SELECT name, email, role FROM users WHERE user_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("Name: "
                        + rs.getString("name"));

                System.out.println("Email: "
                        + rs.getString("email"));

                System.out.println("Role: "
                        + rs.getString("role"));

            } else {

                System.out.println("Profile not found!");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to load profile!");
            System.out.println(e.getMessage());
        }
    }
}