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

public class DeleteJobServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        response.setHeader(
                "Pragma",
                "no-cache"
        );

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    "/HireHub/login.html"
            );

            return;
        }

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
                !role.equalsIgnoreCase("Recruiter")) {

            showError(
                    response,
                    "Access Denied!",
                    "Only recruiters can delete jobs."
            );

            return;
        }

        Object userIdObject =
                session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {

            response.sendRedirect(
                    "/HireHub/login.html"
            );

            return;
        }

        int recruiterId =
                (Integer) userIdObject;

        String jobIdParameter =
                request.getParameter("jobId");

        if (jobIdParameter == null ||
                jobIdParameter.trim().isEmpty()) {

            showError(
                    response,
                    "Job ID Missing!",
                    "Please select a valid job to delete."
            );

            return;
        }

        int jobId;

        try {

            jobId =
                    Integer.parseInt(
                            jobIdParameter.trim()
                    );

        } catch (NumberFormatException e) {

            showError(
                    response,
                    "Invalid Job ID!",
                    "The selected job ID is not valid."
            );

            return;
        }

        if (jobId <= 0) {

            showError(
                    response,
                    "Invalid Job ID!",
                    "The selected job ID is not valid."
            );

            return;
        }

        String jobTitle;

        try (
                Connection con =
                        DatabaseConnection.getConnection()
        ) {

            String selectSql =
                    "SELECT title FROM jobs " +
                    "WHERE job_id = ? " +
                    "AND recruiter_id = ?";

            try (
                    PreparedStatement selectPs =
                            con.prepareStatement(selectSql)
            ) {

                selectPs.setInt(1, jobId);
                selectPs.setInt(2, recruiterId);

                try (
                        ResultSet rs =
                                selectPs.executeQuery()
                ) {

                    if (rs.next()) {

                        jobTitle =
                                rs.getString("title");

                    } else {

                        showError(
                                response,
                                "Job Not Found!",
                                "This job does not exist or does not belong to your account."
                        );

                        return;
                    }
                }
            }

            String deleteSql =
                    "DELETE FROM jobs " +
                    "WHERE job_id = ? " +
                    "AND recruiter_id = ?";

            try (
                    PreparedStatement ps =
                            con.prepareStatement(deleteSql)
            ) {

                ps.setInt(1, jobId);
                ps.setInt(2, recruiterId);

                int rows =
                        ps.executeUpdate();

                if (rows > 0) {

                    showSuccess(
                            response,
                            jobTitle
                    );

                } else {

                    showError(
                            response,
                            "Unable to Delete Job!",
                            "The job could not be removed."
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "DeleteJobServlet Error: " +
                    e.getMessage()
            );

            showError(
                    response,
                    "Unable to Delete Job!",
                    "Something went wrong while deleting the job."
            );
        }
    }

    private void showSuccess(
            HttpServletResponse response,
            String jobTitle)
            throws IOException {

        PrintWriter out =
                response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="en">

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Job Deleted - HireHub</title>

                    <style>

                        * {
                            box-sizing: border-box;
                            margin: 0;
                            padding: 0;
                        }

                        body {
                            font-family: Arial, Helvetica, sans-serif;
                            background: linear-gradient(
                                135deg,
                                #fff1f2,
                                #f8fafc
                            );
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 20px;
                        }

                        .card {
                            width: 100%;
                            max-width: 520px;
                            background: white;
                            border-radius: 22px;
                            padding: 45px 35px;
                            text-align: center;
                            box-shadow:
                                0 18px 45px
                                rgba(0,0,0,0.12);
                        }

                        .delete-icon {
                            width: 85px;
                            height: 85px;
                            margin: 0 auto 25px;
                            border-radius: 50%;
                            background: #fee2e2;
                            color: #dc2626;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 40px;
                        }

                        h1 {
                            font-size: 28px;
                            color: #111827;
                            margin-bottom: 12px;
                        }

                        .message {
                            font-size: 16px;
                            color: #6b7280;
                            line-height: 1.6;
                            margin-bottom: 25px;
                        }

                        .job-name {
                            background: #fef2f2;
                            border: 1px solid #fecaca;
                            border-radius: 12px;
                            padding: 15px;
                            margin-bottom: 28px;
                            color: #991b1b;
                            font-weight: 600;
                        }

                        .buttons {
                            display: flex;
                            gap: 12px;
                            justify-content: center;
                            flex-wrap: wrap;
                        }

                        .btn {
                            display: inline-block;
                            padding: 13px 22px;
                            border-radius: 10px;
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 15px;
                        }

                        .primary {
                            background: #2563eb;
                            color: white;
                        }

                        .secondary {
                            background: #f3f4f6;
                            color: #374151;
                        }

                        .primary:hover {
                            background: #1d4ed8;
                        }

                        .brand {
                            font-size: 14px;
                            color: #9ca3af;
                            margin-top: 30px;
                        }

                        .brand span {
                            color: #2563eb;
                            font-weight: bold;
                        }

                        @media (max-width: 500px) {

                            .card {
                                padding: 35px 22px;
                            }

                            h1 {
                                font-size: 24px;
                            }

                            .buttons {
                                flex-direction: column;
                            }

                            .btn {
                                width: 100%;
                            }
                        }

                    </style>

                </head>

                <body>

                    <div class="card">

                        <div class="delete-icon">
                            🗑️
                        </div>

                        <h1>
                            Job Deleted Successfully!
                        </h1>

                        <p class="message">
                            The job posting has been permanently
                            removed from your job listings.
                        </p>
                """);

        out.println(
                "<div class='job-name'>💼 " +
                escapeHtml(jobTitle) +
                "</div>"
        );

        out.println("""
                        <div class="buttons">

                            <a class="btn primary"
                               href="/HireHub/my-jobs.html">
                                Back to My Jobs
                            </a>

                            <a class="btn secondary"
                               href="/HireHub/recruiter-dashboard.html">
                                Dashboard
                            </a>

                        </div>

                        <div class="brand">
                            Powered by <span>HireHub</span>
                        </div>

                    </div>

                </body>

                </html>
                """);
    }

    private void showError(
            HttpServletResponse response,
            String title,
            String message)
            throws IOException {

        PrintWriter out =
                response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="en">

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>HireHub</title>

                    <style>

                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            font-family: Arial, Helvetica, sans-serif;
                            background: #f8fafc;
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 20px;
                        }

                        .card {
                            background: white;
                            padding: 45px;
                            border-radius: 20px;
                            text-align: center;
                            max-width: 500px;
                            width: 100%;
                            box-shadow:
                                0 15px 40px
                                rgba(0,0,0,.1);
                        }

                        .icon {
                            font-size: 55px;
                            margin-bottom: 20px;
                        }

                        h1 {
                            color: #dc2626;
                            margin-bottom: 12px;
                        }

                        p {
                            color: #6b7280;
                            line-height: 1.6;
                            margin-bottom: 25px;
                        }

                        a {
                            display: inline-block;
                            padding: 13px 22px;
                            background: #2563eb;
                            color: white;
                            text-decoration: none;
                            border-radius: 10px;
                            font-weight: bold;
                        }

                        a:hover {
                            background: #1d4ed8;
                        }

                    </style>

                </head>

                <body>

                    <div class="card">

                        <div class="icon">
                            ⚠️
                        </div>
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

        out.println("""
                        <a href="/HireHub/my-jobs.html">
                            Back to My Jobs
                        </a>

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