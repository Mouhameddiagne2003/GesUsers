# GesUsers

Application web Java (Servlets/JSP) de gestion des utilisateurs.

## Prerequis

- JDK 8+
- Apache Tomcat 9+ (ou serveur compatible Jakarta/Servlet selon votre version)
- Base de donnees MySQL/MariaDB

## Structure du projet

```text
src/main/java/
  beans/
  config/
  dao/
  filters/
  services/
  servlets/
  utils/
src/main/webapp/
  WEB-INF/
  css/
```

## Configuration de l'environnement

Un fichier `.env` est fourni a la racine du projet.
Renseignez vos valeurs avant de lancer l'application.

Variables disponibles :

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `APP_PORT`
- `APP_ENV`

## Build et execution

1. Compiler le projet (IDE ou build tool configure dans votre environnement).
2. Deployer l'application sur Tomcat.
3. Ouvrir l'application via l'URL du serveur (ex: `http://localhost:8080/gesusers`).

## Pages principales


- `/login`
- `/dashboard`
- `/list`
- `/add`
- `/update`

## Fonctionnalites

- Authentification utilisateur
- Tableau de bord
- Ajout, modification, suppression d'utilisateurs
- Validation des donnees et encodage des mots de passe

## Notes

- Ne committez pas de secrets reels dans `.env`.
- Adaptez la configuration de connexion selon votre base locale.
