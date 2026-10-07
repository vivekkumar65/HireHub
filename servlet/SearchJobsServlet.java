package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

public class SearchJobsServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String keyword = request.getParameter("keyword");

        response.setContentType("text/html");

        String sql = "SELECT * FROM jobs WHERE title LIKE ? OR company LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            var out = response.getWriter();

            out.println("<h1>Search Results</h1>");

            while (rs.next()) {
                out.println("<h2>" + rs.getString("title") + "</h2>");
                out.println("<p>" + rs.getString("company") + "</p>");
                out.println("<hr>");
            }

        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}