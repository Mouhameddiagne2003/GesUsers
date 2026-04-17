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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final String DASHBOARD_VIEW = "/WEB-INF/dashboard.jsp";
	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("utilisateur") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		int totalUtilisateurs = utilisateurService.listerUtilisateurs().size();
		Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
		
		request.setAttribute("totalUtilisateurs", totalUtilisateurs);
		request.setAttribute("currentUser", currentUser);
		
		getServletContext().getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
	}
}
