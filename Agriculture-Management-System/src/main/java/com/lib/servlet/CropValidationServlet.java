package com.lib.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CropValidationServlet")
public class CropValidationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cropName = request.getParameter("cropName");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String url="jdbc:mysql://localhost:3306/CropDatabase";
        String username="root";
        String password="globalwarn1705";

        try {
            String query = "SELECT C.total_period, C.growth_period, C.productivity_period, S.soil_name " +
                           "FROM Crops C JOIN SoilType S ON C.soil_id = S.soil_id WHERE C.crop_name = ?";

            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url,username,password);

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cropName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.print("{ \"valid\": true, \"soilType\": \"" + rs.getString("soil_name") + "\", " +
                          "\"totalPeriod\": " + rs.getInt("total_period") + ", " +
                          "\"growthPeriod\": " + rs.getInt("growth_period") + ", " +
                          "\"productivityPeriod\": " + rs.getInt("productivity_period") + " }");
            } else {
                out.print("{ \"valid\": false, \"message\": \"No data found for the given crop name.\" }");
            }
            conn.close();
        } catch (Exception e) {
            out.print("{ \"valid\": false, \"error\": \"" + e.getMessage() + "\" }");
        }
        out.flush();
    }
}
