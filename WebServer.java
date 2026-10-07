import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WebServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        server.createContext("/", WebServer::handleRequest);

        server.setExecutor(null);

        System.out.println("=================================");
        System.out.println("Online Job Portal Server Started!");
        System.out.println("Open: http://localhost:8080");
        System.out.println("=================================");

        server.start();
    }

    public static void handleRequest(HttpExchange exchange)
            throws IOException {

        String urlPath = URLDecoder.decode(
                exchange.getRequestURI().getPath(),
                StandardCharsets.UTF_8
        );

        // Login API
        if (urlPath.equals("/api/login")) {
            handleLogin(exchange);
            return;
        }

        // Home page
        if (urlPath.equals("/")) {
            urlPath = "/index.html";
        }

        Path filePath = Path.of(
                "frontend" + urlPath
        ).normalize();

        Path frontendPath = Path.of(
                "frontend"
        ).toAbsolutePath().normalize();

        if (!filePath.toAbsolutePath().normalize()
                .startsWith(frontendPath)) {

            sendResponse(exchange, 403, "Access Denied");
            return;
        }

        if (!Files.exists(filePath)
                || Files.isDirectory(filePath)) {

            sendResponse(exchange, 404, "Page Not Found");
            return;
        }

        byte[] fileData = Files.readAllBytes(filePath);

        String contentType = getContentType(
                filePath.toString()
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                contentType
        );

        exchange.sendResponseHeaders(
                200,
                fileData.length
        );

        OutputStream output = exchange.getResponseBody();

        output.write(fileData);
        output.close();
    }

    public static void handleLogin(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Only POST method is allowed"
            );

            return;
        }

        String requestData = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        String email = getValue(requestData, "email");
        String password = getValue(requestData, "password");
        String role = getValue(requestData, "role");

        String sql = "SELECT * FROM users " +
                     "WHERE email = ? AND password = ? " +
                     "AND LOWER(role) = LOWER(?)";

        try {

            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String response = "{"
                        + "\"success\":true,"
                        + "\"message\":\"Login Successful\","
                        + "\"name\":\""
                        + rs.getString("name")
                        + "\","
                        + "\"role\":\""
                        + rs.getString("role")
                        + "\""
                        + "}";

                sendJsonResponse(exchange, 200, response);

            } else {

                String response = "{"
                        + "\"success\":false,"
                        + "\"message\":\"Invalid Email, Password or Role\""
                        + "}";

                sendJsonResponse(exchange, 401, response);
            }

            con.close();

        } catch (Exception e) {

            String response = "{"
                    + "\"success\":false,"
                    + "\"message\":\"Database connection failed\""
                    + "}";

            sendJsonResponse(exchange, 500, response);

            System.out.println(e.getMessage());
        }
    }

    public static String getValue(
            String data,
            String key) {

        String search = "\"" + key + "\":\"";

        int start = data.indexOf(search);

        if (start == -1) {
            return "";
        }

        start = start + search.length();

        int end = data.indexOf("\"", start);

        if (end == -1) {
            return "";
        }

        return data.substring(start, end);
    }

    public static void sendJsonResponse(
            HttpExchange exchange,
            int statusCode,
            String message) throws IOException {

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        sendResponse(exchange, statusCode, message);
    }

    public static String getContentType(String fileName) {

        if (fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".css")) {
            return "text/css";
        }

        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        if (fileName.endsWith(".png")) {
            return "image/png";
        }

        if (fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")) {

            return "image/jpeg";
        }

        return "text/plain";
    }

    public static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String message) throws IOException {

        byte[] response = message.getBytes(
                StandardCharsets.UTF_8
        );

        exchange.sendResponseHeaders(
                statusCode,
                response.length
        );

        OutputStream output = exchange.getResponseBody();

        output.write(response);
        output.close();
    }
}