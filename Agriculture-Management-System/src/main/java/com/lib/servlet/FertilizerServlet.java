
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

@WebServlet("/FertilizerServlet")
public class FertilizerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String previousCrop = request.getParameter("previousCrop");
        String currentCrop = request.getParameter("currentCrop");
        String fertilizerType = request.getParameter("fertilizerType");
        
        double nitrogen = 0, phosphorus = 0, potassium = 0;
        double nitrogenReduction = 0, phosphorusReduction = 0, potassiumReduction = 0;
        String organicMatter = "";
        
        String jdbcURL = "jdbc:mysql://localhost:3306/Agriculture_Management_System";
        String dbUser = "root";
        String dbPassword = "globalwarn1705";
        
        JSONObject jsonResponse = new JSONObject();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            
            // Get fertilizer requirements for current crop
            String query = "SELECT nitrogen_kg_per_hectare, phosphorus_kg_per_hectare, potassium_kg_per_hectare FROM FertilizerRecommendations WHERE crop_id IN (SELECT crop_id FROM Crops WHERE crop_name = ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentCrop);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                nitrogen = rs.getDouble("nitrogen_kg_per_hectare");
                phosphorus = rs.getDouble("phosphorus_kg_per_hectare");
                potassium = rs.getDouble("potassium_kg_per_hectare");
            }
            
            // Check if previous crop is a nitrogen-fixing agent
            String nitrogenFixerQuery = "SELECT is_nitrogen_fixer, nitrogen_contribution, phosphorus_contribution, potassium_contribution FROM Crops WHERE crop_name = ?";
            PreparedStatement psNitrogen = conn.prepareStatement(nitrogenFixerQuery);
            psNitrogen.setString(1, previousCrop);
            ResultSet rsNitrogen = psNitrogen.executeQuery();
            
            if (rsNitrogen.next()) {
                boolean isNitrogenFixer = rsNitrogen.getBoolean("is_nitrogen_fixer");
                if (isNitrogenFixer) {
                    nitrogenReduction = rsNitrogen.getDouble("nitrogen_contribution");
                    phosphorusReduction = rsNitrogen.getDouble("phosphorus_contribution");
                    potassiumReduction = rsNitrogen.getDouble("potassium_contribution");
                    
                    // Apply reductions
                    nitrogen = Math.max(0, nitrogen - nitrogenReduction);
                    phosphorus = Math.max(0, phosphorus - phosphorusReduction);
                    potassium = Math.max(0, potassium - potassiumReduction);
                }
            }
            
            // Handle natural fertilizer type
            if (fertilizerType.equals("natural")) {
                // Get organic matter recommendations
                String organicQuery = "SELECT organic_matter_recommendation FROM OrganicFertilizers WHERE crop_id IN (SELECT crop_id FROM Crops WHERE crop_name = ?)";
                PreparedStatement psOrganic = conn.prepareStatement(organicQuery);
                psOrganic.setString(1, currentCrop);
                ResultSet rsOrganic = psOrganic.executeQuery();
                
                if (rsOrganic.next()) {
                    organicMatter = rsOrganic.getString("organic_matter_recommendation");
                } else {
                    organicMatter = "Use 5-10 tons of well-decomposed compost or farmyard manure per hectare";
                }
                
                jsonResponse.put("organicMatter", organicMatter);
            } else {
                // Man-made fertilizer - return NPK values
                jsonResponse.put("nitrogen", nitrogen);
                jsonResponse.put("phosphorus", phosphorus);
                jsonResponse.put("potassium", potassium);
            }
            
            rs.close();
            ps.close();
            conn.close();
            
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
				jsonResponse.put("error", "Database error: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        }
    }
}