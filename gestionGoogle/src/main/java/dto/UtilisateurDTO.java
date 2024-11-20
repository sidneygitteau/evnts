package dto;

import bdd.InteractionBDDGoogle;

import java.sql.SQLException;

public class UtilisateurDTO {
    String email;

    public static void deleteUtilisateur(String email) throws SQLException {
        InteractionBDDGoogle interactionBDDGoogle = new InteractionBDDGoogle();
        interactionBDDGoogle.deleteUtilisateur(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void ajouterUtilisateur(String email) throws SQLException {
        InteractionBDDGoogle interactionBDDGoogle = new InteractionBDDGoogle();
        interactionBDDGoogle.enregistrerUser(email);
    }
}
