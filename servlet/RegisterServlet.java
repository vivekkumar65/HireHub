package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        String role =
                request.getParameter("role");

        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {

            showError(
                    response,
                    "Registration Failed!",
                    "Please fill in all required fields.",
                    "/HireHub/register.html",
                    "Back to Register"
            );

            return;
        }

        String sql =
                "INSERT INTO users " +
                "(name, email, password, role) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, name.trim());
            ps.setString(2, email.trim());
            ps.setString(3, password);
            ps.setString(4, role.trim());

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                showSuccess(
                        response,
                        name.trim()
                );

            } else {

                showError(
                        response,
                        "Registration Failed!",
                        "Your account could not be created. Please try again.",
                        "/HireHub/register.html",
                        "Back to Register"
                );
            }

        } catch (SQLException e) {

            if (e.getMessage() != null &&
                e.getMessage().toLowerCase().contains("duplicate")) {

                showError(
                        response,
                        "Email Already Registered!",
                        "An account with this email already exists. Please use another email or login.",
                        "/HireHub/register.html",
                        "Back to Register"
                );

            } else {

                System.out.println(
                        "RegisterServlet SQL Error: "
                        + e.getMessage()
                );

                showError(
                        response,
                        "Registration Failed!",
                        "Something went wrong while creating your account.",
                        "/HireHub/register.html",
                        "Back to Register"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "RegisterServlet Error: "
                    + e.getMessage()
            );

            showError(
                    response,
                    "Registration Failed!",
                    "Something went wrong while creating your account.",
                    "/HireHub/register.html",
                    "Back to Register"
            );
        }
    }

    private void showSuccess(
            HttpServletResponse response,
            String name)
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

                "<title>Registration Successful - HireHub</title>" +

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

                ".user-name{" +
                "background:#eff6ff;" +
                "border:1px solid #bfdbfe;" +
                "border-radius:12px;" +
                "padding:15px;" +
                "margin-bottom:28px;" +
                "color:#1e40af;" +
                "font-weight:600;" +
                "}" +

                ".btn{" +
                "display:inline-block;" +
                "padding:13px 25px;" +
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

                "<div class='success-icon'>✓</div>" +

                "<h1>Registration Successful!</h1>" +

                "<p class='message'>" +
                "Your HireHub account has been created successfully." +
                "</p>" +

                "<div class='user-name'>" +
                "Welcome, " +
                escapeHtml(name) +
                "!" +
                "</div>" +

                "<a class='btn' " +
                "href='/HireHub/login.html'>" +
                "Continue to Login" +
                "</a>" +

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