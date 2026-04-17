package utils;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour valider les données utilisateur
 * Contient des méthodes de validation réutilisables
 */
public class Validator {
	
	// Patterns regex
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
	);
	
	private static final Pattern LOGIN_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9_]{3,20}$"
	);
	
	private static final Pattern NOM_PRENOM_PATTERN = Pattern.compile(
		"^[a-zA-ZÀ-ÿ\\s'-]{2,50}$"
	);
	
	/**
	 * Vérifier si une chaîne est vide ou null
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	/**
	 * Valider un nom ou prénom
	 * - Minimum 2 caractères
	 * - Maximum 50 caractères
	 * - Lettres, espaces, apostrophes et tirets uniquement
	 */
	public static boolean isValidNomPrenom(String nomPrenom) {
		if (isNullOrEmpty(nomPrenom)) {
			return false;
		}
		return NOM_PRENOM_PATTERN.matcher(nomPrenom.trim()).matches();
	}
	
	/**
	 * Valider un login
	 * - Minimum 3 caractères
	 * - Maximum 20 caractères
	 * - Lettres, chiffres et underscore uniquement
	 */
	public static boolean isValidLogin(String login) {
		if (isNullOrEmpty(login)) {
			return false;
		}
		return LOGIN_PATTERN.matcher(login.trim()).matches();
	}
	
	/**
	 * Valider un email
	 */
	public static boolean isValidEmail(String email) {
		if (isNullOrEmpty(email)) {
			return false;
		}
		return EMAIL_PATTERN.matcher(email.trim()).matches();
	}
	
	/**
	 * Valider la force d'un mot de passe
	 * - Minimum 8 caractères
	 * - Au moins une lettre majuscule
	 * - Au moins une lettre minuscule
	 * - Au moins un chiffre
	 * - Au moins un caractère spécial
	 */
	public static boolean isStrongPassword(String password) {
		if (isNullOrEmpty(password) || password.length() < 8) {
			return false;
		}
		
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasDigit = false;
		boolean hasSpecial = false;
		
		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) hasUpper = true;
			else if (Character.isLowerCase(c)) hasLower = true;
			else if (Character.isDigit(c)) hasDigit = true;
			else hasSpecial = true;
		}
		
		return hasUpper && hasLower && hasDigit && hasSpecial;
	}
	
	/**
	 * Valider un mot de passe (version simple)
	 * - Minimum 6 caractères
	 */
	public static boolean isValidPassword(String password) {
		return !isNullOrEmpty(password) && password.length() >= 6;
	}
	
	/**
	 * Obtenir un message d'erreur pour un mot de passe faible
	 */
	public static String getPasswordStrengthError(String password) {
		if (isNullOrEmpty(password)) {
			return "Le mot de passe est obligatoire";
		}
		if (password.length() < 8) {
			return "Le mot de passe doit contenir au moins 8 caractères";
		}
		if (!password.matches(".*[A-Z].*")) {
			return "Le mot de passe doit contenir au moins une majuscule";
		}
		if (!password.matches(".*[a-z].*")) {
			return "Le mot de passe doit contenir au moins une minuscule";
		}
		if (!password.matches(".*\\d.*")) {
			return "Le mot de passe doit contenir au moins un chiffre";
		}
		if (!password.matches(".*[^a-zA-Z0-9].*")) {
			return "Le mot de passe doit contenir au moins un caractère spécial";
		}
		return null;
	}
	
	/**
	 * Nettoyer une chaîne (trim et échapper caractères dangereux)
	 */
	public static String sanitize(String input) {
		if (input == null) {
			return null;
		}
		return input.trim()
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#x27;");
	}
}
