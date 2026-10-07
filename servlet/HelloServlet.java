package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>HireHub Servlet</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>Hello from HireHub!</h1>");

        out.println(
            "<p>Java Servlet is working successfully.</p>"
        );

        out.println("</body>");
        out.println("</html>");
    }
}