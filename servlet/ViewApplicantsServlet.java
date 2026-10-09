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

public class ViewApplicantsServlet extends HttpServlet {

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

        PrintWriter out = response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            out.println("[]");
            return;
        }

        Object userIdObject =
                session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {

            out.println("[]");
            return;
        }

        int recruiterId =
                (Integer) userIdObject;

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
                !role.equalsIgnoreCase("Recruiter")) {

            out.println("[]");
            return;
        }

        String sql =
                "SELECT " +
                "applications.application_id, " +
                "applications.job_id, " +
                "jobs.title, " +
                "jobs.company, " +
                "jobs.location, " +
                "users.name, " +
                "users.email, " +
                "applications.status " +
                "FROM applications " +
                "JOIN jobs " +
                "ON applications.job_id = jobs.job_id " +
                "JOIN users " +
                "ON applications.user_id = users.user_id " +
                "WHERE jobs.recruiter_id = ? " +
                "ORDER BY applications.application_id DESC";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, recruiterId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                out.println("[");

                boolean first = true;

                while (rs.next()) {

                    if (!first) {
                        out.println(",");
                    }

                    first = false;

                    int applicationId =
                            rs.getInt("application_id");

                    int jobId =
                            rs.getInt("job_id");

                    String title =
                            rs.getString("title");

                    String company =
                            rs.getString("company");

                    String location =
                            rs.getString("location");

                    String name =
                            rs.getString("name");

                    String email =
                            rs.getString("email");

                    String status =
                            rs.getString("status");

                    out.println("{");

                    out.println(
                            "\"applicationId\":" +
                            applicationId + ","
                    );

                    out.println(
                            "\"jobId\":" +
                            jobId + ","
                    );

                    out.println(
                            "\"title\":\"" +
                            escapeJson(title) +
                            "\","
                    );

                    out.println(
                            "\"company\":\"" +
                            escapeJson(company) +
                            "\","
                    );

                    out.println(
                            "\"location\":\"" +
                            escapeJson(location) +
                            "\","
                    );

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
                            "\"status\":\"" +
                            escapeJson(status) +
                            "\""
                    );

                    out.println("}");
                }

                out.println("]");
            }

        } catch (Exception e) {

            System.err.println(
                    "ViewApplicantsServlet Error: " +
                    e.getMessage()
            );

            out.println("[]");
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