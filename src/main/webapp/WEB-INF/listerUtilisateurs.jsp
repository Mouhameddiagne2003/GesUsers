<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="beans.Utilisateur" %>
<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }
%>
<%
    String csrfToken = (String) session.getAttribute("csrfToken");
    List<Utilisateur> utilisateurs = (List<Utilisateur>) request.getAttribute("utilisateurs");
    Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
    Integer currentUserId = (Integer) session.getAttribute("userId");
    String message = request.getParameter("message");
    String status = request.getParameter("status");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des utilisateurs - GesUsers</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="<%= request.getContextPath() %>/dashboard" class="navbar-brand">
                GesUsers
            </a>
            <ul class="navbar-menu">
                <li><a href="<%= request.getContextPath() %>/dashboard">Accueil</a></li>
                <li><a href="<%= request.getContextPath() %>/list" style="color: var(--primary-color);">Utilisateurs</a></li>
                <li><a href="<%= request.getContextPath() %>/add">Ajouter</a></li>
            </ul>
            <div class="navbar-user">
                <span class="user-badge <%= currentUser.isAdmin() ? "badge-admin" : "badge-user" %>">
                    <%= currentUser.getRole() %>
                </span>
                <span><%= currentUser.getPrenom() %> <%= currentUser.getNom() %></span>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-danger btn-sm">
                    Deconnexion
                </a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h1>Liste des utilisateurs</h1>
            <% if (currentUser.isAdmin()) { %>
            <a href="<%= request.getContextPath() %>/add" class="btn btn-primary">
                Nouvel utilisateur
            </a>
            <% } %>
        </div>

        <% if(message != null && status != null) { %>
            <div class="alert <%= status.equals("success") ? "alert-success" : "alert-error" %>">
                <%= escapeHtml(message) %>
            </div>
        <% } %>

        <% if (utilisateurs == null || utilisateurs.isEmpty()) { %>
            <div class="card text-center">
                <p style="color: var(--text-light);">Aucun utilisateur trouvé.</p>
            </div>
        <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Login</th>
                        <th>Rôle</th>
                        <% if (currentUser.isAdmin() || currentUserId != null) { %>
                        <th>Actions</th>
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                <% for (Utilisateur u : utilisateurs) { %>
                    <tr>
                        <td><%= u.getId() %></td>
                        <td><%= escapeHtml(u.getNom()) %></td>
                        <td><%= escapeHtml(u.getPrenom()) %></td>
                        <td><%= escapeHtml(u.getLogin()) %></td>
                        <td>
                            <span class="badge <%= u.isAdmin() ? "badge-admin" : "badge-user" %>">
                                <%= u.getRole() %>
                            </span>
                        </td>
                        <% if (currentUser.isAdmin() || (currentUserId != null && currentUserId == u.getId())) { %>
                        <td>
                            <a class="btn btn-success btn-sm" href="<%= request.getContextPath() %>/update?id=<%= u.getId() %>">
                                Modifier
                            </a>
                            <% if (currentUser.isAdmin()) { %>
                            <form action="<%= request.getContextPath() %>/delete" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="<%= u.getId() %>">
                                <input type="hidden" name="csrfToken" value="<%= csrfToken %>">
                                <button class="btn btn-danger btn-sm" type="submit"
                                        onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');">
                                    Supprimer
                                </button>
                            </form>
                            <% } %>
                        </td>
                        <% } %>
                    </tr>
                <% } %>
                </tbody>
            </table>
        <%} %>
    </div>
</body>
</html>
