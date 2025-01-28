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

		try {
			// Define the SQL query
			String query = "SELECT total_period, growth_period, productivity_period FROM crops WHERE crop_name = ?";

			// Database connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CropManagement", "root",
					"globalwarn1705");

			// Prepare the statement with the query
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, cropName);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int totalPeriod = rs.getInt("total_period");
				int growthPeriod = rs.getInt("growth_period");
				int productivityPeriod = rs.getInt("productivity_period");

				// Return JSON response
				out.print("{ \"valid\": true, \"totalPeriod\": " + totalPeriod + ", \"growthPeriod\": " + growthPeriod
						+ ", \"productivityPeriod\": " + productivityPeriod + " }");
			} else {
				// Return invalid response
				out.print("{ \"valid\": false, \"message\": \"No data found for the given crop name.\" }");
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{ \"valid\": false, \"error\": \"" + e.getMessage() + "\" }");
		}

		out.flush();
	}
}
