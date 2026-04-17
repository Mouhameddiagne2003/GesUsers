package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe utilitaire pour le chiffrement des mots de passe
 * Utilise SHA-256 avec salt pour sécuriser les mots de passe
 * Note: Dans un projet professionnel, utilisez BCrypt (library externe)
 */
public class PasswordEncoder {
	
	private static final String ALGORITHM = "SHA-256";
	private static final int SALT_LENGTH = 16;
	
	/**
	 * Générer un salt aléatoire
	 * @return Le salt encodé en Base64
	 */
	private static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
	
	/**
	 * Hasher un mot de passe avec un salt
	 * @param password Le mot de passe en clair
	 * @param salt Le salt à utiliser
	 * @return Le hash du mot de passe
	 */
	private static String hashWithSalt(String password, String salt) {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(salt.getBytes());
			byte[] hashedPassword = md.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(hashedPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
		}
	}
	
	/**
	 * Encoder un mot de passe (hash + salt)
	 * Format: salt:hash
	 * @param password Le mot de passe en clair
	 * @return Le mot de passe encodé avec son salt
	 */
	public static String encode(String password) {
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
		}
		
		String salt = generateSalt();
		String hash = hashWithSalt(password, salt);
		return salt + ":" + hash;
	}
	
	/**
	 * Vérifier si un mot de passe correspond au hash stocké
	 * @param password Le mot de passe en clair à vérifier
	 * @param encodedPassword Le mot de passe encodé (salt:hash)
	 * @return true si le mot de passe correspond
	 */
	public static boolean matches(String password, String encodedPassword) {
		if (password == null || encodedPassword == null) {
			return false;
		}
		
		String[] parts = encodedPassword.split(":");
		if (parts.length != 2) {
			return false;
		}
		
		String salt = parts[0];
		String storedHash = parts[1];
		String computedHash = hashWithSalt(password, salt);
		
		return storedHash.equals(computedHash);
	}
	
	/**
	 * Vérifier si un mot de passe est déjà encodé
	 * @param password Le mot de passe à vérifier
	 * @return true si le mot de passe est encodé
	 */
	public static boolean isEncoded(String password) {
		if (password == null) {
			return false;
		}
		// Un mot de passe encodé contient ":" et fait plus de 40 caractères
		return password.contains(":") && password.length() > 40;
	}
}
