import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class WithdrawApplication {

    public static void withdraw(Scanner sc, int userId) {

        System.out.println("\n===== WITHDRAW APPLICATION =====");

        System.out.print("Enter Application ID: ");
        int applicationId = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM applications " +
                     "WHERE application_id = ? AND user_id = ?";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, applicationId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                    "Application Withdrawn Successfully!"
                );

            } else {

                System.out.println(
                    "Application not found!"
                );
            }

            con.close();

        } catch (Exception e) {

            System.out.println(
                "Unable to withdraw application!"
            );

            System.out.println(e.getMessage());
        }
    }
}