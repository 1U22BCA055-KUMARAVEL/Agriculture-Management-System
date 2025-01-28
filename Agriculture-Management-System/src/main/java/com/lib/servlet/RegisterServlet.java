package com.lib.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt; // Make sure you add bcrypt to your project dependencies

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Hash the password
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		// Database connection details (Consider moving this to a configuration file)
		String url = "jdbc:mysql://localhost:3306/login";
		String dbUsername = "root"; // replace with your DB username
		String dbPassword = "globalwarn1705"; // replace with your DB password

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			// Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establish connection
			conn = DriverManager.getConnection(url, dbUsername, dbPassword);

			// Check if the username already exists
			String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
			ps = conn.prepareStatement(checkUserSql);
			ps.setString(1, username);
			var resultSet = ps.executeQuery();
			if (resultSet.next() && resultSet.getInt(1) > 0) {
				response.sendRedirect("login.html?error=Username%20already%20exists");
				return;
			}

			// Insert user into the database
			String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, hashedPassword);
			int result = ps.executeUpdate();

			if (result > 0) {
				// Redirect to login page after successful registration
				response.sendRedirect("login.html");
			} else {
				// In case of error, stay on the registration page with an error message
				response.sendRedirect("login.html?error=Registration%20Failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("login.html?error=Database%20Error");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
