package beans;

public class Utilisateur {
	private int id;
	private String nom, prenom, login, password;
	private String role; // ADMIN ou USER

	public Utilisateur() {

	}

	public Utilisateur(int id, String nom, String prenom, String login, String password, String role) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.password = password;
		this.role = role;
	}
	
	public Utilisateur(int id, String nom, String prenom, String login, String password) {
		this(id, nom, prenom, login, password, "USER");
	}

	public Utilisateur(String nom, String prenom, String login, String password) {
		this(0, nom, prenom, login, password, "USER");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isAdmin() {
		return "ADMIN".equals(role);
	}

}
