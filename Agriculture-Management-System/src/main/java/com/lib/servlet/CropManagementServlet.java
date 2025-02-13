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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LandformClimateCrops";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "globalwarn1705";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            switch (action) {
                case "getCountriesByLandform":
                    String landform = request.getParameter("landform");
                    jsonResponse.put("countries", getCountriesByLandform(conn, landform));
                    break;

                case "getCropsByCountry":
                    String country = request.getParameter("country");
                    jsonResponse.put("crops", getCropsByCountry(conn, country));
                    break;

                case "getPeriods":
                    String crop = request.getParameter("crop");
                    JSONObject periods = getCropPeriods(conn, crop);
                    jsonResponse.put("totalPeriod", periods.getInt("totalPeriod"));
                    jsonResponse.put("growthPeriod", periods.getInt("growthPeriod"));
                    jsonResponse.put("productivityPeriod", periods.getInt("productivityPeriod"));
                    break;

                default:
                    jsonResponse.put("error", "Invalid action.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                jsonResponse.put("error", "Database connection failed: " + e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        response.getWriter().write(jsonResponse.toString());
    }

    private JSONArray getCountriesByLandform(Connection conn, String landform) throws Exception {
        JSONArray countries = new JSONArray();
        String query = "SELECT DISTINCT country FROM Crops WHERE landform = ?";
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
        String query = "SELECT DISTINCT crop FROM Crops WHERE country = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                crops.put(rs.getString("crop"));
            }
        }
        return crops;
    }

    private JSONObject getCropPeriods(Connection conn, String crop) throws Exception {
        JSONObject periods = new JSONObject();
        String query = "SELECT totalPeriod, growthPeriod, productivityPeriod FROM Crops WHERE crop = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, crop);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                periods.put("totalPeriod", rs.getInt("totalPeriod"));
                periods.put("growthPeriod", rs.getInt("growthPeriod"));
                periods.put("productivityPeriod", rs.getInt("productivityPeriod"));
            } else {
                throw new Exception("Crop not found in the database.");
            }
        }
        return periods;
    }
}
