package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateApplicationStatusServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please login first."
            );

            return;
        }

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
            !role.equalsIgnoreCase("Recruiter")) {

            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Only recruiters can update application status."
            );

            return;
        }

        int recruiterId =
                (Integer) session.getAttribute("userId");


        String applicationIdText =
                request.getParameter("applicationId");

        String status =
                request.getParameter("status");


        if (applicationIdText == null ||
            applicationIdText.trim().isEmpty()) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Application ID is missing."
            );

            return;
        }


        if (status == null ||
            status.trim().isEmpty()) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Status is missing."
            );

            return;
        }


        int applicationId;

        try {

            applicationId =
                    Integer.parseInt(
                        applicationIdText.trim()
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid Application ID."
            );

            return;
        }


        if (!status.equals("Applied") &&
            !status.equals("Shortlisted") &&
            !status.equals("Selected") &&
            !status.equals("Rejected")) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid application status."
            );

            return;
        }


        String sql =
                "UPDATE applications a " +
                "JOIN jobs j ON a.job_id = j.job_id " +
                "SET a.status = ? " +
                "WHERE a.application_id = ? " +
                "AND j.recruiter_id = ?";


        try (
            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.setInt(3, recruiterId);


            int rows =
                    ps.executeUpdate();


            if (rows > 0) {

                response.setStatus(
                    HttpServletResponse.SC_OK
                );

                response.getWriter().println(
                    "Status updated successfully."
                );

            } else {

                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Application not found or this application does not belong to your job."
                );
            }


        } catch (Exception e) {

            System.out.println(
                "UpdateApplicationStatusServlet Error: "
                + e.getMessage()
            );

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to update application status."
            );
        }
    }
}