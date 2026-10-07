import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ApplyJob {

    public static void apply(Scanner sc, int userId) {

        System.out.println("\n===== APPLY FOR JOB =====");

        System.out.print("Enter Job ID: ");
        int jobId = sc.nextInt();
        sc.nextLine();

        try {

            Connection con = DatabaseConnection.getConnection();

            // Check whether job exists
            String jobSql = "SELECT * FROM jobs WHERE job_id = ?";

            PreparedStatement jobPs =
                    con.prepareStatement(jobSql);

            jobPs.setInt(1, jobId);

            ResultSet jobRs = jobPs.executeQuery();

            if (!jobRs.next()) {

                System.out.println("Job not found!");

                con.close();
                return;
            }

            // Check whether user already applied
            String checkSql =
                    "SELECT * FROM applications " +
                    "WHERE job_id = ? AND user_id = ?";

            PreparedStatement checkPs =
                    con.prepareStatement(checkSql);

            checkPs.setInt(1, jobId);
            checkPs.setInt(2, userId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                System.out.println(
                    "You have already applied for this job!"
                );

                con.close();
                return;
            }

            // Apply for job
            String sql =
                    "INSERT INTO applications " +
                    "(job_id, user_id, status) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, jobId);
            ps.setInt(2, userId);
            ps.setString(3, "Applied");

            ps.executeUpdate();

            System.out.println(
                "Application Submitted Successfully!"
            );

            con.close();

        } catch (Exception e) {

            System.out.println("Application Failed!");
            System.out.println(e.getMessage());
        }
    }
}