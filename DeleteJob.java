import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DeleteJob {

    public static void deleteJob(Scanner sc, int recruiterId) {

        System.out.println("\n===== DELETE JOB =====");

        System.out.print("Enter Job ID: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM jobs WHERE job_id = ? AND recruiter_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, jobId);
            ps.setInt(2, recruiterId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Job Deleted Successfully!");

            } else {

                System.out.println(
                    "Job not found or you are not the owner of this job!"
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to delete job!");
            System.out.println(e.getMessage());
        }
    }
}