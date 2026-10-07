package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class JobsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String sql = "SELECT * FROM jobs";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int jobId = rs.getInt("job_id");

                String title = rs.getString("title");
                String company = rs.getString("company");
                String location = rs.getString("location");
                String salary = rs.getString("salary");
                String skills = rs.getString("skills");

                out.println("<div class='job-card'>");

                out.println("<div class='job-card-top'>");

                out.println("<div class='company-icon'>");
                out.println("💼");
                out.println("</div>");

                out.println("<div>");

                out.println("<h2>" + title + "</h2>");

                out.println("<p>" + company + "</p>");

                out.println("</div>");

                out.println("</div>");

                out.println("<div class='job-info'>");

                out.println("<span>📍 " + location + "</span>");

                out.println("<span>💰 " + salary + "</span>");

                out.println("<span>💼 Full Time</span>");

                out.println("</div>");

                out.println("<div class='job-skills'>");

                ArrayList<String> skillList =
                        new ArrayList<>();

                if (skills != null && !skills.trim().isEmpty()) {

                    String[] skillsArray =
                            skills.split(",");

                    for (String skill : skillsArray) {

                        skillList.add(skill.trim());
                    }
                }

                for (String skill : skillList) {

                    out.println(
                            "<span>"
                            + skill
                            + "</span>"
                    );
                }

                out.println("</div>");

                out.println(
                        "<button class='view-details-btn' " +
                        "onclick=\"window.location.href=" +
                        "'job-details.html?jobId="
                        + jobId
                        + "'\">"
                        + "View Details"
                        + "</button>"
                );

                out.println("</div>");
            }

        } catch (Exception e) {

            out.println(
                    "<p style='text-align:center;'>"
                    + "Unable to load jobs."
                    + "</p>"
            );

            System.out.println(
                    "JobsServlet Error: "
                    + e.getMessage()
            );
        }
    }
}