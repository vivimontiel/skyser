package skyser.objects;

public class Reservation{
	private String nom, prenom, photo, id_book;
	private int nbr_places;
	
	public Reservation(String nom, String prenom, String photo, int places, String id_book) {
		this.nom = nom;
		this.prenom = prenom;
		this.photo = photo;
		this.nbr_places = places;
		this.id_book = id_book;
	}
	
	public Reservation() {}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getNbr_places() {
		return nbr_places;
	}

	public void setNbr_places(int nbr_places) {
		this.nbr_places = nbr_places;
	}

	public String getId_book() {
		return id_book;
	}

	public void setId_book(String id_book) {
		this.id_book = id_book;
	}
}
