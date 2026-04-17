package servlets;

import java.io.IOException;
import java.net.URLEncoder;

import services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/delete")
public class RemoveUser extends HttpServlet {

	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = URLEncoder.encode("Méthode non autorisée pour la suppression", "UTF-8");
		response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAdmin(request)) {
			String message = URLEncoder.encode("Accès refusé: droits administrateur requis", "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
			return;
		}

		if (!isValidCsrfToken(request)) {
			String message = URLEncoder.encode("Erreur de sécurité (CSRF): requête invalide", "UTF-8");
			response.sendRedirect(request.getContextPath() + "/list?message=" + message + "&status=error");
			return;
		}

		String id = request.getParameter("id");
		String message = "Erreur lors de la suppression";
		boolean status = false;
		
		try {
			if (utilisateurService.supprimerUtilisateur(Integer.parseInt(id))) {
				message = "Suppression effectuée avec succès";
				status = true;
			}
		} catch (NumberFormatException e) {
			message = "Erreur : ID utilisateur invalide";
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

	private boolean isValidCsrfToken(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}

		String sessionToken = (String) session.getAttribute("csrfToken");
		String requestToken = request.getParameter("csrfToken");

		return sessionToken != null && sessionToken.equals(requestToken);
	}

}
