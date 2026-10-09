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

        response.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        response.setHeader(
                "Pragma",
                "no-cache"
        );

        PrintWriter out =
                response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            out.println(
                    "{\"error\":\"Please login first\"}"
            );

            return;
        }

        Object userIdObject =
                session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {

            out.println(
                    "{\"error\":\"Invalid session\"}"
            );

            return;
        }

        int userId =
                (Integer) userIdObject;

        if (userId <= 0) {

            out.println(
                    "{\"error\":\"Invalid user ID\"}"
            );

            return;
        }

        String sql =
                "SELECT name, email, role " +
                "FROM users " +
                "WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    String name =
                            rs.getString("name");

                    String email =
                            rs.getString("email");

                    String role =
                            rs.getString("role");

                    out.println("{");

                    out.println(
                            "\"name\":\"" +
                            escapeJson(name) +
                            "\","
                    );

                    out.println(
                            "\"email\":\"" +
                            escapeJson(email) +
                            "\","
                    );

                    out.println(
                            "\"role\":\"" +
                            escapeJson(role) +
                            "\""
                    );

                    out.println("}");

                } else {

                    out.println(
                            "{\"error\":\"User not found\"}"
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "ProfileServlet Error: " +
                    e.getMessage()
            );

            out.println(
                    "{\"error\":\"Unable to load profile\"}"
            );
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}