package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        response.setHeader(
                "Pragma",
                "no-cache"
        );

        response.setHeader(
                "Expires",
                "0"
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

        Object userIdObject =
                session.getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {

            showError(
                    response,
                    "Invalid Session!",
                    "Please login again and try updating your profile."
            );

            return;
        }

        int userId =
                (Integer) userIdObject;

        if (userId <= 0) {

            showError(
                    response,
                    "Invalid User!",
                    "Please login again and try again."
            );

            return;
        }

        String role =
                (String) session.getAttribute("userRole");

        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        if (name == null ||
                name.trim().isEmpty() ||
                email == null ||
                email.trim().isEmpty()) {

            showError(
                    response,
                    "Required Fields Missing!",
                    "Please enter both your name and email."
            );

            return;
        }

        name = name.trim();
        email = email.trim();

        if (name.length() > 100) {

            showError(
                    response,
                    "Invalid Name!",
                    "Name cannot contain more than 100 characters."
            );

            return;
        }

        if (email.length() > 100) {

            showError(
                    response,
                    "Invalid Email!",
                    "Email cannot contain more than 100 characters."
            );

            return;
        }

        if (!isValidEmail(email)) {

            showError(
                    response,
                    "Invalid Email!",
                    "Please enter a valid email address."
            );

            return;
        }

        String sql =
                "UPDATE users SET " +
                "name = ?, email = ? " +
                "WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, userId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                session.setAttribute(
                        "userName",
                        name
                );

                session.setAttribute(
                        "userEmail",
                        email
                );

                showSuccess(
                        response,
                        name,
                        role
                );

            } else {

                showError(
                        response,
                        "Profile Update Failed!",
                        "We could not update your profile. Please try again."
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "EditProfileServlet Error: "
                    + e.getMessage()
            );

            showError(
                    response,
                    "Unable to Update Profile!",
                    "Something went wrong while updating your profile. Please try again."
            );
        }
    }


    private boolean isValidEmail(String email) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }


    private void showSuccess(
            HttpServletResponse response,
            String name,
            String role)
            throws IOException {

        String dashboardLink;

        if (role != null &&
                role.equalsIgnoreCase("Recruiter")) {

            dashboardLink =
                    "/HireHub/recruiter-dashboard.html";

        } else {

            dashboardLink =
                    "/HireHub/jobseeker-dashboard.html";
        }

        response.getWriter().println(

                "<!DOCTYPE html>" +

                "<html lang='en'>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +

                "<title>Profile Updated - HireHub</title>" +

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

                ".profile-name{" +
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

                "<h1>Profile Updated Successfully!</h1>" +

                "<p class='message'>" +
                "Your personal information has been successfully " +
                "updated on HireHub." +
                "</p>" +

                "<div class='profile-name'>" +
                "👤 " +
                escapeHtml(name) +
                "</div>" +

                "<div class='buttons'>" +

                "<a class='btn primary' " +
                "href='/HireHub/profile.html'>" +
                "View Profile" +
                "</a>" +

                "<a class='btn secondary' href='" +
                dashboardLink +
                "'>" +
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
            String message)
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

                "<a href='/HireHub/edit-profile.html'>" +
                "Back to Edit Profile" +
                "</a>" +

                "</div>" +

                "</body>" +

                "</html>"
        );
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