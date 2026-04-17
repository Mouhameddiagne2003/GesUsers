<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="beans.Utilisateur" %>
<%
    Utilisateur utilisateur = (Utilisateur) request.getAttribute("utilisateur");
    Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier un utilisateur - GesUsers</title>
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
        <h1 class="mb-3">Modifier l'utilisateur #<%= utilisateur.getId() %></h1>

        <div class="card" style="max-width: 600px;">
            <form action="<%= request.getContextPath() %>/update" method="post">
                <input type="hidden" name="id" value="<%= utilisateur.getId() %>">

                <div class="form-group">
                    <label class="form-label" for="nom">Nom *</label>
                    <input type="text" name="nom" id="nom" class="form-input" 
                           value="<%= utilisateur.getNom() %>" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prenom">Prénom *</label>
                    <input type="text" name="prenom" id="prenom" class="form-input" 
                           value="<%= utilisateur.getPrenom() %>" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="login">Login *</label>
                    <input type="text" name="login" id="login" class="form-input" 
                           value="<%= utilisateur.getLogin() %>" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Mot de passe (optionnel)</label>
                    <input type="password" name="password" id="password" class="form-input" 
                           placeholder="Laissez vide pour conserver le mot de passe actuel">
                    <small style="color: var(--text-light);">
                        Entrez un nouveau mot de passe uniquement si vous voulez le changer.
                    </small>
                </div>

                <div style="display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-success">
                        Enregistrer les modifications
                    </button>
                    <a href="<%= request.getContextPath() %>/list" class="btn" style="background: #6b7280; color: white;">
                        ← Annuler
                    </a>
                </div>
            </form>
        </div>

        <div class="alert alert-info" style="max-width: 600px; margin-top: 1rem;">
            <strong>Informations :</strong>
            <ul style="margin: 0.5rem 0 0 1.5rem;">
                <li>ID de l'utilisateur : <strong>#<%= utilisateur.getId() %></strong></li>
                <li>Rôle actuel : <span class="badge <%= utilisateur.isAdmin() ? "badge-admin" : "badge-user" %>"><%= utilisateur.getRole() %></span></li>
                <li>Le mot de passe sera rechiffré si vous le modifiez</li>
            </ul>
        </div>
    </div>
</body>
</html>