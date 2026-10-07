import java.util.Scanner;

public class JobSeekerMenu {

    public static void showMenu(Scanner sc, int userId) {

        while (true) {

            System.out.println("\n===== JOB SEEKER MENU =====");
            System.out.println("1. View Jobs");
            System.out.println("2. Search Jobs");
            System.out.println("3. View Job Details");
            System.out.println("4. Apply for Job");
            System.out.println("5. My Applications");
            System.out.println("6. Withdraw Application");
            System.out.println("7. My Profile");
            System.out.println("8. Edit Profile");
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                ViewJobs.showJobs();

            } else if (choice == 2) {

                SearchJobs.search(sc);

            } else if (choice == 3) {

                JobDetails.showDetails(sc);

            } else if (choice == 4) {

                ApplyJob.apply(sc, userId);

            } else if (choice == 5) {

                MyApplications.showApplications(userId);

            } else if (choice == 6) {

                WithdrawApplication.withdraw(sc, userId);

            } else if (choice == 7) {

                Profile.showProfile(userId);

            } else if (choice == 8) {

                EditProfile.edit(sc, userId);

            } else if (choice == 9) {

                System.out.println("Logged out successfully!");
                break;

            } else {

                System.out.println("Invalid Choice!");
            }
        }
    }
}