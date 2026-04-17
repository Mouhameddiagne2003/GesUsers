package servlets;

import java.io.IOException;
import java.net.URLEncoder;

import beans.Utilisateur;
import services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/add")
public class AddUser extends HttpServlet {

	private static final String FORM_ADD_USER_VIEW = "/WEB-INF/ajouterUtilisateur.jsp";
	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			String message = URLEncoder.encode("Accès refusé: droits administrateur requis", "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
			return;
		}

		getServletContext().getRequestDispatcher(FORM_ADD_USER_VIEW).forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			String message = URLEncoder.encode("Accès refusé: droits administrateur requis", "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
			return;
		}

		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		Utilisateur u = new Utilisateur(nom, prenom, login, password);
		String message = "Erreur lors de l'ajout";
		boolean status = false;
		
		try {
			if (utilisateurService.ajouterUtilisateur(u)) {
				message = "Ajout effectué avec succès";
				status = true;
			}
		} catch (IllegalArgumentException e) {
			message = "Erreur : " + e.getMessage();
		}
		
		String url = String.format("%s/list?message=%s&status=%s", request.getContextPath(), URLEncoder.encode(message, "UTF-8"),
				status ? "success" : "error");
		response.sendRedirect(url);
	}

	private boolean isAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		Object role = session.getAttribute("userRole");
		return role != null && "ADMIN".equals(String.valueOf(role));
	}

}
