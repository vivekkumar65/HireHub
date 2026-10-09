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

public class ChangePasswordServlet extends HttpServlet {

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

        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");

        if (currentPassword == null ||
            newPassword == null ||
            confirmPassword == null ||
            currentPassword.isEmpty() ||
            newPassword.isEmpty() ||
            confirmPassword.isEmpty()) {

            showMessage(
                    response,
                    "All Fields Are Required",
                    "Please fill in all password fields.",
                    "Back to Change Password",
                    "/HireHub/change-password.html",
                    false
            );

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            showMessage(
                    response,
                    "Passwords Do Not Match",
                    "Please enter the same password in both fields.",
                    "Back to Change Password",
                    "/HireHub/change-password.html",
                    false
            );

            return;
        }

        String checkSql =
                "SELECT password FROM users " +
                "WHERE user_id = ?";

        String updateSql =
                "UPDATE users SET password = ? " +
                "WHERE user_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement checkPs =
                        con.prepareStatement(checkSql)
        ) {

            checkPs.setInt(1, userId);

            ResultSet rs =
                    checkPs.executeQuery();

            if (!rs.next()) {

                showMessage(
                        response,
                        "User Not Found",
                        "We could not find your account.",
                        "Back to Profile",
                        "/HireHub/profile.html",
                        false
                );

                return;
            }

            String databasePassword =
                    rs.getString("password");

            if (!databasePassword.equals(currentPassword)) {

                showMessage(
                        response,
                        "Current Password Is Incorrect",
                        "Please enter your current password correctly.",
                        "Try Again",
                        "/HireHub/change-password.html",
                        false
                );

                return;
            }

            try (
                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql)
            ) {

                updatePs.setString(1, newPassword);
                updatePs.setInt(2, userId);

                int rows =
                        updatePs.executeUpdate();

                if (rows > 0) {

                    showMessage(
                            response,
                            "Password Changed Successfully!",
                            "Your password has been updated successfully.",
                            "Go to Profile",
                            "/HireHub/profile.html",
                            true
                    );

                } else {

                    showMessage(
                            response,
                            "Password Change Failed",
                            "We could not update your password. Please try again.",
                            "Back to Change Password",
                            "/HireHub/change-password.html",
                            false
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ChangePasswordServlet Error: "
                    + e.getMessage()
            );

            showMessage(
                    response,
                    "Unable to Change Password",
                    "Something went wrong while updating your password.",
                    "Back to Profile",
                    "/HireHub/profile.html",
                    false
            );
        }
    }

    private void showMessage(
            HttpServletResponse response,
            String title,
            String message,
            String buttonText,
            String buttonLink,
            boolean success)
            throws IOException {

        String icon = success ? "✓" : "!";

        response.getWriter().println(
                "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
                + "<title>" + title + " | HireHub</title>"

                + "<style>"

                + "* {"
                + "box-sizing: border-box;"
                + "margin: 0;"
                + "padding: 0;"
                + "}"

                + "body {"
                + "font-family: Arial, sans-serif;"
                + "background: #f4f7fb;"
                + "min-height: 100vh;"
                + "display: flex;"
                + "flex-direction: column;"
                + "}"

                + ".navbar {"
                + "height: 70px;"
                + "background: #ffffff;"
                + "border-bottom: 1px solid #e5e7eb;"
                + "display: flex;"
                + "align-items: center;"
                + "justify-content: space-between;"
                + "padding: 0 7%;"
                + "}"

                + ".logo {"
                + "font-size: 25px;"
                + "font-weight: bold;"
                + "color: #2563eb;"
                + "}"

                + ".logo span {"
                + "color: #111827;"
                + "}"

                + ".nav-link {"
                + "text-decoration: none;"
                + "color: #374151;"
                + "font-size: 14px;"
                + "font-weight: 600;"
                + "}"

                + ".container {"
                + "flex: 1;"
                + "display: flex;"
                + "align-items: center;"
                + "justify-content: center;"
                + "padding: 50px 20px;"
                + "}"

                + ".card {"
                + "width: 100%;"
                + "max-width: 520px;"
                + "background: #ffffff;"
                + "border-radius: 18px;"
                + "padding: 45px 40px;"
                + "text-align: center;"
                + "box-shadow: 0 15px 40px rgba(0,0,0,0.08);"
                + "}"

                + ".icon {"
                + "width: 76px;"
                + "height: 76px;"
                + "border-radius: 50%;"
                + "background: "
                + (success ? "#dcfce7" : "#fee2e2")
                + ";"
                + "color: "
                + (success ? "#16a34a" : "#dc2626")
                + ";"
                + "font-size: 42px;"
                + "font-weight: bold;"
                + "display: flex;"
                + "align-items: center;"
                + "justify-content: center;"
                + "margin: 0 auto 25px;"
                + "}"

                + "h1 {"
                + "font-size: 27px;"
                + "color: #111827;"
                + "margin-bottom: 14px;"
                + "}"

                + ".message {"
                + "font-size: 16px;"
                + "line-height: 1.6;"
                + "color: #6b7280;"
                + "margin-bottom: 30px;"
                + "}"

                + ".button {"
                + "display: inline-block;"
                + "padding: 13px 25px;"
                + "background: #2563eb;"
                + "color: #ffffff;"
                + "text-decoration: none;"
                + "border-radius: 9px;"
                + "font-size: 15px;"
                + "font-weight: bold;"
                + "transition: 0.2s;"
                + "}"

                + ".button:hover {"
                + "background: #1d4ed8;"
                + "}"

                + ".footer {"
                + "text-align: center;"
                + "padding: 20px;"
                + "color: #9ca3af;"
                + "font-size: 13px;"
                + "}"

                + "@media (max-width: 600px) {"
                + ".navbar {"
                + "padding: 0 20px;"
                + "}"
                + ".card {"
                + "padding: 35px 25px;"
                + "}"
                + "h1 {"
                + "font-size: 23px;"
                + "}"
                + "}"

                + "</style>"
                + "</head>"

                + "<body>"

                + "<nav class='navbar'>"
                + "<div class='logo'>Hire<span>Hub</span></div>"
                + "<a class='nav-link' "
                + "href='/HireHub/profile.html'>"
                + "My Profile"
                + "</a>"
                + "</nav>"

                + "<main class='container'>"

                + "<div class='card'>"

                + "<div class='icon'>"
                + icon
                + "</div>"

                + "<h1>"
                + title
                + "</h1>"

                + "<p class='message'>"
                + message
                + "</p>"

                + "<a class='button' href='"
                + buttonLink
                + "'>"
                + buttonText
                + "</a>"

                + "</div>"

                + "</main>"

                + "<footer class='footer'>"
                + "© 2026 HireHub. All rights reserved."
                + "</footer>"

                + "</body>"
                + "</html>"
        );
    }
}