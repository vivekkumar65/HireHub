import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class EditRecruiterProfile {

    public static void edit(Scanner sc, int recruiterId) {

        System.out.println("\n===== EDIT RECRUITER PROFILE =====");

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter New Email: ");
        String email = sc.nextLine();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Enter a valid email!");
            return;
        }

        String sql = "UPDATE users SET name = ?, email = ? " +
                     "WHERE user_id = ? AND role = 'Recruiter'";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, recruiterId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                    "Recruiter Profile Updated Successfully!"
                );

            } else {

                System.out.println("Profile not found!");
            }

            con.close();

        } catch (Exception e) {

            System.out.println(
                "Unable to update recruiter profile!"
            );

            if (e.getMessage().contains("Duplicate")) {

                System.out.println("Email already registered!");

            } else {

                System.out.println(e.getMessage());
            }
        }
    }
}