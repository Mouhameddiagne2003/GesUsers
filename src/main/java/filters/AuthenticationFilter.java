package filters;

import java.io.IOException;
import java.net.URLEncoder;

import beans.Utilisateur;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtre pour vérifier l'authentification avant d'accéder aux pages protégées
 * Bloque l'accès si l'utilisateur n'est pas connecté
 */
@WebFilter(urlPatterns = {"/list", "/add", "/update", "/delete", "/dashboard"})
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("✓ AuthenticationFilter initialisé");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpSession session = httpRequest.getSession(false);

		boolean isLoggedIn = (session != null && session.getAttribute("utilisateur") != null);

		if (!isLoggedIn) {
			String contextPath = httpRequest.getContextPath();
			String errorMessage = URLEncoder.encode("Veuillez vous connecter pour accéder à cette page", "UTF-8");
			httpResponse.sendRedirect(contextPath + "/login?error=" + errorMessage);
			return;
		}

		String path = httpRequest.getServletPath();
		boolean adminRoute = "/add".equals(path) || "/delete".equals(path);
		if (adminRoute) {
			Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
			if (currentUser == null || !currentUser.isAdmin()) {
				String contextPath = httpRequest.getContextPath();
				String errorMessage = URLEncoder.encode("Accès refusé: droits administrateur requis", "UTF-8");
				httpResponse.sendRedirect(contextPath + "/list?message=" + errorMessage + "&status=error");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		System.out.println("✓ AuthenticationFilter détruit");
	}
}
