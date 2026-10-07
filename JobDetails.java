import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JobDetails {

    public static void showDetails(Scanner sc) {

        System.out.println("\n===== JOB DETAILS =====");

        System.out.print("Enter Job ID: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        String sql = "SELECT * FROM jobs WHERE job_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, jobId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nJob ID: "
                        + rs.getInt("job_id"));

                System.out.println("Title: "
                        + rs.getString("title"));

                System.out.println("Company: "
                        + rs.getString("company"));

                System.out.println("Location: "
                        + rs.getString("location"));

                System.out.println("Salary: "
                        + rs.getString("salary"));

                System.out.println("Skills: "
                        + rs.getString("skills"));

                System.out.println("Description: "
                        + rs.getString("description"));

            } else {

                System.out.println("Job not found!");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to load job details!");
            System.out.println(e.getMessage());
        }
    }
}