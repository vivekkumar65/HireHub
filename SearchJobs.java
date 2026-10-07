import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SearchJobs {

    public static void search(Scanner sc) {

        System.out.println("\n===== SEARCH JOBS =====");

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        String sql = "SELECT * FROM jobs " +
                     "WHERE title LIKE ? " +
                     "OR company LIKE ? " +
                     "OR location LIKE ? " +
                     "OR skills LIKE ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nJob ID: " +
                        rs.getInt("job_id"));

                System.out.println("Title: " +
                        rs.getString("title"));

                System.out.println("Company: " +
                        rs.getString("company"));

                System.out.println("Location: " +
                        rs.getString("location"));

                System.out.println("Salary: " +
                        rs.getString("salary"));

                System.out.println("Skills: " +
                        rs.getString("skills"));

                System.out.println("----------------------------");
            }

            if (!found) {
                System.out.println("No jobs found.");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Search Failed!");
            System.out.println(e.getMessage());
        }
    }
}