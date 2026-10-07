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

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String sql =
                "SELECT * FROM users " +
                "WHERE email = ? " +
                "AND password = ? " +
                "AND LOWER(role) = LOWER(?)";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session =
                        request.getSession();

                session.setAttribute(
                        "userId",
                        rs.getInt("user_id")
                );

                session.setAttribute(
                        "userName",
                        rs.getString("name")
                );

                session.setAttribute(
                        "userEmail",
                        rs.getString("email")
                );

                session.setAttribute(
                        "userRole",
                        rs.getString("role")
                );

                // Recruiter Dashboard
                if (role.equalsIgnoreCase("recruiter")) {

                    response.sendRedirect(
                            "/HireHub/recruiter-dashboard.html"
                    );

                }

                // Job Seeker Dashboard
                else {

                    response.sendRedirect(
                            "/HireHub/jobseeker-dashboard.html"
                    );
                }

            } else {

                response.getWriter().println(
                        "<h1>Login Failed!</h1>"
                );

                response.getWriter().println(
                        "<p>Invalid Email, Password or Role.</p>"
                );

                response.getWriter().println(
                        "<a href='/HireHub/login.html'>"
                        + "Try Again"
                        + "</a>"
                );
            }

            rs.close();

        } catch (Exception e) {

            response.getWriter().println(
                    "<h1>Database Error!</h1>"
            );

            response.getWriter().println(
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}