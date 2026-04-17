package services;

import java.util.ArrayList;

import beans.Utilisateur;
import dao.UtilisateurDao;
import utils.PasswordEncoder;
import utils.Validator;


public class UtilisateurService {
	
	/**
	 * Ajouter un utilisateur avec validation métier
	 * @param utilisateur L'utilisateur à ajouter
	 * @return true si succès, false sinon
	 * @throws IllegalArgumentException si les données sont invalides
	 */
	public boolean ajouterUtilisateur(Utilisateur utilisateur) {
		// Validation métier
		if (utilisateur == null) {
			throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
		}
		
		// Validation du nom
		if (!Validator.isValidNomPrenom(utilisateur.getNom())) {
			throw new IllegalArgumentException("Le nom est invalide (2-50 caractères, lettres uniquement)");
		}
		
		// Validation du prénom
		if (!Validator.isValidNomPrenom(utilisateur.getPrenom())) {
			throw new IllegalArgumentException("Le prénom est invalide (2-50 caractères, lettres uniquement)");
		}
		
		// Validation du login
		if (!Validator.isValidLogin(utilisateur.getLogin())) {
			throw new IllegalArgumentException("Le login est invalide (3-20 caractères, lettres/chiffres/underscore uniquement)");
		}
		
		// Validation du mot de passe
		if (!Validator.isValidPassword(utilisateur.getPassword())) {
			throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
		}
		
		// Vérifier si le login existe déjà
		if (UtilisateurDao.loginExists(utilisateur.getLogin())) {
			throw new IllegalArgumentException("Ce login existe déjà");
		}
		
		// Nettoyer les données
		utilisateur.setNom(Validator.sanitize(utilisateur.getNom()));
		utilisateur.setPrenom(Validator.sanitize(utilisateur.getPrenom()));
		utilisateur.setLogin(Validator.sanitize(utilisateur.getLogin()));
		
		// Encoder le mot de passe avant de sauvegarder
		utilisateur.setPassword(PasswordEncoder.encode(utilisateur.getPassword()));
		
		// Appeler le DAO pour ajouter
		return UtilisateurDao.ajouter(utilisateur);
	}
	
	/**
	 * Modifier un utilisateur existant
	 * @param utilisateur L'utilisateur avec les nouvelles données
	 * @return true si succès, false sinon
	 * @throws IllegalArgumentException si les données sont invalides
	 */
	public boolean modifierUtilisateur(Utilisateur utilisateur) {
		// Validation
		if (utilisateur == null) {
			throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
		}
		
		if (utilisateur.getId() <= 0) {
			throw new IllegalArgumentException("ID utilisateur invalide");
		}
		
		// Vérifier que l'utilisateur existe
		Utilisateur existant = UtilisateurDao.get(utilisateur.getId());
		if (existant == null) {
			throw new IllegalArgumentException("Utilisateur introuvable");
		}
		
		// Validation du nom
		if (!Validator.isValidNomPrenom(utilisateur.getNom())) {
			throw new IllegalArgumentException("Le nom est invalide (2-50 caractères, lettres uniquement)");
		}
		
		// Validation du prénom
		if (!Validator.isValidNomPrenom(utilisateur.getPrenom())) {
			throw new IllegalArgumentException("Le prénom est invalide (2-50 caractères, lettres uniquement)");
		}
		
		// Validation du login
		if (!Validator.isValidLogin(utilisateur.getLogin())) {
			throw new IllegalArgumentException("Le login est invalide (3-20 caractères, lettres/chiffres/underscore uniquement)");
		}
		
		// Validation du mot de passe
		if (!Validator.isValidPassword(utilisateur.getPassword())) {
			throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
		}
		
		// Nettoyer les données
		utilisateur.setNom(Validator.sanitize(utilisateur.getNom()));
		utilisateur.setPrenom(Validator.sanitize(utilisateur.getPrenom()));
		utilisateur.setLogin(Validator.sanitize(utilisateur.getLogin()));
		
		// Encoder le mot de passe si ce n'est pas déjà fait
		if (!PasswordEncoder.isEncoded(utilisateur.getPassword())) {
			utilisateur.setPassword(PasswordEncoder.encode(utilisateur.getPassword()));
		}
		
		return UtilisateurDao.modifier(utilisateur);
	}
	
	/**
	 * Supprimer un utilisateur par ID
	 * @param id L'ID de l'utilisateur
	 * @return true si succès, false sinon
	 * @throws IllegalArgumentException si l'ID est invalide
	 */
	public boolean supprimerUtilisateur(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("ID invalide");
		}
		
		// Vérifier que l'utilisateur existe
		Utilisateur utilisateur = UtilisateurDao.get(id);
		if (utilisateur == null) {
			throw new IllegalArgumentException("Utilisateur introuvable");
		}
		
		return UtilisateurDao.supprimer(id);
	}
	
	/**
	 * Récupérer tous les utilisateurs
	 * @return Liste des utilisateurs
	 */
	public ArrayList<Utilisateur> listerUtilisateurs() {
		return UtilisateurDao.lister();
	}
	
	/**
	 * Récupérer un utilisateur par ID
	 * @param id L'ID de l'utilisateur
	 * @return L'utilisateur ou null
	 * @throws IllegalArgumentException si l'ID est invalide
	 */
	public Utilisateur getUtilisateur(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("ID invalide");
		}
		
		return UtilisateurDao.get(id);
	}
	
	/**
	 * Vérifier si un login existe déjà
	 * @param login Le login à vérifier
	 * @return true si existe, false sinon
	 */
	public boolean loginExiste(String login) {
		if (login == null || login.trim().isEmpty()) {
			return false;
		}
		return UtilisateurDao.loginExists(login);
	}
	
	/**
	 * Authentifier un utilisateur (pour connexion future)
	 * @param login Le login
	 * @param password Le mot de passe en clair
	 * @return L'utilisateur si authentification réussie, null sinon
	 */
	public Utilisateur authenticate(String login, String password) {
		if (Validator.isNullOrEmpty(login) || Validator.isNullOrEmpty(password)) {
			return null;
		}
		
		// Récupérer tous les utilisateurs et chercher le bon login
		ArrayList<Utilisateur> utilisateurs = UtilisateurDao.lister();
		for (Utilisateur u : utilisateurs) {
			if (u.getLogin().equals(login)) {
				// Vérifier le mot de passe
				if (PasswordEncoder.matches(password, u.getPassword())) {
					return u;
				}
			}
		}
		
		return null;
	}
}
