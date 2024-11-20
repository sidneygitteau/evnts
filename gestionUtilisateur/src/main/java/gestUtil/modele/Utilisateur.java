package gestUtil.modele;

import java.util.List;

//@AllArgsConstructor
//@Entity
public class Utilisateur  {
    //@Id
    private String email;
    private String pseudo;

    private String bio;

    private String photoDeProfil; // byte[] ???

    private List<String> listeContacts;

    public Utilisateur(String email, String pseudo, String bio, String photoDeProfil, List<String> listeContacts) {
        this.email = email;
        this.pseudo = pseudo;
        this.bio = bio;
        this.photoDeProfil = photoDeProfil;
        this.listeContacts = listeContacts;
    }

    public String getEmail() {
        return email;
    }



    public String getPseudo() {
        return pseudo;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoDeProfil() {
        return photoDeProfil;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhotoDeProfil(String photoDeProfil) {
        this.photoDeProfil = photoDeProfil;
    }

    public List<String> getListeContacts() {
        return listeContacts;
    }

    public void setListeContacts(List<String> listeContacts) {
        this.listeContacts = listeContacts;
    }

    public void ajouterPseudoListeContact(String pseudo){
        this.listeContacts.add(pseudo);
    }
}
