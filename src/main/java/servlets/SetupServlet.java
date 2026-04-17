package servlets;

import java.io.IOException;

import beans.Utilisateur;
import services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet pour créer le compte admin initial
 * À SUPPRIMER après la première utilisation !
 */
@WebServlet("/setup")
public class SetupServlet extends HttpServlet {

	private static final boolean ENABLED = false;
	private static final String ADMIN_PASSWORD_ENV = "GESUSERS_SETUP_ADMIN_PASSWORD";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!ENABLED) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String remoteAddr = request.getRemoteAddr();
		if (!"127.0.0.1".equals(remoteAddr) && !"0:0:0:0:0:0:0:1".equals(remoteAddr) && !"::1".equals(remoteAddr)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Setup accessible uniquement en local");
			return;
		}
		
		UtilisateurService service = new UtilisateurService();
		if (service.loginExiste("admin")) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println("<html><body>");
			response.getWriter().println("<h1>Compte admin déjà existant</h1>");
			response.getWriter().println("<p><a href='" + request.getContextPath() + "/login'>Se connecter</a></p>");
			response.getWriter().println("</body></html>");
			return;
		}

		String adminPassword = System.getenv(ADMIN_PASSWORD_ENV);
		if (adminPassword == null || adminPassword.length() < 8) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Variable d'environnement " + ADMIN_PASSWORD_ENV + " absente ou trop faible");
			return;
		}
		
		try {
			Utilisateur admin = new Utilisateur("Administrateur", "Systeme", "admin", adminPassword);
			admin.setRole("ADMIN");
			
			if (service.ajouterUtilisateur(admin)) {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().println("<html><body>");
				response.getWriter().println("<h1>Compte admin cree avec succes</h1>");
				response.getWriter().println("<p>Login: <strong>admin</strong></p>");
				response.getWriter().println("<p>Mot de passe: défini via variable d'environnement <strong>" + ADMIN_PASSWORD_ENV + "</strong></p>");
				response.getWriter().println("<p><a href='" + request.getContextPath() + "/login'>Se connecter</a></p>");
				response.getWriter().println("<p style='color: red;'><strong>Desactivez definitivement le setup en production.</strong></p>");
				response.getWriter().println("</body></html>");
			} else {
				response.getWriter().println("<h1>Erreur lors de la création</h1>");
			}
			
		} catch (Exception e) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println("<html><body>");
			response.getWriter().println("<h1>Erreur</h1>");
			response.getWriter().println("<p>" + e.getMessage() + "</p>");
			response.getWriter().println("<p>Le compte admin existe peut-être déjà.</p>");
			response.getWriter().println("<p><a href='" + request.getContextPath() + "/login'>Essayez de vous connecter</a></p>");
			response.getWriter().println("</body></html>");
		}
	}
}
