package com.lib.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FertilizerServlet")
public class FertilizerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String previousCrop = request.getParameter("previousCrop");
        String currentCrop = request.getParameter("currentCrop");
        String soilType = request.getParameter("soilType");
        String fertilizerType = request.getParameter("fertilizerType");

        double nitrogen = 0, phosphorus = 0, potassium = 0, organicMatter = 0;
        boolean isNitrogenFixer = false;

        String jdbcURL = "jdbc:mysql://localhost:3306/Agriculture_Management_System";
        String dbUser = "root";
        String dbPassword = "globalwarn1705";
        JSONObject jsonResponse = new JSONObject();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Check if the previous crop was a nitrogen fixer
            String checkFixerQuery = "SELECT is_nitrogen_fixer FROM Crops WHERE crop_name = ?";
            PreparedStatement psCheck = conn.prepareStatement(checkFixerQuery);
            psCheck.setString(1, previousCrop);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next()) {
                isNitrogenFixer = rsCheck.getBoolean("is_nitrogen_fixer");
            }

            // Fetch fertilizer recommendations
            String query = "SELECT fr.nitrogen_kg_per_hectare, fr.phosphorus_kg_per_hectare, fr.potassium_kg_per_hectare, fr.organic_matter_per_hectare " +
                           "FROM FertilizerRecommendations fr " +
                           "JOIN Crops c ON fr.crop_id = c.crop_id " +
                           "JOIN SoilTypes s ON c.soil_id = s.soil_id " +
                           "WHERE c.crop_name = ? AND s.soil_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentCrop);
            ps.setString(2, soilType);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nitrogen = rs.getDouble("nitrogen_kg_per_hectare");
                phosphorus = rs.getDouble("phosphorus_kg_per_hectare");
                potassium = rs.getDouble("potassium_kg_per_hectare");
                organicMatter = rs.getDouble("organic_matter_per_hectare");
            } else {
                jsonResponse.put("error", "No data found for selected crop and soil.");
            }

            // Reduce nitrogen need if the previous crop was a nitrogen fixer
            if (isNitrogenFixer) {
                nitrogen *= 0.7;  // Reduce nitrogen by 30%
                phosphorus *= 0.9;  // Reduce phosphorus by 10%
            }

            if ("natural".equals(fertilizerType)) {
                jsonResponse.put("organicMatter", organicMatter + " tons per hectare");
            } else if ("man-made".equals(fertilizerType)) {
                jsonResponse.put("nitrogen", nitrogen + " kg per hectare");
                jsonResponse.put("phosphorus", phosphorus + " kg per hectare");
                jsonResponse.put("potassium", potassium + " kg per hectare");
            } else {
                jsonResponse.put("error", "Invalid fertilizer type");
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Database error\"}");
        }
    }
}
