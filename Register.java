import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Register {

    public static void register(Scanner sc) {

        System.out.println("\n===== ONLINE JOB PORTAL REGISTRATION =====");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Enter a valid email!");
            return;
        }

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (password.length() < 5) {
            System.out.println("Password must contain at least 5 characters!");
            return;
        }

        System.out.print("Enter Role (Job Seeker / Recruiter): ");
        String role = sc.nextLine();

        if (!role.equalsIgnoreCase("Job Seeker")
                && !role.equalsIgnoreCase("Recruiter")) {

            System.out.println("Invalid role!");
            System.out.println("Use: Job Seeker or Recruiter");
            return;
        }

        String sql = "INSERT INTO users " +
                     "(name, email, password, role) " +
                     "VALUES (?, ?, ?, ?)";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();

            System.out.println("Registration Successful!");

            con.close();

        } catch (Exception e) {

            if (e.getMessage().contains("Duplicate")) {
                System.out.println("Email already registered!");
            } else {
                System.out.println("Registration Failed!");
                System.out.println(e.getMessage());
            }
        }
    }
}