package com.lib.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.catalina.mbeans.ClassNameMBean;
import org.json.JSONException;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CropManagementServlet")
public class CropManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/agriculture_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "globalwarn1705";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String action = request.getParameter("action");
        if (action == null) {
            try {
				jsonResponse.put("error", "No action specified.");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            out.write(jsonResponse.toString());
            return;
        }

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if ("getPeriods".equals(action)) {
                String crop = request.getParameter("crop");
                if (crop != null && !crop.isEmpty()) {
                    String query = "SELECT total_period, growth_period, productivity_period FROM CropPeriods WHERE crop_name = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, crop);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                jsonResponse.put("totalPeriod", rs.getInt("total_period"));
                                jsonResponse.put("growthPeriod", rs.getInt("growth_period"));
                                jsonResponse.put("productivityPeriod", rs.getInt("productivity_period"));
                            } else {
                                jsonResponse.put("error", "No data found for the selected crop.");
                            }
                        }
                    }
                } else {
                    jsonResponse.put("error", "Invalid crop name.");
                }
            } else {
                jsonResponse.put("error", "Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
				jsonResponse.put("error", "Database connection error: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        out.write(jsonResponse.toString());
    }
}
