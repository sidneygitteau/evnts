package com.example.gestiongoogle.facade;

import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.EmailDejaPritException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import com.google.api.services.calendar.model.Event;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

public interface IFacadeGoogle {

    /**
     * Liste les événements d'un calendrier Google entre deux dates.
     * @param authentication L'authentification de l'utilisateur.
     * @param dateDebut La date de début pour filtrer les événements.
     * @param dateFin La date de fin pour filtrer les événements.
     * @return Une liste d'événements.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     * @throws GeneralSecurityException Si une erreur de sécurité se produit.
     */
    List<Event> listerEvenements(Authentication authentication, String dateDebut, String dateFin) throws IOException, GeneralSecurityException;

    /**
     * Ajoute un événement au calendrier Google de l'utilisateur.
     * @param authentication L'authentification de l'utilisateur.
     * @param nom Le nom de l'événement.
     * @param location Le lieu de l'événement.
     * @param debut La date et heure de début de l'événement.
     * @param fin La date et heure de fin de l'événement.
     * @param description La description de l'événement.
     * @return Un lien vers l'événement créé.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     * @throws GeneralSecurityException Si une erreur de sécurité se produit.
     * @throws AucunCalendrierTrouveException Si aucun calendrier correspondant n'est trouvé.
     * @throws DateFinEvenementInvalideException Si la date de fin est avant la date de début.
     */
    String ajouterEvenementCalendrier(Authentication authentication, String nom, String location, String debut, String fin, String description) throws IOException, GeneralSecurityException, AucunCalendrierTrouveException, DateFinEvenementInvalideException;

    /**
     * Envoie un email de validation pour un événement créé.
     * @param authentication L'authentification de l'utilisateur.
     * @throws ProblemeEnvoiMailException Si une erreur se produit lors de l'envoi de l'email.
     */
    void envoyerMailValidationEvenement(Authentication authentication) throws ProblemeEnvoiMailException;

    /**
     * Envoie un email avec un contenu spécifique à un destinataire donné.
     * @param email L'adresse email du destinataire.
     * @param sujet Le sujet de l'email.
     * @param contenu Le contenu de l'email.
     * @throws ProblemeEnvoiMailException Si une erreur se produit lors de l'envoi de l'email.
     */
    void envoyerMailParContenu(String email, String sujet, String contenu) throws ProblemeEnvoiMailException;

    /**
     * Vérifie si un utilisateur est déjà enregistré.
     * @param email L'email de l'utilisateur à vérifier.
     * @return Vrai si l'utilisateur existe, faux sinon.
     */
    boolean verifierUtilisateurExistant(String email);

    /**
     * Enregistre un nouvel utilisateur.
     * @param email L'email de l'utilisateur.
     * @throws SQLException Si une erreur se produit lors de l'enregistrement dans la base de données.
     * @throws EmailDejaPritException Si l'email est déjà utilisé par un autre utilisateur.
     */
    void newUtilisateur(String email) throws SQLException, EmailDejaPritException;

}