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

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect("/HireHub/login.html");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        if (role == null ||
                !"Recruiter".equalsIgnoreCase(role)) {

            showError(
                    response,
                    "Access Denied",
                    "Only recruiters can view their posted jobs.",
                    "/HireHub/jobseeker-dashboard.html",
                    "Back to Dashboard"
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

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>My Jobs - HireHub</title>

                    <style>
                        * {
                            box-sizing: border-box;
                            margin: 0;
                            padding: 0;
                            font-family: Arial, sans-serif;
                        }

                        body {
                            background: #f5f7fb;
                            color: #1f2937;
                            min-height: 100vh;
                        }

                        .navbar {
                            background: #111827;
                            color: white;
                            padding: 16px 7%;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 20px;
                        }

                        .logo {
                            font-size: 25px;
                            font-weight: bold;
                        }

                        .nav-links {
                            display: flex;
                            gap: 20px;
                            align-items: center;
                            flex-wrap: wrap;
                        }

                        .nav-links a {
                            color: white;
                            text-decoration: none;
                            font-size: 14px;
                        }

                        .nav-links a:hover {
                            color: #60a5fa;
                        }

                        .container {
                            width: 90%;
                            max-width: 1100px;
                            margin: 45px auto;
                        }

                        .header {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 20px;
                            margin-bottom: 30px;
                            flex-wrap: wrap;
                        }

                        .header h1 {
                            font-size: 32px;
                            color: #111827;
                            margin-bottom: 7px;
                        }

                        .header p {
                            color: #6b7280;
                        }

                        .post-btn {
                            background: #2563eb;
                            color: white;
                            padding: 12px 20px;
                            border-radius: 8px;
                            text-decoration: none;
                            font-weight: bold;
                        }

                        .post-btn:hover {
                            background: #1d4ed8;
                        }

                        .jobs-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(310px, 1fr));
                            gap: 22px;
                        }

                        .job-card {
                            background: white;
                            border-radius: 14px;
                            padding: 24px;
                            box-shadow: 0 5px 20px rgba(0,0,0,0.07);
                            border: 1px solid #e5e7eb;
                        }

                        .job-card-top {
                            display: flex;
                            gap: 14px;
                            align-items: center;
                            margin-bottom: 20px;
                        }

                        .company-icon {
                            width: 48px;
                            height: 48px;
                            background: #eff6ff;
                            border-radius: 10px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            font-size: 24px;
                        }

                        .job-card h2 {
                            font-size: 20px;
                            color: #111827;
                            margin-bottom: 5px;
                        }

                        .company {
                            color: #6b7280;
                            font-size: 14px;
                        }

                        .job-info {
                            display: flex;
                            flex-wrap: wrap;
                            gap: 10px;
                            margin-bottom: 18px;
                        }

                        .job-info span {
                            background: #f3f4f6;
                            padding: 7px 10px;
                            border-radius: 6px;
                            font-size: 13px;
                            color: #4b5563;
                        }

                        .job-skills {
                            display: flex;
                            flex-wrap: wrap;
                            gap: 7px;
                            margin-bottom: 20px;
                        }

                        .job-skills span {
                            background: #eff6ff;
                            color: #2563eb;
                            padding: 6px 9px;
                            border-radius: 6px;
                            font-size: 12px;
                        }

                        .actions {
                            display: flex;
                            gap: 8px;
                            flex-wrap: wrap;
                            border-top: 1px solid #e5e7eb;
                            padding-top: 18px;
                        }

                        .actions a,
                        .actions button {
                            border: none;
                            border-radius: 7px;
                            padding: 9px 12px;
                            font-size: 13px;
                            cursor: pointer;
                            text-decoration: none;
                            font-weight: 600;
                        }

                        .view-btn {
                            background: #eff6ff;
                            color: #2563eb;
                        }

                        .edit-btn {
                            background: #fef3c7;
                            color: #92400e;
                        }

                        .delete-btn {
                            background: #fee2e2;
                            color: #dc2626;
                        }

                        .actions form {
                            margin: 0;
                        }

                        .empty {
                            background: white;
                            border-radius: 14px;
                            padding: 55px 25px;
                            text-align: center;
                            box-shadow: 0 5px 20px rgba(0,0,0,0.06);
                        }

                        .empty-icon {
                            font-size: 48px;
                            margin-bottom: 15px;
                        }

                        .empty h2 {
                            margin-bottom: 8px;
                            color: #111827;
                        }

                        .empty p {
                            color: #6b7280;
                            margin-bottom: 22px;
                        }

                        .footer {
                            text-align: center;
                            color: #9ca3af;
                            padding: 30px 15px;
                            font-size: 13px;
                        }

                        @media (max-width: 600px) {
                            .navbar {
                                flex-direction: column;
                                align-items: flex-start;
                            }

                            .container {
                                width: 94%;
                                margin: 30px auto;
                            }

                            .header h1 {
                                font-size: 27px;
                            }

                            .post-btn {
                                width: 100%;
                                text-align: center;
                            }
                        }
                    </style>
                </head>

                <body>

                <nav class="navbar">
                    <div class="logo">HireHub</div>

                    <div class="nav-links">
                        <a href="/HireHub/recruiter-dashboard.html">Dashboard</a>
                        <a href="/HireHub/jobs.html">Jobs</a>
                        <a href="/HireHub/view-applicants.html">Applicants</a>
                        <a href="/HireHub/profile.html">Profile</a>
                    </div>
                </nav>

                <main class="container">

                    <div class="header">
                        <div>
                            <h1>My Jobs</h1>
                            <p>Manage the jobs you have posted on HireHub.</p>
                        </div>

                        <a class="post-btn"
                           href="/HireHub/post-job.html">
                            + Post New Job
                        </a>
                    </div>

                    <div class="jobs-grid">
                """);

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, recruiterId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {

                    found = true;

                    int jobId = rs.getInt("job_id");

                    String title = rs.getString("title");
                    String company = rs.getString("company");
                    String location = rs.getString("location");
                    String salary = rs.getString("salary");
                    String skills = rs.getString("skills");

                    out.println("<div class='job-card'>");

                    out.println("""
                            <div class="job-card-top">
                                <div class="company-icon">💼</div>
                                <div>
                            """);

                    out.println(
                            "<h2>" +
                            escapeHtml(title) +
                            "</h2>"
                    );

                    out.println(
                            "<p class='company'>" +
                            escapeHtml(company) +
                            "</p>"
                    );

                    out.println("""
                                </div>
                            </div>
                            """);

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

                    if (skills != null && !skills.trim().isEmpty()) {

                        String[] skillList = skills.split(",");

                        for (String skill : skillList) {

                            String cleanSkill = skill.trim();

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

                    out.println("""
                            <div class="actions">
                            """);

                    out.println(
                            "<a class='view-btn' href='/HireHub/job-details.html?jobId=" +
                            jobId +
                            "'>👁️ View Details</a>"
                    );

                    out.println(
                            "<a class='edit-btn' href='/HireHub/edit-job.html?jobId=" +
                            jobId +
                            "'>✏️ Edit Job</a>"
                    );

                    out.println(
                            "<form method='POST' action='/HireHub/deleteJob' " +
                            "onsubmit=\"return confirm('Are you sure you want to delete this job?');\">"
                    );

                    out.println(
                            "<input type='hidden' name='jobId' value='" +
                            jobId +
                            "'>"
                    );

                    out.println(
                            "<button class='delete-btn' type='submit'>" +
                            "🗑️ Delete Job" +
                            "</button>"
                    );

                    out.println("</form>");

                    out.println("</div>");
                    out.println("</div>");
                }

                out.println("</div>");

                if (!found) {

                    out.println("""
                            <div class="empty">
                                <div class="empty-icon">📋</div>
                                <h2>No Jobs Posted Yet</h2>
                                <p>You haven't posted any jobs yet.</p>
                                <a class="post-btn"
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
                    </div>

                    <div class="empty">
                        <div class="empty-icon">⚠️</div>
                        <h2>Unable to Load Jobs</h2>
                        <p>Something went wrong while loading your posted jobs.</p>

                        <a class="post-btn"
                           href="/HireHub/recruiter-dashboard.html">
                            Back to Dashboard
                        </a>
                    </div>
                    """);
        }

        out.println("""
                </main>

                <footer class="footer">
                    © 2026 HireHub. All rights reserved.
                </footer>

                </body>
                </html>
                """);
    }

    private void showError(
            HttpServletResponse response,
            String title,
            String message,
            String buttonLink,
            String buttonText)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>HireHub</title>

                    <style>
                        * {
                            box-sizing: border-box;
                            font-family: Arial, sans-serif;
                        }

                        body {
                            margin: 0;
                            background: #f5f7fb;
                            min-height: 100vh;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                        }

                        .card {
                            width: 90%;
                            max-width: 500px;
                            background: white;
                            padding: 40px;
                            border-radius: 16px;
                            text-align: center;
                            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
                        }

                        .icon {
                            font-size: 50px;
                            margin-bottom: 15px;
                        }

                        h1 {
                            color: #111827;
                            margin-bottom: 10px;
                        }

                        p {
                            color: #6b7280;
                            margin-bottom: 25px;
                        }

                        a {
                            display: inline-block;
                            background: #2563eb;
                            color: white;
                            text-decoration: none;
                            padding: 12px 22px;
                            border-radius: 8px;
                            font-weight: bold;
                        }
                    </style>
                </head>

                <body>
                    <div class="card">
                        <div class="icon">⚠️</div>
                """);

        out.println(
                "<h1>" +
                escapeHtml(title) +
                "</h1>"
        );

        out.println(
                "<p>" +
                escapeHtml(message) +
                "</p>"
        );

        out.println(
                "<a href='" +
                escapeHtml(buttonLink) +
                "'>" +
                escapeHtml(buttonText) +
                "</a>"
        );

        out.println("""
                    </div>
                </body>
                </html>
                """);
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