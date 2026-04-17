<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - GesUsers</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h1 class="login-title">Gestion Utilisateurs</h1>
                <p class="login-subtitle">Connectez-vous pour continuer</p>
            </div>

            <% 
                String error = request.getParameter("error");
                String message = request.getParameter("message");
                if (error != null) {
            %>
                <div class="alert alert-error">
                    <%= escapeHtml(error) %>
                </div>
            <% } %>
            
            <% if (message != null) { %>
                <div class="alert alert-info">
                    <%= escapeHtml(message) %>
                </div>
            <% } %>
            
            <% 
                String errorAttr = (String) request.getAttribute("error");
                if (errorAttr != null) {
            %>
                <div class="alert alert-error">
                    <%= escapeHtml(errorAttr) %>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/login" method="post">
                <div class="form-group">
                    <label class="form-label" for="login">Login</label>
                    <input 
                        type="text" 
                        id="login" 
                        name="login" 
                        class="form-input" 
                        placeholder="Entrez votre login"
                        required
                        autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Mot de passe</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        class="form-input" 
                        placeholder="Entrez votre mot de passe"
                        required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">
                    Se connecter
                </button>
            </form>

            
        </div>
    </div>
</body>
</html>
