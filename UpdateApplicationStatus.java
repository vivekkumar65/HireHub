import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class UpdateApplicationStatus {

    public static void update(Scanner sc, int recruiterId) {

        System.out.println("\n===== UPDATE APPLICATION STATUS =====");

        System.out.print("Enter Application ID: ");
        int applicationId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Status (Selected/Rejected): ");
        String status = sc.nextLine();

        if (!status.equalsIgnoreCase("Selected")
                && !status.equalsIgnoreCase("Rejected")) {

            System.out.println(
                "Invalid Status! Use Selected or Rejected."
            );

            return;
        }

        String sql = "UPDATE applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "SET a.status = ? " +
                     "WHERE a.application_id = ? " +
                     "AND j.recruiter_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.setInt(3, recruiterId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                    "Application Status Updated Successfully!"
                );

            } else {

                System.out.println(
                    "Application not found!"
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println(
                "Unable to update status!"
            );

            System.out.println(e.getMessage());
        }
    }
}