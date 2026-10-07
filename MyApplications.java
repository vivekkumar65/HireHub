import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyApplications {

    public static void showApplications(int userId) {

        System.out.println("\n===== MY APPLICATIONS =====");

        String sql = "SELECT applications.application_id, jobs.title, " +
                     "jobs.company, applications.status " +
                     "FROM applications " +
                     "JOIN jobs ON applications.job_id = jobs.job_id " +
                     "WHERE applications.user_id = ?";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Application ID: "
                        + rs.getInt("application_id"));

                System.out.println("Job: "
                        + rs.getString("title"));

                System.out.println("Company: "
                        + rs.getString("company"));

                System.out.println("Status: "
                        + rs.getString("status"));

                System.out.println("----------------------------");
            }

            if (!found) {
                System.out.println("No applications found.");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Unable to load applications!");
            System.out.println(e.getMessage());
        }
    }
}