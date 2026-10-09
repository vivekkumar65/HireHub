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

public class MyJobsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/HireHub/login.html");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        if (role == null || !"Recruiter".equalsIgnoreCase(role)) {
            out.println(
                    "<p style='text-align:center;'>" +
                    "Access Denied" +
                    "</p>"
            );
            return;
        }

        Object userIdObject = session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {
            response.sendRedirect("/HireHub/login.html");
            return;
        }

        int recruiterId = (Integer) userIdObject;

        String sql =
                "SELECT job_id, title, company, location, salary, skills " +
                "FROM jobs " +
                "WHERE recruiter_id = ? " +
                "ORDER BY job_id DESC";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean jobsFound = false;

                while (rs.next()) {

                    jobsFound = true;

                    int jobId =
                            rs.getInt("job_id");

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

                    /*
                     * Same job-card structure as JobsServlet
                     */

                    out.println("<div class='job-card'>");

                    out.println("<div class='job-card-top'>");

                    out.println("<div class='company-icon'>");
                    out.println("💼");
                    out.println("</div>");

                    out.println("<div>");

                    out.println(
                            "<h2>" +
                            escapeHtml(title) +
                            "</h2>"
                    );

                    out.println(
                            "<p>" +
                            escapeHtml(company) +
                            "</p>"
                    );

                    out.println("</div>");

                    out.println("</div>");

                    out.println("<div class='job-info'>");

                    out.println(
                            "<span>📍 " +
                            escapeHtml(location) +
                            "</span>"
                    );

                    out.println(
                            "<span>💰 " +
                            escapeHtml(salary) +
                            "</span>"
                    );

                    out.println(
                            "<span>💼 Full Time</span>"
                    );

                    out.println("</div>");

                    out.println("<div class='job-skills'>");

                    if (skills != null &&
                        !skills.trim().isEmpty()) {

                        String[] skillsArray =
                                skills.split(",");

                        for (String skill : skillsArray) {

                            String cleanSkill =
                                    skill.trim();

                            if (!cleanSkill.isEmpty()) {

                                out.println(
                                        "<span>" +
                                        escapeHtml(cleanSkill) +
                                        "</span>"
                                );
                            }
                        }
                    }

                    out.println("</div>");

                    /*
                     * Recruiter actions
                     */

                    out.println("""
                            <div class="my-job-actions">
                            """);

                    out.println(
                            "<a class='view-details-btn' " +
                            "href='job-details.html?jobId=" +
                            jobId +
                            "'>" +
                            "View Details" +
                            "</a>"
                    );

                    out.println(
                            "<a class='edit-job-btn' " +
                            "href='edit-job.html?jobId=" +
                            jobId +
                            "'>" +
                            "✏️ Edit Job" +
                            "</a>"
                    );

                    out.println(
                            "<form method='POST' " +
                            "action='/HireHub/deleteJob' " +
                            "onsubmit=\"return confirm('Are you sure you want to delete this job?');\" " +
                            "style='display:inline; margin:0;'>"
                    );

                    out.println(
                            "<input type='hidden' " +
                            "name='jobId' " +
                            "value='" +
                            jobId +
                            "'>"
                    );

                    out.println(
                            "<button " +
                            "class='delete-job-btn' " +
                            "type='submit'>" +
                            "🗑️ Delete Job" +
                            "</button>"
                    );

                    out.println("</form>");

                    out.println("</div>");

                    out.println("</div>");
                }

                if (!jobsFound) {

                    out.println(
                            "<p style='text-align:center;'>" +
                            "You haven't posted any jobs yet." +
                            "</p>"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "MyJobsServlet Error: " +
                    e.getMessage()
            );

            out.println(
                    "<p style='text-align:center;'>" +
                    "Unable to load your jobs. Please try again later." +
                    "</p>"
            );
        }
    }

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}