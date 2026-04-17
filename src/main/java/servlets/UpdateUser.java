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

@WebServlet("/update")
public class UpdateUser extends HttpServlet {

	private static final String FORM_UPDATE_USER_VIEW = "/WEB-INF/modifierUtilisateur.jsp";
	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		
		try {
			int userId = Integer.parseInt(id);
			if (!canManageUser(request, userId)) {
				String message = URLEncoder.encode("Accès refusé: vous ne pouvez modifier que votre propre profil", "UTF-8");
				response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
				return;
			}

			Utilisateur utilisateur = utilisateurService.getUtilisateur(userId);
			if (utilisateur == null) {
				response.sendRedirect(request.getContextPath() + "/list?message=" + URLEncoder.encode("Utilisateur introuvable", "UTF-8") + "&status=error");
			} else {
				request.setAttribute("utilisateur", utilisateur);
				getServletContext().getRequestDispatcher(FORM_UPDATE_USER_VIEW).forward(request, response);
			}
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/list?message=" + URLEncoder.encode("Erreur : " + e.getMessage(), "UTF-8") + "&status=error");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		
		try {
			int userId = Integer.parseInt(id);
			if (!canManageUser(request, userId)) {
				String message = URLEncoder.encode("Accès refusé: vous ne pouvez modifier que votre propre profil", "UTF-8");
				response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
				return;
			}

			Utilisateur existant = utilisateurService.getUtilisateur(userId);
			if (existant == null) {
				response.sendRedirect(request.getContextPath() + "/list?message=" + URLEncoder.encode("Utilisateur introuvable", "UTF-8") + "&status=error");
				return;
			}

			String finalPassword = password;
			if (password == null || password.trim().isEmpty()) {
				finalPassword = existant.getPassword();
			}

			Utilisateur u = new Utilisateur(userId, nom, prenom, login, finalPassword, existant.getRole());
			String message = "";
			boolean status = false;

			if (utilisateurService.modifierUtilisateur(u)) {
				message = "Mise à jour effectuée avec succès";
				status = true;
			} else {
				message = "Erreur lors de la mise à jour";
			}

			String url = String.format("%s/list?message=%s&status=%s", request.getContextPath(), URLEncoder.encode(message, "UTF-8"),
					status ? "success" : "error");
			response.sendRedirect(url);
		} catch (NumberFormatException e) {
			String message = URLEncoder.encode("ID utilisateur invalide", "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
		} catch (IllegalArgumentException e) {
			String message = URLEncoder.encode("Erreur : " + e.getMessage(), "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
		}
	}

	private boolean isAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		Object role = session.getAttribute("userRole");
		return role != null && "ADMIN".equals(String.valueOf(role));
	}

	private boolean canManageUser(HttpServletRequest request, int targetUserId) {
		if (isAdmin(request)) {
			return true;
		}
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return false;
		}
		try {
			int currentUserId = Integer.parseInt(String.valueOf(userIdObj));
			return currentUserId == targetUserId;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
