<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.Utilisateur" %>
<%
    Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un utilisateur - GesUsers</title>
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
                <li><a href="<%= request.getContextPath() %>/add" style="color: var(--primary-color);">Ajouter</a></li>
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
        <h1 class="mb-3">Ajouter un utilisateur</h1>

        <div class="card" style="max-width: 600px;">
            <form method="post" action="<%= request.getContextPath() %>/add">
                <div class="form-group">
                    <label class="form-label" for="nom">Nom *</label>
                    <input type="text" name="nom" id="nom" class="form-input" 
                           placeholder="Entrez le nom" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prenom">Prénom *</label>
                    <input type="text" name="prenom" id="prenom" class="form-input" 
                           placeholder="Entrez le prénom" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="login">Login *</label>
                    <input type="text" name="login" id="login" class="form-input" 
                           placeholder="Entrez le login (3-20 caractères)" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Mot de passe *</label>
                    <input type="password" name="password" id="password" class="form-input" 
                           placeholder="Entrez le mot de passe (min. 6 caractères)" required>
                </div>

                <div style="display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary">
                        Ajouter l'utilisateur
                    </button>
                    <a href="<%= request.getContextPath() %>/list" class="btn" style="background: #6b7280; color: white;">
                        ← Retour à la liste
                    </a>
                </div>
            </form>
        </div>

        <div class="alert alert-info" style="max-width: 600px; margin-top: 1rem;">
            <strong>Informations :</strong>
            <ul style="margin: 0.5rem 0 0 1.5rem;">
                <li>Le login doit contenir 3-20 caractères (lettres, chiffres, underscore)</li>
                <li>Le mot de passe doit contenir au moins 6 caractères</li>
                <li>Le mot de passe sera automatiquement chiffré</li>
                <li>Le rôle par défaut est USER</li>
            </ul>
        </div>
    </div>
</body>
</html>