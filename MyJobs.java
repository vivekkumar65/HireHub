import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyJobs {

    public static void showJobs(int recruiterId) {

        System.out.println("\n===== MY POSTED JOBS =====");

        String sql = "SELECT * FROM jobs WHERE recruiter_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, recruiterId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("Job ID: "
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

                System.out.println("----------------------------");
            }

            if (!found) {
                System.out.println("You have not posted any jobs.");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to load jobs!");
            System.out.println(e.getMessage());
        }
    }
}