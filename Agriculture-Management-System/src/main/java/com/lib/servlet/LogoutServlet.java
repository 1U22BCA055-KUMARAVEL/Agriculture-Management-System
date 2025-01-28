package com.lib.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout") // URL Mapping updated to match logout call in JS
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Invalidate the session
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidate the session to log out the user
		}

		// Send a success response, and the frontend can handle the redirect
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
