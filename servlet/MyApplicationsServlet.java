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

public class MyApplicationsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            out.println("[]");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");


        String sql =
                "SELECT " +
                "a.application_id, " +
                "a.job_id, " +
                "j.title, " +
                "j.company, " +
                "j.location, " +
                "a.status " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.job_id " +
                "WHERE a.user_id = ? " +
                "ORDER BY a.application_id DESC";


        try (
            Connection con =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs =
                    ps.executeQuery();


            out.println("[");

            boolean first = true;


            while (rs.next()) {

                if (!first) {
                    out.println(",");
                }

                first = false;


                int applicationId =
                        rs.getInt("application_id");


                int jobId =
                        rs.getInt("job_id");


                String title =
                        rs.getString("title");


                String company =
                        rs.getString("company");


                String location =
                        rs.getString("location");


                String status =
                        rs.getString("status");


                out.println("{");


                out.println(
                    "\"applicationId\":" +
                    applicationId + ","
                );


                out.println(
                    "\"jobId\":" +
                    jobId + ","
                );


                out.println(
                    "\"title\":\"" +
                    escapeJson(title) +
                    "\","
                );


                out.println(
                    "\"company\":\"" +
                    escapeJson(company) +
                    "\","
                );


                out.println(
                    "\"location\":\"" +
                    escapeJson(location) +
                    "\","
                );


                out.println(
                    "\"status\":\"" +
                    escapeJson(status) +
                    "\""
                );


                out.println("}");
            }


            out.println("]");


        } catch (Exception e) {

            System.out.println(
                "MyApplicationsServlet Error: "
                + e.getMessage()
            );

            out.println("[]");
        }
    }


    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}