package gestUtil.dto;

import bdd.InteractionBDDUtilisateur;

import java.sql.SQLException;
import java.util.ArrayList;

public class UtilisateurDTO  {
    //@Id
    private String email;
    private String pseudo;

    private int id;

    private String bio,status;

    private String photoDeProfil; // byte[] ???

    private ArrayList<String> listeContact;


    public UtilisateurDTO() {
    }

    public static void creerCompte(String pseudo, String email, String bio, String photoDeProfil) throws SQLException {
        InteractionBDDUtilisateur.creerCompteSQL(pseudo,email,bio,photoDeProfil);
    }

    public static void changerPseudo(String email, String pseudo) throws SQLException {
        InteractionBDDUtilisateur.changerPseudoSQL(email,pseudo);

    }

    public static void changementBio(String email, String nouvelleBio) throws SQLException {
        InteractionBDDUtilisateur.changerBioSQL(email,nouvelleBio);
    }

    public static void changerPhotoDeProfil(String email, String nouvellePhotoDeProfil) throws SQLException {
        InteractionBDDUtilisateur.changerPhotoDeProfilSQL(email,nouvellePhotoDeProfil);
    }

    public static void supprimerCompte(String email) throws SQLException {
        InteractionBDDUtilisateur.supprimerCompteSQL(email);
    }

    public static UtilisateurDTO getInformationsPubliques(String pseudo) throws SQLException {
        UtilisateurDTO userDTO = InteractionBDDUtilisateur.getInformationsPubliquesSQL(pseudo);
        userDTO.setListeContact(InteractionBDDUtilisateur.getListeContactByPseudo(pseudo));
        return userDTO;
    }

    public static void ajoutContact(String pseudo1, String pseudo2) throws SQLException {
        InteractionBDDUtilisateur.ajoutContactSQL(pseudo1,pseudo2);
    }

    public ArrayList<String> getListeContact() {
        return listeContact;
    }

    public void setListeContact(ArrayList<String> listeContact) {
        this.listeContact = listeContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
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


}
