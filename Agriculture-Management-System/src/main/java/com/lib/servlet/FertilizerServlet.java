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
        // Get parameters from the request
        String currentCrop = request.getParameter("currentCrop");
        String soilType = request.getParameter("soilType");
        String fertilizerType = request.getParameter("fertilizerType");

        double nitrogen = 0, phosphorus = 0, potassium = 0, organicMatter = 0;

        // Database credentials
        String jdbcURL = "jdbc:mysql://localhost:3306/CropDatabase";
        String dbUser = "root";
        String dbPassword = "globalwarn1705"; // Change this accordingly

        try {
            // Set up database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Query to get fertilizer recommendations based on crop and soil type
            String query = "SELECT fr.nitrogen_kg_per_ha, fr.phosphorus_kg_per_ha, fr.potassium_kg_per_ha, fr.organic_matter_tons_per_ha " +
                           "FROM FertilizerRecommendations fr " +
                           "JOIN Crops c ON fr.crop_id = c.crop_id " +
                           "JOIN SoilType s ON c.soil_id = s.soil_id " +
                           "WHERE c.crop_name = ? AND s.soil_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentCrop);
            ps.setString(2, soilType);
            ResultSet rs = ps.executeQuery();

            // If data is found for the specified crop and soil type
            if (rs.next()) {
                nitrogen = rs.getDouble("nitrogen_kg_per_ha");
                phosphorus = rs.getDouble("phosphorus_kg_per_ha");
                potassium = rs.getDouble("potassium_kg_per_ha");
                organicMatter = rs.getDouble("organic_matter_tons_per_ha");
            }

            // Prepare the response based on the fertilizer type
            JSONObject jsonResponse = new JSONObject();
            if ("natural".equals(fertilizerType)) {
                // Return only organic matter for natural fertilizers
                jsonResponse.put("organicMatter", organicMatter + " tons per hectare");
            } else if ("man-made".equals(fertilizerType)) {
                // Return nitrogen, phosphorus, and potassium for man-made fertilizers
                jsonResponse.put("nitrogen", nitrogen + " kg per hectare");
                jsonResponse.put("phosphorus", phosphorus + " kg per hectare");
                jsonResponse.put("potassium", potassium + " kg per hectare");
            } else {
                jsonResponse.put("error", "Invalid fertilizer type");
            }

            // Set response content type to JSON and send the response
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Database error\"}");
        }
    }
}
