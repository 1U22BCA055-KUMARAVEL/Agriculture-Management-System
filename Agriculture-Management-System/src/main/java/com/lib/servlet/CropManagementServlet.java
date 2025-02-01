package com.lib.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
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

    // Database connection settings
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LandformClimateCrops";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "globalwarn1705";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        JSONObject jsonResponse = new JSONObject();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if ("getCountries".equals(action)) {
                String landform = request.getParameter("landform");
                jsonResponse.put("countries", getCountriesByLandform(conn, landform));
            } else if ("getCrops".equals(action)) {
                String country = request.getParameter("country");
                jsonResponse.put("crops", getCropsByCountry(conn, country));
            } else if ("getPeriods".equals(action)) {
                String crop = request.getParameter("crop");
                JSONObject periods = getCropPeriods(conn, crop);
                jsonResponse.put("totalPeriod", periods.getInt("totalPeriod"));
                jsonResponse.put("growthPeriod", periods.getInt("growthPeriod"));
                jsonResponse.put("productivityPeriod", periods.getInt("productivityPeriod"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jsonResponse.put("error", "Database connection failed: " + e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    private JSONArray getCountriesByLandform(Connection conn, String landform) throws Exception {
        JSONArray countries = new JSONArray();
        String query = "SELECT DISTINCT country FROM Crops WHERE landform_id = (SELECT landform_id FROM Landforms WHERE landform_name = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, landform);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                countries.put(rs.getString("country"));
            }
        }
        return countries;
    }

    private JSONArray getCropsByCountry(Connection conn, String country) throws Exception {
        JSONArray crops = new JSONArray();
        String query = "SELECT DISTINCT crop_name FROM Crops WHERE country_id = (SELECT country_id FROM Countries WHERE country_name = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crops.put(rs.getString("crop_name"));
            }
        }
        return crops;
    }

    private JSONObject getCropPeriods(Connection conn, String crop) throws Exception {
        JSONObject periods = new JSONObject();
        String query = "SELECT cp.total_period, cp.growth_period, cp.productivity_period " +
                       "FROM CropPeriods cp " +
                       "JOIN Crops c ON cp.crop_id = c.crop_id " +
                       "WHERE c.crop_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, crop);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                periods.put("totalPeriod", rs.getInt("total_period"));
                periods.put("growthPeriod", rs.getInt("growth_period"));
                periods.put("productivityPeriod", rs.getInt("productivity_period"));
            } else {
                periods.put("totalPeriod", 0);
                periods.put("growthPeriod", 0);
                periods.put("productivityPeriod", 0);
            }
        }
        return periods;
    }
}
