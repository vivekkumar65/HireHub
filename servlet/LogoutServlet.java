package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Current session
        HttpSession session =
                request.getSession(false);

        // Session destroy
        if (session != null) {
            session.invalidate();
        }

        // Login page par redirect
        response.sendRedirect(
                "/HireHub/login.html"
        );
    }
}