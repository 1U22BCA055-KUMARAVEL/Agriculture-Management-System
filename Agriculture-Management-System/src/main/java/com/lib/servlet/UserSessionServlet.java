package com.lib.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/getUsername")
public class UserSessionServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false); // Get the session if it exists
		if (session != null && session.getAttribute("username") != null) {
			String username = (String) session.getAttribute("username");
			// Send a response indicating the user is logged in and include the username
			out.print("{\"loggedIn\": true, \"username\": \"" + username + "\"}");
		} else {
			// Send a response indicating the user is not logged in
			out.print("{\"loggedIn\": false}");
		}

		out.flush();
	}
}
