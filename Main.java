import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== ONLINE JOB PORTAL =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                Register.register(sc);

            } else if (choice == 2) {

                boolean loggedIn = Login.login(sc);

                if (loggedIn) {

                    // Role ko lowercase me convert karke check karenge
                    String userRole = Login.role.toLowerCase();

                    if (userRole.equals("job seeker")) {

                        JobSeekerMenu.showMenu(sc, Login.userId);

                    } else if (userRole.equals("recruiter")) {

                        RecruiterMenu.showMenu(sc, Login.userId);

                    } else {

                        System.out.println("Invalid role in database!");
                    }
                }

            } else if (choice == 3) {

                System.out.println(
                    "Thank you for using Online Job Portal!"
                );

                break;

            } else {

                System.out.println("Invalid Choice!");
            }
        }

        sc.close();
    }
}