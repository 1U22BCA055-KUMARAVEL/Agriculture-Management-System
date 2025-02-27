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
        String crop = request.getParameter("crop");
        
        if (crop == null || crop.isEmpty()) {
            JSONObject errorResponse = new JSONObject();
            try {
				errorResponse.put("error", "Invalid crop selection");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            out.print(errorResponse.toString());
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Agriculture_Management_System", "root", "globalwarn1705");
            String query = "SELECT total_period, growth_period, productivity_period FROM cropperiods WHERE crop_id = (SELECT crop_id FROM crops WHERE crop_name = ? LIMIT 1)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, crop);
            ResultSet rs = stmt.executeQuery();
            
            JSONObject jsonResponse = new JSONObject();
            if (rs.next()) {
                jsonResponse.put("totalPeriod", rs.getInt("total_period"));
                jsonResponse.put("growthPeriod", rs.getInt("growth_period"));
                jsonResponse.put("productivityPeriod", rs.getInt("productivity_period"));
            } else {
                jsonResponse.put("error", "No data found for selected crop");
            }
            out.print(jsonResponse.toString());
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            JSONObject errorResponse = new JSONObject();
            try {
				errorResponse.put("error", "Database error: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            out.print(errorResponse.toString());
        }
    }
}