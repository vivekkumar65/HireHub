package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplyJobServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session =
                request.getSession(false);


        // ==========================================
        // CHECK LOGIN
        // ==========================================

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect(
                "/HireHub/login.html"
            );

            return;
        }


        // ==========================================
        // GET USER INFORMATION
        // ==========================================

        int userId =
                (Integer) session.getAttribute(
                    "userId"
                );

        String role =
                (String) session.getAttribute(
                    "userRole"
                );


        // ==========================================
        // ONLY JOB SEEKER CAN APPLY
        // ==========================================

        if (role == null ||
            !role.equalsIgnoreCase("Job Seeker")) {

            response.getWriter().println(
                "<h1>Access Denied!</h1>"
            );

            response.getWriter().println(
                "<p>Only Job Seekers can apply for jobs.</p>"
            );

            response.getWriter().println(
                "<br><a href='/HireHub/jobs.html'>" +
                "Back to Jobs</a>"
            );

            return;
        }


        // ==========================================
        // GET JOB ID
        // ==========================================

        String jobIdText =
                request.getParameter("jobId");


        if (jobIdText == null ||
            jobIdText.trim().isEmpty()) {

            response.getWriter().println(
                "<h1>Job ID is missing!</h1>"
            );

            return;
        }


        int jobId;

        try {

            jobId =
                Integer.parseInt(
                    jobIdText.trim()
                );

        } catch (NumberFormatException e) {

            response.getWriter().println(
                "<h1>Invalid Job ID!</h1>"
            );

            return;
        }


        // ==========================================
        // DATABASE
        // ==========================================

        try (
            Connection con =
                DatabaseConnection.getConnection()
        ) {


            // ======================================
            // CHECK IF JOB EXISTS
            // ======================================

            String jobCheckSql =
                "SELECT job_id FROM jobs " +
                "WHERE job_id = ?";


            try (
                PreparedStatement jobCheck =
                    con.prepareStatement(
                        jobCheckSql
                    )
            ) {

                jobCheck.setInt(
                    1,
                    jobId
                );

                ResultSet rs =
                    jobCheck.executeQuery();


                if (!rs.next()) {

                    response.getWriter().println(
                        "<h1>Job Not Found!</h1>"
                    );

                    response.getWriter().println(
                        "<p>This job does not exist.</p>"
                    );

                    response.getWriter().println(
                        "<br><a href='/HireHub/jobs.html'>" +
                        "Back to Jobs</a>"
                    );

                    return;
                }
            }


            // ======================================
            // CHECK DUPLICATE APPLICATION
            // ======================================

            String checkSql =
                "SELECT application_id, status " +
                "FROM applications " +
                "WHERE job_id = ? " +
                "AND user_id = ?";


            try (
                PreparedStatement checkPs =
                    con.prepareStatement(
                        checkSql
                    )
            ) {

                checkPs.setInt(
                    1,
                    jobId
                );

                checkPs.setInt(
                    2,
                    userId
                );


                ResultSet rs =
                    checkPs.executeQuery();


                if (rs.next()) {

                    response.getWriter().println(
                        "<h1>Already Applied!</h1>"
                    );

                    response.getWriter().println(
                        "<p>" +
                        "You have already applied for this job." +
                        "</p>"
                    );

                    response.getWriter().println(
                        "<br>"
                    );

                    response.getWriter().println(
                        "<a href='/HireHub/my-applications.html'>" +
                        "View My Applications" +
                        "</a>"
                    );

                    return;
                }
            }


            // ======================================
            // INSERT NEW APPLICATION
            // ======================================

            String insertSql =
                "INSERT INTO applications " +
                "(job_id, user_id, status) " +
                "VALUES (?, ?, ?)";


            try (
                PreparedStatement ps =
                    con.prepareStatement(
                        insertSql
                    )
            ) {

                ps.setInt(
                    1,
                    jobId
                );

                ps.setInt(
                    2,
                    userId
                );

                ps.setString(
                    3,
                    "Applied"
                );


                int rows =
                    ps.executeUpdate();


                if (rows > 0) {

                    response.getWriter().println(
                        "<h1>" +
                        "Application Submitted Successfully!" +
                        "</h1>"
                    );

                    response.getWriter().println(
                        "<p>" +
                        "Your application has been submitted." +
                        "</p>"
                    );

                    response.getWriter().println(
                        "<br>"
                    );

                    response.getWriter().println(
                        "<a href='/HireHub/my-applications.html'>" +
                        "View My Applications" +
                        "</a>"
                    );

                } else {

                    response.getWriter().println(
                        "<h1>Application Failed!</h1>"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                "ApplyJobServlet Error: "
                + e.getMessage()
            );

            response.getWriter().println(
                "<h1>Unable to Submit Application!</h1>"
            );

            response.getWriter().println(
                "<p>" +
                e.getMessage() +
                "</p>"
            );
        }
    }
}