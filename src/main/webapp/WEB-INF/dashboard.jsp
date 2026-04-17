<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.Utilisateur" %>
<%
    Utilisateur currentUser = (Utilisateur) request.getAttribute("currentUser");
    Integer totalUtilisateurs = (Integer) request.getAttribute("totalUtilisateurs");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - GesUsers</title>
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
                <li><a href="<%= request.getContextPath() %>/list">Utilisateurs</a></li>
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
        <h1 style="margin-bottom: 2rem;">Bienvenue, <%= currentUser.getPrenom() %> !</h1>

        <div class="dashboard-grid">
            <div class="stat-card">
                <div class="stat-value"><%= totalUtilisateurs %></div>
                <div class="stat-label">Utilisateurs</div>
            </div>
            <div class="stat-card" style="border-color: var(--success-color);">
                <div class="stat-value" style="color: var(--success-color);">OK</div>
                <div class="stat-label">Connecté</div>
            </div>
            <div class="stat-card" style="border-color: var(--warning-color);">
                <div class="stat-value" style="color: var(--warning-color);"><%= currentUser.getRole() %></div>
                <div class="stat-label">Votre Rôle</div>
            </div>
        </div>

        <div class="card">
            <h2 style="margin-bottom: 1.5rem;">Actions Rapides</h2>
            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="<%= request.getContextPath() %>/list" class="btn btn-primary">
                    Voir tous les utilisateurs
                </a>
                <% if (currentUser.isAdmin()) { %>
                <a href="<%= request.getContextPath() %>/add" class="btn btn-success">
                    Ajouter un utilisateur
                </a>
                <% } %>
            </div>
        </div>

        <div class="card">
            <h2 style="margin-bottom: 1rem;">Informations</h2>
            <p><strong>Nom complet :</strong> <%= currentUser.getPrenom() %> <%= currentUser.getNom() %></p>
            <p><strong>Login :</strong> <%= currentUser.getLogin() %></p>
            <p><strong>Rôle :</strong> <span class="badge <%= currentUser.isAdmin() ? "badge-admin" : "badge-user" %>"><%= currentUser.getRole() %></span></p>
            <p style="margin-top: 1rem; color: var(--text-light); font-size: 0.875rem;">
                <% if (currentUser.isAdmin()) { %>
                    Vous avez tous les droits d'administration (CRUD complet)
                <% } else { %>
                    Vous avez un accès en lecture seule
                <% } %>
            </p>
        </div>
    </div>
</body>
</html>
