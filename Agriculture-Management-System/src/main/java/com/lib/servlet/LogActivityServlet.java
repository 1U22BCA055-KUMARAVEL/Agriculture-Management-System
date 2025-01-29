package com.lib.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class LogActivityServlet
 */
@WebServlet("/logActivity")
public class LogActivityServlet extends HttpServlet {

    // Handle GET requests to fetch the activity report
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder summaryReport = new StringBuilder();

        try {
            // Database connection logic
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "globalwarn1705");

            // Query to get the total access count for each module
            String query = "SELECT module_name, SUM(access_count) AS total_accesses FROM activity_log GROUP BY module_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Generate the summary report
            while (rs.next()) {
                String moduleName = rs.getString("module_name");
                int totalAccesses = rs.getInt("total_accesses");
                summaryReport.append(moduleName)
                        .append(": ")
                        .append(totalAccesses)
                        .append(" total accesses\n");
            }

            // Set response content type to text
            response.setContentType("text/html");
            response.getWriter().write("<html><body><h1>Summary Report</h1><pre>" + summaryReport.toString() + "</pre></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("<html><body><h1>Error generating report</h1></body></html>");
        }
    }

    // Handle POST requests to log individual activity (user visiting a module)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String moduleName = request.getParameter("module_name");

        try {
            // Database connection logic
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "globalwarn1705");

            // Insert or update the activity log when the user visits a module
            String query = "INSERT INTO activity_log (username, module_name, access_time, access_count) " +
                           "VALUES (?, ?, NOW(), 1) " +
                           "ON DUPLICATE KEY UPDATE access_count = access_count + 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, moduleName);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                response.getWriter().write("Activity logged successfully");
            } else {
                response.getWriter().write("Error logging activity");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error logging activity");
        }
    }
}
