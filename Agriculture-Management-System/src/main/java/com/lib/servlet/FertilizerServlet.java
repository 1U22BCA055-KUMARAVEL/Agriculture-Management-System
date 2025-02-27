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
        double nitrogenReduction = 0, phosphorusReduction = 0, potassiumReduction = 0;

        String jdbcURL = "jdbc:mysql://localhost:3306/Agriculture_Management_System";
        String dbUser = "root";
        String dbPassword = "globalwarn1705";
        JSONObject jsonResponse = new JSONObject();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Get crop IDs
            String cropIdQuery = "SELECT crop_id FROM Crops WHERE crop_name = ?";
            PreparedStatement psCrop = conn.prepareStatement(cropIdQuery);
            psCrop.setString(1, previousCrop);
            ResultSet rsPreviousCrop = psCrop.executeQuery();
            int previousCropId = rsPreviousCrop.next() ? rsPreviousCrop.getInt("crop_id") : -1;

            psCrop.setString(1, currentCrop);
            ResultSet rsCurrentCrop = psCrop.executeQuery();
            int currentCropId = rsCurrentCrop.next() ? rsCurrentCrop.getInt("crop_id") : -1;

            // Fetch fertilizer recommendations
            String query = "SELECT fr.nitrogen_kg_per_hectare, fr.phosphorus_kg_per_hectare, fr.potassium_kg_per_hectare, fr.organic_matter_per_hectare " +
                           "FROM FertilizerRecommendations fr " +
                           "JOIN Crops c ON fr.crop_id = c.crop_id " +
                           "WHERE c.crop_name = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentCrop);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nitrogen = rs.getDouble("nitrogen_kg_per_hectare");
                phosphorus = rs.getDouble("phosphorus_kg_per_hectare");
                potassium = rs.getDouble("potassium_kg_per_hectare");
                organicMatter = rs.getDouble("organic_matter_per_hectare");
            } else {
                jsonResponse.put("error", "No data found for selected crop.");
            }

            // Fetch fertilizer reduction values
            String reductionQuery = "SELECT nitrogen_reduction, phosphorus_reduction, potassium_reduction FROM FertilizerReduction " +
                                    "WHERE previous_crop_id = ? AND current_crop_id = ?";
            PreparedStatement psReduction = conn.prepareStatement(reductionQuery);
            psReduction.setInt(1, previousCropId);
            psReduction.setInt(2, currentCropId);
            ResultSet rsReduction = psReduction.executeQuery();

            if (rsReduction.next()) {
                nitrogenReduction = rsReduction.getDouble("nitrogen_reduction");
                phosphorusReduction = rsReduction.getDouble("phosphorus_reduction");
                potassiumReduction = rsReduction.getDouble("potassium_reduction");
                
                nitrogen -= nitrogenReduction;
                phosphorus -= phosphorusReduction;
                potassium -= potassiumReduction;
            }

            if ("natural".equals(fertilizerType)) {
                jsonResponse.put("organicMatter", organicMatter + " tons per hectare");
            } else if ("man-made".equals(fertilizerType)) {
                jsonResponse.put("nitrogen", nitrogen + " kg per hectare");
                jsonResponse.put("phosphorus", phosphorus + " kg per hectare");
                jsonResponse.put("potassium", potassium + " kg per hectare");
                jsonResponse.put("nitrogenReduction", nitrogenReduction);
                jsonResponse.put("phosphorusReduction", phosphorusReduction);
                jsonResponse.put("potassiumReduction", potassiumReduction);
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