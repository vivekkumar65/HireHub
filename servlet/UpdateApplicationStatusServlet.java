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

public class UpdateApplicationStatusServlet extends HttpServlet {

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

            showError(
                    response,
                    "Login Required",
                    "Please login before updating an application.",
                    "/HireHub/login.html",
                    "Go to Login"
            );

            return;
        }

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
                !role.equalsIgnoreCase("Recruiter")) {

            showError(
                    response,
                    "Access Denied",
                    "Only recruiters can update application status.",
                    "/HireHub/recruiter-dashboard.html",
                    "Back to Dashboard"
            );

            return;
        }

        Object userIdObject =
                session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {

            showError(
                    response,
                    "Session Error",
                    "Your session is no longer valid. Please login again.",
                    "/HireHub/login.html",
                    "Go to Login"
            );

            return;
        }

        int recruiterId =
                (Integer) userIdObject;

        String applicationIdText =
                request.getParameter("applicationId");

        String status =
                request.getParameter("status");

        if (applicationIdText == null ||
                applicationIdText.trim().isEmpty()) {

            showError(
                    response,
                    "Invalid Request",
                    "Application ID is missing.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );

            return;
        }

        if (status == null ||
                status.trim().isEmpty()) {

            showError(
                    response,
                    "Invalid Request",
                    "Application status is missing.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );

            return;
        }

        status = status.trim();

        int applicationId;

        try {

            applicationId =
                    Integer.parseInt(
                            applicationIdText.trim()
                    );

        } catch (NumberFormatException e) {

            showError(
                    response,
                    "Invalid Application ID",
                    "The application ID is not valid.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );

            return;
        }

        if (applicationId <= 0) {

            showError(
                    response,
                    "Invalid Application ID",
                    "The application ID must be a valid positive number.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );

            return;
        }

        if (!status.equals("Applied") &&
                !status.equals("Shortlisted") &&
                !status.equals("Selected") &&
                !status.equals("Rejected")) {

            showError(
                    response,
                    "Invalid Status",
                    "The selected application status is not valid.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );

            return;
        }

        String sql =
                "UPDATE applications a " +
                "JOIN jobs j ON a.job_id = j.job_id " +
                "SET a.status = ? " +
                "WHERE a.application_id = ? " +
                "AND j.recruiter_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, applicationId);
            ps.setInt(3, recruiterId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                showSuccess(
                        response,
                        status
                );

            } else {

                showError(
                        response,
                        "Application Not Found",
                        "This application does not exist or does not belong to one of your jobs.",
                        "/HireHub/view-applicants.html",
                        "Back to Applicants"
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "UpdateApplicationStatusServlet Error: " +
                    e.getMessage()
            );

            showError(
                    response,
                    "Update Failed",
                    "Something went wrong while updating the application status.",
                    "/HireHub/view-applicants.html",
                    "Back to Applicants"
            );
        }
    }

    private void showSuccess(
            HttpServletResponse response,
            String status)
            throws IOException {

        PrintWriter out =
                response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Status Updated - HireHub</title>

                    <style>
                        * {
                            box-sizing: border-box;
                            margin: 0;
                            padding: 0;
                            font-family: Arial, sans-serif;
                        }

                        body {
                            min-height: 100vh;
                            background: #f5f7fb;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }

                        .card {
                            width: 90%;
                            max-width: 520px;
                            background: white;
                            padding: 45px 35px;
                            border-radius: 18px;
                            text-align: center;
                            box-shadow: 0 12px 35px rgba(0,0,0,0.08);
                        }

                        .logo {
                            font-size: 26px;
                            font-weight: bold;
                            color: #2563eb;
                            margin-bottom: 25px;
                        }

                        .success-icon {
                            width: 75px;
                            height: 75px;
                            margin: 0 auto 20px;
                            border-radius: 50%;
                            background: #dcfce7;
                            color: #16a34a;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 38px;
                        }

                        h1 {
                            color: #111827;
                            margin-bottom: 12px;
                        }

                        p {
                            color: #6b7280;
                            line-height: 1.6;
                            margin-bottom: 12px;
                        }

                        .status {
                            display: inline-block;
                            margin: 10px 0 25px;
                            padding: 9px 16px;
                            border-radius: 20px;
                            background: #eff6ff;
                            color: #2563eb;
                            font-weight: bold;
                        }

                        .buttons {
                            display: flex;
                            justify-content: center;
                            gap: 10px;
                            flex-wrap: wrap;
                        }

                        a {
                            text-decoration: none;
                            padding: 12px 20px;
                            border-radius: 8px;
                            font-weight: bold;
                        }

                        .primary {
                            background: #2563eb;
                            color: white;
                        }

                        .secondary {
                            background: #e5e7eb;
                            color: #374151;
                        }

                        .primary:hover {
                            background: #1d4ed8;
                        }
                    </style>
                </head>

                <body>

                    <div class="card">

                        <div class="logo">
                            HireHub
                        </div>

                        <div class="success-icon">
                            ✓
                        </div>

                        <h1>Status Updated</h1>

                        <p>
                            The applicant's application status
                            has been updated successfully.
                        </p>
                """);

        out.println(
                "<div class='status'>" +
                escapeHtml(status) +
                "</div>"
        );

        out.println("""
                        <div class="buttons">

                            <a class="primary"
                               href="/HireHub/view-applicants.html">
                                View Applicants
                            </a>

                            <a class="secondary"
                               href="/HireHub/recruiter-dashboard.html">
                                Dashboard
                            </a>

                        </div>

                    </div>

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

        PrintWriter out =
                response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>HireHub</title>

                    <style>
                        * {
                            box-sizing: border-box;
                            font-family: Arial, sans-serif;
                        }

                        body {
                            margin: 0;
                            min-height: 100vh;
                            background: #f5f7fb;
                            display: flex;
                            align-items: center;
                            justify-content: center;
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

                        .logo {
                            font-size: 26px;
                            font-weight: bold;
                            color: #2563eb;
                            margin-bottom: 25px;
                        }

                        .icon {
                            font-size: 50px;
                            margin-bottom: 15px;
                        }

                        h1 {
                            color: #111827;
                            margin-bottom: 12px;
                        }

                        p {
                            color: #6b7280;
                            line-height: 1.6;
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

                        a:hover {
                            background: #1d4ed8;
                        }
                    </style>
                </head>

                <body>

                    <div class="card">

                        <div class="logo">
                            HireHub
                        </div>

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