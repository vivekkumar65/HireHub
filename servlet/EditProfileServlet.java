package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Current login session
        HttpSession session =
                request.getSession(false);

        // Login check
        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect(
                    "/HireHub/login.html"
            );

            return;
        }

        // Logged-in user's ID
        int userId =
                (Integer) session.getAttribute("userId");

        // Form se data lena
        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        // Basic validation
        if (name == null ||
            name.trim().isEmpty() ||
            email == null ||
            email.trim().isEmpty()) {

            response.getWriter().println(
                    "<h1>Name and Email are required!</h1>"
            );

            return;
        }

        String sql =
                "UPDATE users SET " +
                "name = ?, email = ? " +
                "WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, name.trim());
            ps.setString(2, email.trim());
            ps.setInt(3, userId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                // Session me bhi updated name/email save karo
                session.setAttribute(
                        "userName",
                        name.trim()
                );

                session.setAttribute(
                        "userEmail",
                        email.trim()
                );

                response.getWriter().println(
                        "<h1>Profile Updated Successfully!</h1>"
                );

                response.getWriter().println(
                        "<p>Your profile has been updated.</p>"
                );

                response.getWriter().println(
                        "<br>"
                );

                response.getWriter().println(
                        "<a href='/HireHub/profile.html'>"
                        + "View Profile"
                        + "</a>"
                );

            } else {

                response.getWriter().println(
                        "<h1>Profile Update Failed!</h1>"
                );

            }

        } catch (Exception e) {

            response.getWriter().println(
                    "<h1>Unable to Update Profile!</h1>"
            );

            response.getWriter().println(
                    "<p>"
                    + e.getMessage()
                    + "</p>"
            );
        }
    }
}