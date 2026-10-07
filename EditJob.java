import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class EditJob {

    public static void editJob(Scanner sc, int recruiterId) {

        System.out.println("\n===== EDIT JOB =====");

        System.out.print("Enter Job ID: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Job Title: ");
        String title = sc.nextLine();

        System.out.print("Enter New Company: ");
        String company = sc.nextLine();

        System.out.print("Enter New Location: ");
        String location = sc.nextLine();

        System.out.print("Enter New Salary: ");
        String salary = sc.nextLine();

        System.out.print("Enter New Skills: ");
        String skills = sc.nextLine();

        System.out.print("Enter New Description: ");
        String description = sc.nextLine();

        String sql = "UPDATE jobs SET title = ?, company = ?, " +
                     "location = ?, salary = ?, skills = ?, " +
                     "description = ? " +
                     "WHERE job_id = ? AND recruiter_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, company);
            ps.setString(3, location);
            ps.setString(4, salary);
            ps.setString(5, skills);
            ps.setString(6, description);
            ps.setInt(7, jobId);
            ps.setInt(8, recruiterId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Job Updated Successfully!");

            } else {

                System.out.println(
                    "Job not found or you are not the owner of this job!"
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to update job!");
            System.out.println(e.getMessage());
        }
    }
}