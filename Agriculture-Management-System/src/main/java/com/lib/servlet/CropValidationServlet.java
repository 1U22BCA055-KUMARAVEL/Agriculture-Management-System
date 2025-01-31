package com.lib.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CropValidationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String cropName = request.getParameter("cropName");
        JSONObject jsonResponse = new JSONObject();

        if (cropName == null || cropName.trim().isEmpty()) {
            try {
				jsonResponse.put("error", "Crop name is required.");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        // Database credentials
        String jdbcURL = "jdbc:mysql://localhost:3306/CropDatabase";
        String dbUser = "root";
        String dbPassword = "globalwarn1705"; // Change this accordingly

        try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String query = "SELECT s.soil_name, g.total_period, g.growth_period, g.productivity_period " +
                           "FROM Crops c " +
                           "JOIN SoilType s ON c.soil_id = s.soil_id " +
                           "JOIN GrowthStages g ON c.crop_id = g.crop_id " +
                           "WHERE c.crop_name = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, cropName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    jsonResponse.put("soilType", rs.getString("soil_name"));
                    jsonResponse.put("totalPeriod", rs.getInt("total_period"));
                    jsonResponse.put("growthPeriod", rs.getInt("growth_period"));
                    jsonResponse.put("productivityPeriod", rs.getInt("productivity_period"));
                } else {
                    jsonResponse.put("error", "Crop not found.");
                }
            }
        } catch (Exception e) {
            try {
				jsonResponse.put("error", "Database error: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
