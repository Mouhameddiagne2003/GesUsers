package servlets;

import java.io.IOException;

import beans.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.UtilisateurService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";
	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("utilisateur") != null) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
			return;
		}

		getServletContext().getRequestDispatcher(LOGIN_VIEW).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String login = request.getParameter("login");
		String password = request.getParameter("password");

		if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			request.setAttribute("error", "Login et mot de passe obligatoires");
			getServletContext().getRequestDispatcher(LOGIN_VIEW).forward(request, response);
			return;
		}
		
		Utilisateur utilisateur = utilisateurService.authenticate(login.trim(), password);
		if (utilisateur != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("utilisateur", utilisateur);
			session.setAttribute("userId", utilisateur.getId());
			session.setAttribute("userRole", utilisateur.getRole());
			session.setMaxInactiveInterval(30 * 60);

			response.sendRedirect(request.getContextPath() + "/dashboard");
		} else {
			request.setAttribute("error", "Login ou mot de passe incorrect");
			getServletContext().getRequestDispatcher(LOGIN_VIEW).forward(request, response);
		}
	}
}
