package gestSal.dto;


import bdd.InteractionBDDSalon;
import gestSal.facade.erreurs.UtilisateurDejaModoException;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class SalonDTO {
    public SalonDTO() {
    }

    public static void creerSalonSQL(String nomSalon, String nomCreateur, String url) {
        try {
            InteractionBDDSalon.creerSalonSQL(nomSalon,nomCreateur,url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Salon getSalonById(int numSalon) throws SQLException {
        return InteractionBDDSalon.getSalonById(numSalon);
    }

    public static Salon modifierSalonSQL(Salon salon,String choix, String valeur, int numSalon) throws SQLException {
        return InteractionBDDSalon.modifierSalonSQL(salon,choix,valeur, numSalon);
    }

    public static Salon getSalonByName(String nomSalon) throws SQLException {
        return InteractionBDDSalon.getSalonByName(nomSalon);
    }

    public static void rejoindreSalon(Utilisateur utilisateur, Salon salonRejoint) throws SQLException {
        InteractionBDDSalon.rejoindreSalonSql(utilisateur, salonRejoint);
    }

    public static void retirerModerateurDuSalon(Salon salon, Utilisateur utilisateurPlusModo) throws SQLException {
        InteractionBDDSalon.retirerModerateurDuSalonSQL(salon, utilisateurPlusModo);
    }

    public static void ajouterModerateurAuSalon(Utilisateur nouveauModo, Salon salonPourLeNouveauModo) throws SQLException, UtilisateurDejaModoException {
        InteractionBDDSalon.ajouterModerateurAuSalon(nouveauModo, salonPourLeNouveauModo);
    }

    public static Evenement creerEvenement(Salon salon, String nomEvenement, int nombrePersonneMax, String detailsEvenement, String lieu, Utilisateur createur, String date) throws SQLException {
        InteractionBDDSalon.creerEvenement(salon,nomEvenement,nombrePersonneMax,detailsEvenement,lieu,createur,date);
        return InteractionBDDSalon.getEvenementByNomEtNumSalonSQL(salon.getIdSalon(),nomEvenement);
    }

    public static void envoyerMessageSalon(int idSalon, String pseudoUtilisateur, String contenu, String dateTime) throws SQLException {
        InteractionBDDSalon bdd = new InteractionBDDSalon();
        bdd.envoyerMessageSalonSQL(idSalon,pseudoUtilisateur,contenu,dateTime);
    }

    public static void envoyerMessageEvenement(int idEvenement, String pseudoUtilisateur, String contenu, String dateTime) throws SQLException {
        InteractionBDDSalon bdd = new InteractionBDDSalon();
        bdd.envoyerMessageEventSQL(idEvenement,pseudoUtilisateur,contenu,dateTime);
    }

    public static List<Message> getMessagesSalon(Salon salon) throws SQLException {
        return InteractionBDDSalon.getMessageSalonSQL(salon.getIdSalon());
    }

}
