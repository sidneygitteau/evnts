package gestSal.modele;

import java.util.List;

public class Utilisateur {
    private int idUtilisateur;
    private String pseudo,email;

    public Utilisateur(int idUtilisateur, String pseudo, String email) {
        this.idUtilisateur = idUtilisateur;
        this.pseudo = pseudo;
        this.email = email;
    }

    public Utilisateur() {
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
