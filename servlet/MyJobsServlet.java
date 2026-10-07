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

public class MyJobsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("/HireHub/login.html");
            return;
        }

        int recruiterId =
                (Integer) session.getAttribute("userId");

        String sql =
                "SELECT * FROM jobs " +
                "WHERE recruiter_id = ?";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, recruiterId);

            ResultSet rs =
                    ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                int jobId =
                        rs.getInt("job_id");

                String title =
                        rs.getString("title");

                String company =
                        rs.getString("company");

                String location =
                        rs.getString("location");

                String salary =
                        rs.getString("salary");

                String skills =
                        rs.getString("skills");

                out.println("<div class='job-card'>");

                out.println(
                        "<div class='job-card-top'>"
                );

                out.println(
                        "<div class='company-icon'>💼</div>"
                );

                out.println("<div>");

                out.println(
                        "<h2>" + title + "</h2>"
                );

                out.println(
                        "<p>" + company + "</p>"
                );

                out.println("</div>");

                out.println("</div>");

                out.println(
                        "<div class='job-info'>"
                );

                out.println(
                        "<span>📍 "
                        + location
                        + "</span>"
                );

                out.println(
                        "<span>💰 "
                        + salary
                        + "</span>"
                );

                out.println(
                        "<span>💼 Full Time</span>"
                );

                out.println("</div>");

                out.println(
                        "<div class='job-skills'>"
                );

                if (skills != null) {

                    String[] skillList =
                            skills.split(",");

                    for (String skill : skillList) {

                        out.println(
                                "<span>"
                                + skill.trim()
                                + "</span>"
                        );
                    }
                }

                out.println("</div>");

                // RECRUITER ACTIONS

                out.println(
                        "<div style='margin-top:20px; display:flex; gap:10px; flex-wrap:wrap;'>"
                );

                // View Details
                out.println(
                        "<button onclick=\"window.location.href="
                        + "'job-details.html?jobId="
                        + jobId
                        + "'\">"
                        + "👁️ View Details"
                        + "</button>"
                );

                // Edit Job
                out.println(
                        "<button onclick=\"window.location.href="
                        + "'edit-job.html?jobId="
                        + jobId
                        + "'\">"
                        + "✏️ Edit Job"
                        + "</button>"
                );

                // Delete Job
                out.println(
                        "<form method='POST' "
                        + "action='/HireHub/deleteJob' "
                        + "style='display:inline;'>"
                );

                out.println(
                        "<input type='hidden' "
                        + "name='jobId' "
                        + "value='" + jobId + "'>"
                );

                out.println(
                        "<button type='submit'>"
                        + "🗑️ Delete Job"
                        + "</button>"
                );

                out.println("</form>");

                out.println("</div>");

                out.println("</div>");
            }

            if (!found) {

                out.println(
                        "<p style='text-align:center;'>"
                        + "You have not posted any jobs yet."
                        + "</p>"
                );
            }

        } catch (Exception e) {

            out.println(
                    "<p style='text-align:center;'>"
                    + "Unable to load your jobs."
                    + "</p>"
            );

            System.out.println(
                    "MyJobsServlet Error: "
                    + e.getMessage()
            );
        }
    }
}