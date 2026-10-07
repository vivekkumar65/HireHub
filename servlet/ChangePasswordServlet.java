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

public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Check login session
        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect(
                    "/HireHub/login.html"
            );

            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        // Get passwords from form
        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");

        // Check empty fields
        if (currentPassword == null ||
            newPassword == null ||
            confirmPassword == null ||
            currentPassword.isEmpty() ||
            newPassword.isEmpty() ||
            confirmPassword.isEmpty()) {

            response.getWriter().println(
                    "<h1>All fields are required!</h1>"
            );

            return;
        }

        // Check new password confirmation
        if (!newPassword.equals(confirmPassword)) {

            response.getWriter().println(
                    "<h1>New Passwords Do Not Match!</h1>"
            );

            response.getWriter().println(
                    "<p>Please enter the same password in both fields.</p>"
            );

            return;
        }

        String checkSql =
                "SELECT password FROM users " +
                "WHERE user_id = ?";

        String updateSql =
                "UPDATE users SET password = ? " +
                "WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement checkPs =
                        con.prepareStatement(checkSql)
        ) {

            // Get current password from database
            checkPs.setInt(1, userId);

            ResultSet rs =
                    checkPs.executeQuery();

            if (!rs.next()) {

                response.getWriter().println(
                        "<h1>User Not Found!</h1>"
                );

                return;
            }

            String databasePassword =
                    rs.getString("password");

            // Verify current password
            if (!databasePassword.equals(currentPassword)) {

                response.getWriter().println(
                        "<h1>Current Password is Incorrect!</h1>"
                );

                return;
            }

            // Update password
            try (
                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql)
            ) {

                updatePs.setString(1, newPassword);
                updatePs.setInt(2, userId);

                int rows =
                        updatePs.executeUpdate();

                if (rows > 0) {

                    response.getWriter().println(
                            "<h1>Password Changed Successfully!</h1>"
                    );

                    response.getWriter().println(
                            "<p>Your password has been updated.</p>"
                    );

                    response.getWriter().println(
                            "<br>"
                    );

                    response.getWriter().println(
                            "<a href='/HireHub/profile.html'>"
                            + "Back to Profile"
                            + "</a>"
                    );

                } else {

                    response.getWriter().println(
                            "<h1>Password Change Failed!</h1>"
                    );
                }
            }

        } catch (Exception e) {

            response.getWriter().println(
                    "<h1>Unable to Change Password!</h1>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );

            System.out.println(
                    "ChangePasswordServlet Error: "
                    + e.getMessage()
            );
        }
    }
}