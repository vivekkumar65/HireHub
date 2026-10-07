import java.util.Scanner;

public class RecruiterMenu {

    public static void showMenu(Scanner sc, int recruiterId) {

        while (true) {

            System.out.println("\n===== RECRUITER MENU =====");
            System.out.println("1. Post Job");
            System.out.println("2. My Jobs");
            System.out.println("3. View Applicants");
            System.out.println("4. Update Application Status");
            System.out.println("5. Delete Job");
            System.out.println("6. Edit Job");
            System.out.println("7. My Profile");
            System.out.println("8. Edit Profile");
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                PostJob.postJob(sc, recruiterId);

            } else if (choice == 2) {

                MyJobs.showJobs(recruiterId);

            } else if (choice == 3) {

                ViewApplicants.showApplicants(recruiterId);

            } else if (choice == 4) {

                UpdateApplicationStatus.update(sc, recruiterId);

            } else if (choice == 5) {

                DeleteJob.deleteJob(sc, recruiterId);

            } else if (choice == 6) {

                EditJob.editJob(sc, recruiterId);

            } else if (choice == 7) {

                Profile.showProfile(recruiterId);

            } else if (choice == 8) {

                EditRecruiterProfile.edit(sc, recruiterId);

            } else if (choice == 9) {

                System.out.println("Logged out successfully!");
                break;

            } else {

                System.out.println("Invalid Choice!");
            }
        }
    }
}