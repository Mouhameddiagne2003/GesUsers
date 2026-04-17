package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.Utilisateur;
import config.DatabaseConnection;

/**
 * DAO pour la gestion des utilisateurs avec base de données MySQL
 * Utilise des PreparedStatement pour éviter les injections SQL
 */
public class UtilisateurDao {

	/**
	 * Ajouter un nouvel utilisateur dans la base de données
	 * @param utilisateur L'utilisateur à ajouter
	 * @return true si l'ajout réussit, false sinon
	 */
	public static boolean ajouter(Utilisateur utilisateur) {
		String sql = "INSERT INTO utilisateurs (nom, prenom, login, password, role) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setString(1, utilisateur.getNom());
			stmt.setString(2, utilisateur.getPrenom());
			stmt.setString(3, utilisateur.getLogin());
			stmt.setString(4, utilisateur.getPassword());
			stmt.setString(5, utilisateur.getRole() != null ? utilisateur.getRole() : "USER");
			
			int rowsAffected = stmt.executeUpdate();
			
			// Récupérer l'ID généré automatiquement
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						utilisateur.setId(generatedKeys.getInt(1));
					}
				}
				return true;
			}
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Modifier un utilisateur existant
	 * @param utilisateur L'utilisateur avec les nouvelles données
	 * @return true si la modification réussit, false sinon
	 */
	public static boolean modifier(Utilisateur utilisateur) {
		String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, login = ?, password = ?, role = ? WHERE id = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, utilisateur.getNom());
			stmt.setString(2, utilisateur.getPrenom());
			stmt.setString(3, utilisateur.getLogin());
			stmt.setString(4, utilisateur.getPassword());
			stmt.setString(5, utilisateur.getRole() != null ? utilisateur.getRole() : "USER");
			stmt.setInt(6, utilisateur.getId());
			
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Supprimer un utilisateur par son ID
	 * @param id L'ID de l'utilisateur à supprimer
	 * @return true si la suppression réussit, false sinon
	 */
	public static boolean supprimer(int id) {
		String sql = "DELETE FROM utilisateurs WHERE id = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, id);
			
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Récupérer tous les utilisateurs
	 * @return Liste de tous les utilisateurs
	 */
	public static ArrayList<Utilisateur> lister() {
		ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
		String sql = "SELECT * FROM utilisateurs ORDER BY id DESC";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			
			while (rs.next()) {
				Utilisateur u = new Utilisateur(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("password"),
					rs.getString("role")
				);
				utilisateurs.add(u);
			}
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération de la liste : " + e.getMessage());
			e.printStackTrace();
		}
		
		return utilisateurs;
	}

	/**
	 * Récupérer un utilisateur par son ID
	 * @param id L'ID de l'utilisateur recherché
	 * @return L'utilisateur trouvé ou null
	 */
	public static Utilisateur get(int id) {
		String sql = "SELECT * FROM utilisateurs WHERE id = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, id);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Utilisateur(
						rs.getInt("id"),
						rs.getString("nom"),
						rs.getString("prenom"),
						rs.getString("login"),
						rs.getString("password"),
						rs.getString("role")
					);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Vérifier si un login existe déjà (utile pour éviter les doublons)
	 * @param login Le login à vérifier
	 * @return true si le login existe, false sinon
	 */
	public static boolean loginExists(String login) {
		String sql = "SELECT COUNT(*) FROM utilisateurs WHERE login = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, login);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la vérification du login : " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Récupérer un utilisateur par son login (pour authentification)
	 * @param login Le login de l'utilisateur
	 * @return L'utilisateur trouvé ou null
	 */
	public static Utilisateur getByLogin(String login) {
		String sql = "SELECT * FROM utilisateurs WHERE login = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, login);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Utilisateur(
						rs.getInt("id"),
						rs.getString("nom"),
						rs.getString("prenom"),
						rs.getString("login"),
						rs.getString("password"),
						rs.getString("role")
					);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération de l'utilisateur par login : " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
