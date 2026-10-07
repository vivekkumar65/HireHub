import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class PostJob {

    public static void postJob(Scanner sc, int recruiterId) {

        System.out.println("\n===== POST A JOB =====");

        System.out.print("Enter Job Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Company: ");
        String company = sc.nextLine();

        System.out.print("Enter Location: ");
        String location = sc.nextLine();

        System.out.print("Enter Salary: ");
        String salary = sc.nextLine();

        System.out.print("Enter Skills: ");
        String skills = sc.nextLine();

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        String sql = "INSERT INTO jobs " +
                "(title, company, location, salary, skills, description, recruiter_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, company);
            ps.setString(3, location);
            ps.setString(4, salary);
            ps.setString(5, skills);
            ps.setString(6, description);
            ps.setInt(7, recruiterId);

            ps.executeUpdate();

            System.out.println("Job Posted Successfully!");

            con.close();

        } catch (Exception e) {

            System.out.println("Job Posting Failed!");
            System.out.println(e.getMessage());
        }
    }
}