package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // Current login session
        HttpSession session =
                request.getSession(false);

        // Login check
        if (session == null ||
            session.getAttribute("userId") == null) {

            out.println(
                "{\"error\":\"Please login first\"}"
            );

            return;
        }

        // Logged-in user's ID
        int userId =
                (Integer) session.getAttribute("userId");

        String sql =
                "SELECT name, email, role " +
                "FROM users WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                out.println("{");

                out.println(
                    "\"name\":\""
                    + escapeJson(rs.getString("name"))
                    + "\","
                );

                out.println(
                    "\"email\":\""
                    + escapeJson(rs.getString("email"))
                    + "\","
                );

                out.println(
                    "\"role\":\""
                    + escapeJson(rs.getString("role"))
                    + "\""
                );

                out.println("}");

            } else {

                out.println(
                    "{\"error\":\"User not found\"}"
                );
            }

        } catch (Exception e) {

            out.println(
                "{\"error\":\"Unable to load profile\"}"
            );

            System.out.println(
                "ProfileServlet Error: "
                + e.getMessage()
            );
        }
    }

    // JSON special characters handle karne ke liye
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}