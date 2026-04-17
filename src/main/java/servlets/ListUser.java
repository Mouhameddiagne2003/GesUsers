package servlets;

import java.io.IOException;

import services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

@WebServlet("/list")
public class ListUser extends HttpServlet {

	private static final String LIST_USERS_VIEW = "/WEB-INF/listerUtilisateurs.jsp";
	private final UtilisateurService utilisateurService = new UtilisateurService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("csrfToken") == null) {
			session.setAttribute("csrfToken", UUID.randomUUID().toString());
		}

		request.setAttribute("utilisateurs", utilisateurService.listerUtilisateurs());

		getServletContext().getRequestDispatcher(LIST_USERS_VIEW).forward(request, response);

	}
}
