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
            out.println("<p style='text-align:center;'>Access Denied</p>");
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

        out.println("""
                <style>

                    .jobs-grid {
                        width: 100%;
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 22px;
                    }

                    .job-card {
                        background: white;
                        border: 1px solid #e2e8f0;
                        border-radius: 14px;
                        padding: 24px;
                        box-shadow: 0 5px 20px rgba(0,0,0,0.06);
                    }

                    .job-card-top {
                        display: flex;
                        gap: 14px;
                        align-items: center;
                        margin-bottom: 18px;
                    }

                    .company-icon {
                        width: 48px;
                        height: 48px;
                        background: #eff6ff;
                        border-radius: 10px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 23px;
                    }

                    .job-card h2 {
                        font-size: 20px;
                        color: #111827;
                        margin: 0 0 5px 0;
                    }

                    .job-card-top p {
                        color: #64748b;
                        font-size: 14px;
                        margin: 0;
                    }

                    .job-info {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 8px;
                        margin-bottom: 16px;
                    }

                    .job-info span {
                        background: #f3f4f6;
                        padding: 7px 9px;
                        border-radius: 6px;
                        font-size: 13px;
                        color: #475569;
                    }

                    .job-skills {
                        display: flex;
                        flex-wrap: wrap;
                        gap: 7px;
                        margin-bottom: 18px;
                    }

                    .job-skills span {
                        background: #eff6ff;
                        color: #2563eb;
                        padding: 6px 9px;
                        border-radius: 6px;
                        font-size: 12px;
                    }

                    .my-job-actions {
                        display: flex;
                        gap: 8px;
                        flex-wrap: wrap;
                        border-top: 1px solid #e5e7eb;
                        padding-top: 16px;
                    }

                    .my-job-actions a,
                    .my-job-actions button {
                        border: none;
                        border-radius: 7px;
                        padding: 9px 12px;
                        font-size: 13px;
                        cursor: pointer;
                        text-decoration: none;
                        font-weight: 600;
                    }

                    .view-details-btn {
                        background: #2563eb;
                        color: white;
                    }

                    .edit-job-btn {
                        background: #fef3c7;
                        color: #92400e;
                    }

                    .delete-job-btn {
                        background: #fee2e2;
                        color: #dc2626;
                    }

                    .my-job-actions form {
                        margin: 0;
                    }

                    .my-jobs-empty {
                        grid-column: 1 / -1;
                        background: white;
                        border-radius: 14px;
                        padding: 55px 25px;
                        text-align: center;
                        border: 1px solid #e2e8f0;
                    }

                    .my-jobs-empty-icon {
                        font-size: 45px;
                        margin-bottom: 15px;
                    }

                    .my-jobs-empty h2 {
                        margin-bottom: 8px;
                    }

                    .my-jobs-empty p {
                        color: #64748b;
                        margin-bottom: 20px;
                        color: #64748b;
                    }

                    .post-first-job-btn {
                        display: inline-block;
                        background: #2563eb;
                        color: white;
                        padding: 11px 18px;
                        border-radius: 8px;
                        text-decoration: none;
                        font-weight: 600;
                    }

                    @media (max-width: 600px) {

                        .jobs-grid {
                            grid-template-columns: 1fr;
                        }

                    }

                </style>

                <div class="jobs-grid">
                """);

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean jobsFound = false;

                while (rs.next()) {

                    jobsFound = true;

                    int jobId = rs.getInt("job_id");

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

                    out.println("<div class='job-card'>");

                    out.println("<div class='job-card-top'>");

                    out.println("""
                            <div class="company-icon">
                                💼
                            </div>
                            """);

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

                    out.println("<div class='my-job-actions'>");

                    out.println(
                            "<a class='view-details-btn' " +
                            "href='/HireHub/job-details.html?jobId=" +
                            jobId +
                            "'>" +
                            "View Details" +
                            "</a>"
                    );

                    out.println(
                            "<a class='edit-job-btn' " +
                            "href='/HireHub/edit-job.html?jobId=" +
                            jobId +
                            "'>" +
                            "✏️ Edit Job" +
                            "</a>"
                    );

                    out.println(
                            "<form method='POST' " +
                            "action='/HireHub/deleteJob' " +
                            "onsubmit=\"return confirm('Are you sure you want to delete this job?');\">"
                    );

                    out.println(
                            "<input type='hidden' " +
                            "name='jobId' " +
                            "value='" +
                            jobId +
                            "'>"
                    );

                    out.println(
                            "<button class='delete-job-btn' " +
                            "type='submit'>" +
                            "🗑️ Delete Job" +
                            "</button>"
                    );

                    out.println("</form>");

                    out.println("</div>");

                    out.println("</div>");
                }

                if (!jobsFound) {

                    out.println("""
                            <div class="my-jobs-empty">

                                <div class="my-jobs-empty-icon">
                                    📋
                                </div>

                                <h2>No Jobs Posted Yet</h2>

                                <p>
                                    You haven't posted any jobs yet.
                                </p>

                                <a
                                    class="post-first-job-btn"
                                    href="/HireHub/post-job.html">
                                    Post Your First Job
                                </a>

                            </div>
                            """);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "MyJobsServlet Error: " +
                    e.getMessage()
            );

            out.println("""
                    <div class="my-jobs-empty">

                        <div class="my-jobs-empty-icon">
                            ⚠️
                        </div>

                        <h2>Unable to Load Jobs</h2>

                        <p>
                            Something went wrong while loading your posted jobs.
                        </p>

                    </div>
                    """);
        }

        out.println("</div>");
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