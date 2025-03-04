package com.lib.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish database connection
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root",
					"globalwarn1705");

			// Prepare SQL query
			String sql = "SELECT * FROM users WHERE username = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);

			// Execute query
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				// Retrieve the stored hash from the database
				String storedHash = rs.getString("password");

				// Use BCrypt to check if the provided password matches the stored hash
				if (BCrypt.checkpw(password, storedHash)) {
					// Valid login
					HttpSession session = request.getSession();
					session.setAttribute("username", username);

					// Redirect to homepage
					response.sendRedirect("General_module/Homepage.html");
				} else {
					// Invalid password
					response.sendRedirect("login.html?error=Invalid Username or Password");
				}
			} else {
				// Invalid username
				response.sendRedirect("login.html?error=Invalid Username or Password");
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("login.html?error=Something went wrong. Please try again later.");
		}
	}
}
