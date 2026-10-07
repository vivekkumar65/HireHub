import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewApplicants {

    public static void showApplicants(int recruiterId) {

        System.out.println("\n===== VIEW APPLICANTS =====");

        String sql = "SELECT jobs.title, users.name, users.email, " +
                "applications.status " +
                "FROM applications " +
                "JOIN jobs ON applications.job_id = jobs.job_id " +
                "JOIN users ON applications.user_id = users.user_id " +
                "WHERE jobs.recruiter_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, recruiterId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Job: "
                        + rs.getString("title"));

                System.out.println("Applicant: "
                        + rs.getString("name"));

                System.out.println("Email: "
                        + rs.getString("email"));

                System.out.println("Status: "
                        + rs.getString("status"));

                System.out.println("----------------------------");
            }

            if (!found) {
                System.out.println("No applicants found.");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to load applicants!");
            System.out.println(e.getMessage());
        }
    }
}