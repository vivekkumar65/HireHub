package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JobDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json;charset=UTF-8"
        );

        PrintWriter out =
                response.getWriter();

        String jobIdText =
                request.getParameter("jobId");

        if (jobIdText == null ||
            jobIdText.trim().isEmpty()) {

            out.println(
                    "{\"error\":\"Job ID is missing\"}"
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

            out.println(
                    "{\"error\":\"Invalid Job ID\"}"
            );

            return;
        }

        String sql =
                "SELECT title, company, location, " +
                "salary, skills, description " +
                "FROM jobs " +
                "WHERE job_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, jobId);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    String title =
                            rs.getString("title");

                    String company =
                            rs.getString("company");

                    String location =
                            rs.getString("location");

                    String salary =
                            rs.getString("salary");

                    String skills =
                            rs.getString("skills");

                    String description =
                            rs.getString("description");

                    out.println("{");

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
                            "\"salary\":\"" +
                            escapeJson(salary) +
                            "\","
                    );

                    out.println(
                            "\"skills\":\"" +
                            escapeJson(skills) +
                            "\","
                    );

                    out.println(
                            "\"description\":\"" +
                            escapeJson(description) +
                            "\""
                    );

                    out.println("}");

                } else {

                    out.println(
                            "{\"error\":\"Job not found\"}"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JobDetailsServlet Error: " +
                    e.getMessage()
            );

            out.println(
                    "{\"error\":\"Unable to load job details\"}"
            );
        }
    }

    private String escapeJson(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }
}