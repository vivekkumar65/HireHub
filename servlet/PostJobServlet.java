package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PostJobServlet extends HttpServlet {

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

        String role =
                (String) session.getAttribute("userRole");

        if (role == null ||
            !role.equalsIgnoreCase("Recruiter")) {

            showError(
                    response,
                    "Access Denied!",
                    "Only recruiters can post jobs.",
                    "/HireHub/recruiter-dashboard.html",
                    "Back to Dashboard"
            );

            return;
        }

        int recruiterId =
                (Integer) session.getAttribute("userId");

        String title =
                request.getParameter("title");

        String company =
                request.getParameter("company");

        String location =
                request.getParameter("location");

        String salary =
                request.getParameter("salary");

        String skills =
                request.getParameter("skills");

        String description =
                request.getParameter("description");

        if (title == null || title.trim().isEmpty() ||
            company == null || company.trim().isEmpty() ||
            location == null || location.trim().isEmpty() ||
            salary == null || salary.trim().isEmpty() ||
            skills == null || skills.trim().isEmpty() ||
            description == null || description.trim().isEmpty()) {

            showError(
                    response,
                    "Missing Information!",
                    "Please fill in all job details before posting.",
                    "/HireHub/post-job.html",
                    "Back to Post Job"
            );

            return;
        }

        title = title.trim();
        company = company.trim();
        location = location.trim();
        salary = salary.trim();
        skills = skills.trim();
        description = description.trim();

        String sql =
                "INSERT INTO jobs " +
                "(title, company, location, salary, " +
                "skills, description, recruiter_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, title);
            ps.setString(2, company);
            ps.setString(3, location);
            ps.setString(4, salary);
            ps.setString(5, skills);
            ps.setString(6, description);
            ps.setInt(7, recruiterId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                showSuccess(
                        response,
                        title
                );

            } else {

                showError(
                        response,
                        "Unable to Post Job!",
                        "The job could not be added. Please try again.",
                        "/HireHub/post-job.html",
                        "Back to Post Job"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "PostJobServlet Error: " +
                    e.getMessage()
            );

            showError(
                    response,
                    "Unable to Post Job!",
                    "Something went wrong while posting the job.",
                    "/HireHub/post-job.html",
                    "Back to Post Job"
            );
        }
    }

    private void showSuccess(
            HttpServletResponse response,
            String title)
            throws IOException {

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );

        response.setHeader(
                "Pragma",
                "no-cache"
        );

        response.setDateHeader(
                "Expires",
                0
        );

        response.getWriter().println(

                "<!DOCTYPE html>" +

                "<html lang='en'>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +

                "<title>Job Posted - HireHub</title>" +

                "<style>" +

                "*{" +
                "box-sizing:border-box;" +
                "margin:0;" +
                "padding:0;" +
                "}" +

                "body{" +
                "font-family:Arial,Helvetica,sans-serif;" +
                "background:linear-gradient(135deg,#eef2ff,#f8fafc);" +
                "min-height:100vh;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "padding:20px;" +
                "}" +

                ".card{" +
                "width:100%;" +
                "max-width:520px;" +
                "background:white;" +
                "border-radius:20px;" +
                "padding:45px 35px;" +
                "text-align:center;" +
                "box-shadow:0 15px 40px rgba(0,0,0,0.12);" +
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
                "font-size:42px;" +
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
                "margin-bottom:30px;" +
                "}" +

                ".job-name{" +
                "background:#f3f4f6;" +
                "border-radius:12px;" +
                "padding:14px;" +
                "margin-bottom:25px;" +
                "color:#374151;" +
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

                "<h1>Job Posted Successfully!</h1>" +

                "<p class='message'>" +
                "Your job has been successfully published " +
                "and is now available on HireHub." +
                "</p>" +

                "<div class='job-name'>" +
                "💼 " +
                escapeHtml(title) +
                "</div>" +

                "<div class='buttons'>" +

                "<a class='btn primary' " +
                "href='/HireHub/jobs.html'>" +
                "View Jobs" +
                "</a>" +

                "<a class='btn secondary' " +
                "href='/HireHub/recruiter-dashboard.html'>" +
                "Dashboard" +
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