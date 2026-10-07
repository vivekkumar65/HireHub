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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String jobId = request.getParameter("jobId");

        if (jobId == null || jobId.isEmpty()) {

            out.println("{\"error\":\"Job ID is missing\"}");
            return;
        }

        String sql =
                "SELECT * FROM jobs WHERE job_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, Integer.parseInt(jobId));

            ResultSet rs = ps.executeQuery();

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
                        "\"title\":\""
                        + escapeJson(title)
                        + "\","
                );

                out.println(
                        "\"company\":\""
                        + escapeJson(company)
                        + "\","
                );

                out.println(
                        "\"location\":\""
                        + escapeJson(location)
                        + "\","
                );

                out.println(
                        "\"salary\":\""
                        + escapeJson(salary)
                        + "\","
                );

                out.println(
                        "\"skills\":\""
                        + escapeJson(skills)
                        + "\","
                );

                out.println(
                        "\"description\":\""
                        + escapeJson(description)
                        + "\""
                );

                out.println("}");

            } else {

                out.println(
                        "{\"error\":\"Job not found\"}"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "JobDetailsServlet Error: "
                    + e.getMessage()
            );

            out.println(
                    "{\"error\":\"Unable to load job details\"}"
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
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}