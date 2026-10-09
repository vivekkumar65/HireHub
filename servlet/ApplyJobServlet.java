package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplyJobServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect(
                    "/HireHub/login.html"
            );

            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
            !role.equalsIgnoreCase("Job Seeker")) {

            showError(
                    response,
                    "Access Denied!",
                    "Only Job Seekers can apply for jobs.",
                    "/HireHub/jobs.html",
                    "Back to Jobs"
            );

            return;
        }

        String jobIdText =
                request.getParameter("jobId");

        if (jobIdText == null ||
            jobIdText.trim().isEmpty()) {

            showError(
                    response,
                    "Job ID Missing!",
                    "Please select a valid job before applying.",
                    "/HireHub/jobs.html",
                    "Back to Jobs"
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

            showError(
                    response,
                    "Invalid Job ID!",
                    "The selected job ID is not valid.",
                    "/HireHub/jobs.html",
                    "Back to Jobs"
            );

            return;
        }

        try (
                Connection con =
                        DatabaseConnection.getConnection()
        ) {

            // ======================================
            // CHECK IF JOB EXISTS
            // ======================================

            String jobCheckSql =
                    "SELECT job_id, title " +
                    "FROM jobs " +
                    "WHERE job_id = ?";

            String jobTitle = "";

            try (
                    PreparedStatement jobCheck =
                            con.prepareStatement(
                                    jobCheckSql
                            )
            ) {

                jobCheck.setInt(1, jobId);

                ResultSet rs =
                        jobCheck.executeQuery();

                if (!rs.next()) {

                    showError(
                            response,
                            "Job Not Found!",
                            "This job does not exist or is no longer available.",
                            "/HireHub/jobs.html",
                            "Back to Jobs"
                    );

                    return;
                }

                jobTitle =
                        rs.getString("title");
            }


            // ======================================
            // CHECK DUPLICATE APPLICATION
            // ======================================

            String checkSql =
                    "SELECT application_id, status " +
                    "FROM applications " +
                    "WHERE job_id = ? " +
                    "AND user_id = ?";

            try (
                    PreparedStatement checkPs =
                            con.prepareStatement(
                                    checkSql
                            )
            ) {

                checkPs.setInt(1, jobId);
                checkPs.setInt(2, userId);

                ResultSet rs =
                        checkPs.executeQuery();

                if (rs.next()) {

                    showError(
                            response,
                            "Already Applied!",
                            "You have already submitted an application for this job.",
                            "/HireHub/my-applications.html",
                            "View My Applications"
                    );

                    return;
                }
            }


            // ======================================
            // INSERT NEW APPLICATION
            // ======================================

            String insertSql =
                    "INSERT INTO applications " +
                    "(job_id, user_id, status) " +
                    "VALUES (?, ?, ?)";

            try (
                    PreparedStatement ps =
                            con.prepareStatement(
                                    insertSql
                            )
            ) {

                ps.setInt(1, jobId);
                ps.setInt(2, userId);
                ps.setString(3, "Applied");

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
                            "Application Failed!",
                            "We could not submit your application. Please try again.",
                            "/HireHub/jobs.html",
                            "Back to Jobs"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ApplyJobServlet Error: "
                    + e.getMessage()
            );

            showError(
                    response,
                    "Unable to Submit Application!",
                    "Something went wrong while submitting your application.",
                    "/HireHub/jobs.html",
                    "Back to Jobs"
            );
        }
    }


    // ==========================================
    // SUCCESS PAGE
    // ==========================================

    private void showSuccess(
            HttpServletResponse response,
            String jobTitle)
            throws IOException {

        response.getWriter().println(

                "<!DOCTYPE html>" +

                "<html lang='en'>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +

                "<meta http-equiv='Cache-Control' " +
                "content='no-cache, no-store, must-revalidate'>" +

                "<meta http-equiv='Pragma' content='no-cache'>" +

                "<meta http-equiv='Expires' content='0'>" +

                "<title>Application Submitted - HireHub</title>" +

                "<style>" +

                "*{" +
                "box-sizing:border-box;" +
                "margin:0;" +
                "padding:0;" +
                "}" +

                "body{" +
                "font-family:Arial,Helvetica,sans-serif;" +
                "background:linear-gradient(135deg,#eff6ff,#f8fafc);" +
                "min-height:100vh;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "padding:20px;" +
                "}" +

                ".card{" +
                "width:100%;" +
                "max-width:540px;" +
                "background:white;" +
                "border-radius:22px;" +
                "padding:45px 35px;" +
                "text-align:center;" +
                "box-shadow:0 18px 45px rgba(0,0,0,0.12);" +
                "}" +

                ".success-icon{" +
                "width:85px;" +
                "height:85px;" +
                "margin:0 auto 25px;" +
                "border-radius:50%;" +
                "background:#dcfce7;" +
                "color:#16a34a;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "font-size:40px;" +
                "font-weight:bold;" +
                "}" +

                "h1{" +
                "font-size:28px;" +
                "color:#111827;" +
                "margin-bottom:12px;" +
                "}" +

                ".message{" +
                "font-size:16px;" +
                "color:#6b7280;" +
                "line-height:1.6;" +
                "margin-bottom:25px;" +
                "}" +

                ".job-name{" +
                "background:#eff6ff;" +
                "border:1px solid #bfdbfe;" +
                "border-radius:12px;" +
                "padding:15px;" +
                "margin-bottom:28px;" +
                "color:#1e40af;" +
                "font-weight:600;" +
                "}" +

                ".buttons{" +
                "display:flex;" +
                "gap:12px;" +
                "justify-content:center;" +
                "flex-wrap:wrap;" +
                "}" +

                ".btn{" +
                "display:inline-block;" +
                "padding:13px 22px;" +
                "border-radius:10px;" +
                "text-decoration:none;" +
                "font-weight:600;" +
                "font-size:15px;" +
                "}" +

                ".primary{" +
                "background:#2563eb;" +
                "color:white;" +
                "}" +

                ".primary:hover{" +
                "background:#1d4ed8;" +
                "}" +

                ".secondary{" +
                "background:#f3f4f6;" +
                "color:#374151;" +
                "}" +

                ".secondary:hover{" +
                "background:#e5e7eb;" +
                "}" +

                ".brand{" +
                "font-size:14px;" +
                "color:#9ca3af;" +
                "margin-top:30px;" +
                "}" +

                ".brand span{" +
                "color:#2563eb;" +
                "font-weight:bold;" +
                "}" +

                "@media(max-width:500px){" +

                ".card{" +
                "padding:35px 22px;" +
                "}" +

                "h1{" +
                "font-size:24px;" +
                "}" +

                ".buttons{" +
                "flex-direction:column;" +
                "}" +

                ".btn{" +
                "width:100%;" +
                "}" +

                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                "<div class='card'>" +

                "<div class='success-icon'>✓</div>" +

                "<h1>Application Submitted!</h1>" +

                "<p class='message'>" +
                "Your application has been successfully " +
                "submitted. The recruiter can now review your application." +
                "</p>" +

                "<div class='job-name'>" +
                "💼 " +
                escapeHtml(jobTitle) +
                "</div>" +

                "<div class='buttons'>" +

                "<a class='btn primary' " +
                "href='/HireHub/my-applications.html'>" +
                "View My Applications" +
                "</a>" +

                "<a class='btn secondary' " +
                "href='/HireHub/jobs.html'>" +
                "Browse More Jobs" +
                "</a>" +

                "</div>" +

                "<div class='brand'>" +
                "Powered by <span>HireHub</span>" +
                "</div>" +

                "</div>" +

                "</body>" +

                "</html>"
        );
    }


    // ==========================================
    // ERROR PAGE
    // ==========================================

    private void showError(
            HttpServletResponse response,
            String title,
            String message,
            String buttonLink,
            String buttonText)
            throws IOException {

        response.getWriter().println(

                "<!DOCTYPE html>" +

                "<html lang='en'>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +

                "<title>HireHub</title>" +

                "<style>" +

                "*{" +
                "box-sizing:border-box;" +
                "margin:0;" +
                "padding:0;" +
                "}" +

                "body{" +
                "font-family:Arial,Helvetica,sans-serif;" +
                "background:#f8fafc;" +
                "min-height:100vh;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "padding:20px;" +
                "}" +

                ".card{" +
                "background:white;" +
                "padding:45px 35px;" +
                "border-radius:20px;" +
                "text-align:center;" +
                "max-width:500px;" +
                "width:100%;" +
                "box-shadow:0 15px 40px rgba(0,0,0,.1);" +
                "}" +

                ".icon{" +
                "width:75px;" +
                "height:75px;" +
                "border-radius:50%;" +
                "background:#fee2e2;" +
                "color:#dc2626;" +
                "font-size:40px;" +
                "font-weight:bold;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "margin:0 auto 20px;" +
                "}" +

                "h1{" +
                "color:#111827;" +
                "font-size:26px;" +
                "margin-bottom:12px;" +
                "}" +

                "p{" +
                "color:#6b7280;" +
                "line-height:1.6;" +
                "margin-bottom:25px;" +
                "}" +

                "a{" +
                "display:inline-block;" +
                "padding:13px 22px;" +
                "background:#2563eb;" +
                "color:white;" +
                "text-decoration:none;" +
                "border-radius:10px;" +
                "font-weight:bold;" +
                "}" +

                "a:hover{" +
                "background:#1d4ed8;" +
                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                "<div class='card'>" +

                "<div class='icon'>!</div>" +

                "<h1>" +
                escapeHtml(title) +
                "</h1>" +

                "<p>" +
                escapeHtml(message) +
                "</p>" +

                "<a href='" +
                buttonLink +
                "'>" +
                escapeHtml(buttonText) +
                "</a>" +

                "</div>" +

                "</body>" +

                "</html>"
        );
    }


    // ==========================================
    // HTML ESCAPE
    // ==========================================

    private String escapeHtml(
            String value) {

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