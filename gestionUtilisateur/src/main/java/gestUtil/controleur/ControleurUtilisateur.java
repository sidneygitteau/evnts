package gestUtil.controleur;

import gestUtil.dto.ErreurDTO;
import gestUtil.dto.UtilisateurDTO;
import gestUtil.exceptions.EMailDejaPrisException;
import gestUtil.exceptions.PseudoDejaPrisException;
import gestUtil.exceptions.UtilisateurNonTrouveException;
import gestUtil.facade.IFacadeGestionUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/utilisateurs")
public class ControleurUtilisateur {

    @Autowired
    IFacadeGestionUtilisateur facadeUtilisateur;


    @PostMapping(value = "/inscription")
    public ResponseEntity<String> inscription(@RequestParam String pseudo, @RequestParam String email, @RequestParam String bio, @RequestParam String photoDeProfil ) {
        try {
            this.facadeUtilisateur.creerCompte(pseudo,email,bio,photoDeProfil);
            return ResponseEntity.created(null).body("Compte créé !");
        } catch (PseudoDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pseudo "+pseudo+" déjà pris");
        }catch (EMailDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email "+email+" déjà existant");
        }
    }

    @PatchMapping(value = "/changementPseudo")
    public ResponseEntity<String> changerPseudo(@RequestParam String email,
                                             @RequestParam String nouveauPseudo) {
        try {
            facadeUtilisateur.changerPseudo(email, nouveauPseudo);
            return ResponseEntity.ok("Pseudo changé avec succès !");
        } catch (UtilisateurNonTrouveException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (PseudoDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping(value = "/changementBio")
    public ResponseEntity<String> changementBio(@RequestParam String email,
                                                @RequestParam String nouvelleBio) {
        try {
            facadeUtilisateur.changerBio(email, nouvelleBio);
            return ResponseEntity.ok("Bio mise à jour avec succès !");
        } catch (UtilisateurNonTrouveException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping(value = "/changementPhotoDeProfil")
    public ResponseEntity<String> changementPhotoDeProfil(@RequestParam String email,
                                                          @RequestParam String nouvellePhotoDeProfil) {
        try {
            facadeUtilisateur.changerPhotoDeProfil(email, nouvellePhotoDeProfil);
            return ResponseEntity.ok("Photo de profil mise à jour avec succès !");
        } catch (UtilisateurNonTrouveException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/suppressionCompte")
    public ResponseEntity<String> suppressionCompte(@RequestParam String email) {
        try {
            facadeUtilisateur.supprimerCompte(email);
            return ResponseEntity.ok("Compte supprimé avec succès !");
        } catch (UtilisateurNonTrouveException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value="{pseudo}")
    public ResponseEntity<?> informationCompte(@PathVariable String pseudo){
        try{
            UtilisateurDTO user = facadeUtilisateur.getInformationsPubliques(pseudo);
            return ResponseEntity.ok(user);
        }catch(UtilisateurNonTrouveException e){
            ErreurDTO erreurDTO = new ErreurDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreurDTO);
        }
    }

    @PostMapping(value="/ajoutContact")
    public ResponseEntity<String> ajoutContact(@RequestParam String pseudo1,@RequestParam String pseudo2){
        try{
            facadeUtilisateur.ajoutContact(pseudo1,pseudo2);
            return ResponseEntity.ok("Contact bien ajouté !");
        }catch(UtilisateurNonTrouveException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
