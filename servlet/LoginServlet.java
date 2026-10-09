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

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {

            showError(
                    response,
                    "Login Failed!",
                    "Please enter your email, password and select your role.",
                    "/HireHub/login.html",
                    "Try Again"
            );

            return;
        }

        String sql =
                "SELECT * FROM users " +
                "WHERE email = ? " +
                "AND password = ? " +
                "AND LOWER(role) = LOWER(?)";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, email.trim());
            ps.setString(2, password);
            ps.setString(3, role.trim());

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                HttpSession session =
                        request.getSession();

                session.setAttribute(
                        "userId",
                        rs.getInt("user_id")
                );

                session.setAttribute(
                        "userName",
                        rs.getString("name")
                );

                session.setAttribute(
                        "userEmail",
                        rs.getString("email")
                );

                session.setAttribute(
                        "userRole",
                        rs.getString("role")
                );

                if (role.equalsIgnoreCase("recruiter")) {

                    response.sendRedirect(
                            "/HireHub/recruiter-dashboard.html"
                    );

                } else {

                    response.sendRedirect(
                            "/HireHub/jobseeker-dashboard.html"
                    );
                }

            } else {

                showError(
                        response,
                        "Login Failed!",
                        "Invalid email, password or role. Please check your details and try again.",
                        "/HireHub/login.html",
                        "Try Again"
                );
            }

            rs.close();

        } catch (Exception e) {

            System.out.println(
                    "LoginServlet Error: "
                    + e.getMessage()
            );

            showError(
                    response,
                    "Unable to Login!",
                    "Something went wrong while processing your login.",
                    "/HireHub/login.html",
                    "Back to Login"
            );
        }
    }

    private void showError(
            HttpServletResponse response,
            String title,
            String message,
            String buttonLink,
            String buttonText)
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

                "<title>Login Failed - HireHub</title>" +

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
                "max-width:520px;" +
                "background:white;" +
                "border-radius:22px;" +
                "padding:45px 35px;" +
                "text-align:center;" +
                "box-shadow:0 18px 45px rgba(0,0,0,0.12);" +
                "}" +

                ".error-icon{" +
                "width:85px;" +
                "height:85px;" +
                "margin:0 auto 25px;" +
                "border-radius:50%;" +
                "background:#fee2e2;" +
                "color:#dc2626;" +
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
                "margin-bottom:28px;" +
                "}" +

                ".btn{" +
                "display:inline-block;" +
                "padding:13px 26px;" +
                "border-radius:10px;" +
                "text-decoration:none;" +
                "font-weight:600;" +
                "font-size:15px;" +
                "background:#2563eb;" +
                "color:white;" +
                "}" +

                ".btn:hover{" +
                "background:#1d4ed8;" +
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

                ".btn{" +
                "width:100%;" +
                "}" +

                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                "<div class='card'>" +

                "<div class='error-icon'>!</div>" +

                "<h1>" +
                escapeHtml(title) +
                "</h1>" +

                "<p class='message'>" +
                escapeHtml(message) +
                "</p>" +

                "<a class='btn' href='" +
                buttonLink +
                "'>" +
                escapeHtml(buttonText) +
                "</a>" +

                "<div class='brand'>" +
                "Powered by <span>HireHub</span>" +
                "</div>" +

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