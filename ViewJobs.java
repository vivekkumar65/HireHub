import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewJobs {

    public static void showJobs() {

        String sql = "SELECT * FROM jobs";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== AVAILABLE JOBS =====");

            while (rs.next()) {

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

            con.close();

        } catch (Exception e) {

            System.out.println("Unable to load jobs!");
            System.out.println(e.getMessage());
        }
    }
}