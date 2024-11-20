package com.example.gestiongoogle.controleur;

import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.EmailDejaPritException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import com.example.gestiongoogle.facade.FacadeGoogleImpl;

import com.google.api.services.calendar.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping(value="/google")
public class GoogleControleur {

    @Autowired
    FacadeGoogleImpl facadeGoogle;

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;


    public GoogleControleur(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public String home() {
        //TODO : pas très propre, mais c'est juste direct pour envoyer vers /login
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Accueil</title></head>" +
                "<body>" +
                "<h1>Ici c'est l'accueil, pas besoin d'être log</h1>" +
                "<form action='/login' method='get'>" +
                "<input type='submit' value='Se connecter'>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/secure")
    public String secured(Authentication authentication, HttpServletRequest request) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        //TODO : verifier si utilisateur existant : si oui on reste sur secured, sinon on envoie au formulaire de creation
        //TODO : ne fonctionne pas pour le moment ? a voir BDD
        /*
        boolean utilisateurExistant = facadeGoogle.verifierUtilisateurExistant(oidcUser.getEmail());
        if(!utilisateurExistant){
            return "redirect:/google/newAccount";
        }*/

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        //String token = oidcUser.getAccessTokenHash();

        // Récupérer le jeton CSRF IMPORTANT : IL FAUT QU'IL FASSE PARTIE DE LA REQUETE !!!!!!
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        return "<!DOCTYPE html>" +
                "<form action='/google/eventLink' method='post'>" +
                "Titre: <input type='text' name='nom'><br>" +
                "Emplacement: <input type='text' name='location'><br>" +
                "Début: <input type='datetime-local' name='debut'><br>" +
                "Fin: <input type='datetime-local' name='fin'><br>" +
                "Description: <textarea name='description'></textarea><br>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "<input type='submit' value='Ajouter Événement'>" +
                "</form>" +
                "<form action='/google/emailValidationEvenement' method='get'>" +
                "<input type='submit' value='Envoyer un mail'>" +
                "</form>" +
                "</form>" +
                "<form action='/google/events' method='post'>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "Début: <input type='datetime-local' name='dateDebut'><br>" +
                "Fin: <input type='datetime-local' name='dateFin'><br>" +
                "<input type='submit' value='Recuperer Les Evenements'>" +
                "</form>" +
                 "</body>" +
                "</html>";
    }

    @GetMapping("/creation")
    public String newAccount(Authentication authentication,HttpServletRequest request){
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        // Récupérer le jeton CSRF IMPORTANT : IL FAUT QU'IL FASSE PARTIE DE LA REQUETE !!!!!!
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Création d'Utilisateur</title>" +
                "</head>" +
                "<body>" +
                "<form action='/google/new-user' method='post'>" +
                "Email: <input type='email' name='email' value="+email+ "readonly required><br>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "<input type='submit' value='Créer Utilisateur'>" +
                "</form>" +
                "</body>" +
                "</html>";
    }


    @PostMapping("/utilisateur/{email}")
    public ResponseEntity<String> newUser(@PathVariable String email) {
        try {
            facadeGoogle.newUtilisateur(email);
            return ResponseEntity.created(null).body("Compte créé !");
        }
        catch (EmailDejaPritException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Erreur Utilisateur Existant");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur");
        }
    }

    @DeleteMapping("/utilisateur")
    public ResponseEntity<String> deleteUser(@RequestParam String email){
        try{
            facadeGoogle.deleteUtilisateur(email);
            return ResponseEntity.ok("Utilisateur supprimé");
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur");
        }

    }

    @PostMapping("/event")
    public ResponseEntity<String> addEvent(Authentication authentication,
                                   @RequestParam String nom,
                                   @RequestParam String location,
                                   @RequestParam String debut,
                                   @RequestParam String fin,
                                   @RequestParam String description) throws GeneralSecurityException, IOException {
        try {
            facadeGoogle.ajouterEvenementCalendrier(authentication, nom, location, debut, fin, description);

            return ResponseEntity.ok("Evenement ajoute");
        }catch(AucunCalendrierTrouveException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Aucun google calendar trouve");
        } catch (DateFinEvenementInvalideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Date debut et fin invalide");
        }

    }

    @PostMapping("/eventLink")
    public ResponseEntity<String> addEventLink(@RequestParam String nom,
                                           @RequestParam String location,
                                           @RequestParam String debut,
                                           @RequestParam String fin,
                                           @RequestParam String description) {
        try {
            String lien = facadeGoogle.ajouterEvenementCalendrierLink(nom, location, debut, fin, description);

            return ResponseEntity.ok(lien);
        }catch (DateFinEvenementInvalideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Date debut et fin invalide");
        }

    }
    @PostMapping("/events")
    public ResponseEntity<String> getEvents(Authentication authentication,
                                            @RequestParam String dateDebut,
                                            @RequestParam String dateFin){
        try{
            List<Event> listeEvenement = facadeGoogle.listerEvenements(authentication,dateDebut,dateFin);

            return ResponseEntity.ok(listeEvenement.toString());
        } catch (GeneralSecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Probleme Recuperation Evenements Securite");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme Recuperation Evenements");
        }
    }

    @GetMapping("/validation-mail")
    public ResponseEntity<String> sendEmail(Authentication authentication) {
        try{
            facadeGoogle.envoyerMailValidationEvenement(authentication);

            return ResponseEntity.ok("Mail Envoye");
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme envoi mail");
        }

    }

    @GetMapping("/email")
    public ResponseEntity<String> sendEmailAvecContenu(String email, String objet, String contenu){
        try{
            facadeGoogle.envoyerMailParContenu(email,objet,contenu);

            return ResponseEntity.ok("Mail Envoue");
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme envoi mail");
        }
    }

}
