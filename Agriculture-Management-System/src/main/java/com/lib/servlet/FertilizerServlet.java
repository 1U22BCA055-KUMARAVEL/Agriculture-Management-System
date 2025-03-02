/* FertilizerServlet.java */

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String previousCrop = request.getParameter("previousCrop");
        String currentCrop = request.getParameter("currentCrop");
        String fertilizerType = request.getParameter("fertilizerType");

        double nitrogen = 0, phosphorus = 0, potassium = 0;
        double nitrogenReduction = 0, phosphorusReduction = 0, potassiumReduction = 0;

        String jdbcURL = "jdbc:mysql://localhost:3306/Agriculture_Management_System";
        String dbUser = "root";
        String dbPassword = "globalwarn1705";
        JSONObject jsonResponse = new JSONObject();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        		Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        	
            String query = "SELECT nitrogen_kg_per_hectare, phosphorus_kg_per_hectare, potassium_kg_per_hectare FROM FertilizerRecommendations WHERE crop_id IN (SELECT crop_id FROM Crops WHERE crop_name = ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentCrop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nitrogen = rs.getDouble("nitrogen_kg_per_hectare");
                phosphorus = rs.getDouble("phosphorus_kg_per_hectare");
                potassium = rs.getDouble("potassium_kg_per_hectare");
            }
            jsonResponse.put("nitrogen", nitrogen);
            jsonResponse.put("phosphorus", phosphorus);
            jsonResponse.put("potassium", potassium);

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
