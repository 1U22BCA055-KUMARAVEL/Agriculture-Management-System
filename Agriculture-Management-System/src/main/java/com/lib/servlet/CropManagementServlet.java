package com.lib.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/CropManagementServlet")
public class CropManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String landformId = request.getParameter("landformId");
        String climateId = request.getParameter("climateId");
        String soilTypeId = request.getParameter("soilTypeId");
        String cropId = request.getParameter("cropId");
        
        JSONObject jsonResponse = new JSONObject();
        
        if (landformId == null || climateId == null || soilTypeId == null || cropId == null) {
            try {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "Invalid selection. Ensure all fields are selected correctly.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            out.print(jsonResponse.toString());
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Agriculture_Management_System", "root", "globalwarn1705");
            
            // Validate Landform and Climate relation
            String query = "SELECT COUNT(*) FROM Climate WHERE landform_id = ? AND climate_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(landformId));
            stmt.setInt(2, Integer.parseInt(climateId));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "Selected Climate does not match the Landform.");
                out.print(jsonResponse.toString());
                return;
            }
            
            // Validate Climate and Soil Type relation
            query = "SELECT COUNT(*) FROM SoilType WHERE climate_id = ? AND soiltype_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(climateId));
            stmt.setInt(2, Integer.parseInt(soilTypeId));
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "Selected Soil Type does not match the Climate.");
                out.print(jsonResponse.toString());
                return;
            }
            
            // Validate Soil Type and Crop relation
            query = "SELECT COUNT(*) FROM Crops WHERE soiltype_id = ? AND crop_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(soilTypeId));
            stmt.setInt(2, Integer.parseInt(cropId));
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "Selected Crop does not match the Soil Type.");
                out.print(jsonResponse.toString());
                return;
            }
            
            // Fetch crop period data
            query = "SELECT total_period, growth_period, productivity_period FROM CropPeriods WHERE crop_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(cropId));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                jsonResponse.put("valid", true);
                jsonResponse.put("totalPeriod", rs.getInt("total_period"));
                jsonResponse.put("growthPeriod", rs.getInt("growth_period"));
                jsonResponse.put("productivityPeriod", rs.getInt("productivity_period"));
            } else {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "No data found for selected crop.");
            }
            
            out.print(jsonResponse.toString());
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            try {
                jsonResponse.put("valid", false);
                jsonResponse.put("error", "Database error: " + e.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            out.print(jsonResponse.toString());
        }
    }
}
