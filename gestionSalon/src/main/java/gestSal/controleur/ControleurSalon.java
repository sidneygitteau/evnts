package gestSal.controleur;

import gestSal.dto.EvenementDTO;
import gestSal.dto.MessageDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.*;
import gestSal.modele.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/salon")
public class ControleurSalon {

    final
    FacadeSalon facadeSalon;

    public ControleurSalon(FacadeSalon facadeSalon) {
        this.facadeSalon = facadeSalon;
    }


    @PostMapping(value = "")
    public ResponseEntity<Object> creerSalon(@RequestParam String nomCreateur, @RequestParam String nomSalon) {
        try {
            Salon salon = facadeSalon.creerSalon(nomCreateur, nomSalon);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/salon/{nomDuSalon}")
                    .buildAndExpand(salon.getNomSalon())
                    .toUri();

            return ResponseEntity.created(location).body(salon);
        } catch (NomSalonVideException | NomTropCourtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création du salon : " + e.getMessage());
        }catch(NumSalonDejaExistantException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur lors de la création du salon : " + e.getMessage());
        }
    }


    @PatchMapping("/{numSalon}")
    public ResponseEntity<Object> modifierSalon(@PathVariable int numSalon, @RequestBody SalonModificationRequest modifs) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            salon = facadeSalon.modifierSalon(salon, modifs.getChoix(), modifs.getValeur());
            return ResponseEntity.ok(salon);
        } catch (SalonInexistantException | NomSalonVideException | NumeroSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la modification du salon : " + e.getMessage());
        }
    }


    @GetMapping("/{numSalon}")
    public ResponseEntity<Object> getSalonByNum(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            return ResponseEntity.ok(salon);
        } catch (NumeroSalonVideException | SalonInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la récupération du salon : " + e.getMessage());
        }

    }

    @GetMapping("/{nomSalon}")
    public ResponseEntity<Object> getSalonByNom(@PathVariable String nomSalon){
        try{
            Salon salon = facadeSalon.getSalonByNom(nomSalon);
            if(salon==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            return ResponseEntity.ok(salon);

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/utilisateur/{pseudoUtilisateur}")
    public ResponseEntity<Object> getUtilisateurByPseudo(@PathVariable String pseudoUtilisateur) {
        Utilisateur utilisateur;
        try {
            utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
        } catch (NomUtilisateurVideException | UtilisateurInexistantException e) {
            throw new RuntimeException(e);
        }
        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
        }
        return ResponseEntity.ok(utilisateur);
    }

    @PatchMapping("/{id}/evenement/{idEvent}")
    public ResponseEntity<Object> modifierEvenement(@PathVariable int id, @PathVariable int idEvent, @RequestBody SalonModificationRequest modifs) {
        try {
            String nomEvent = facadeSalon.getNomEvenementById(idEvent);
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id, nomEvent);

            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            evenement = facadeSalon.modifierEvenement(id,evenement, modifs.getChoix(),modifs.getValeur());

            return ResponseEntity.ok("Événement modifié avec succès : " + evenement);
        } catch (EvenementInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la modification de l'événement : " + e.getMessage());
        }
    }



    @PostMapping("{numSalon}/evenement/{idEvent}/presence")
    public ResponseEntity<Object> seDefiniCommePresentAUnEvenement(@RequestParam String nomUtilisateur, @PathVariable int idEvent, @PathVariable int numSalon) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);

            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);

            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("{numSalon}/evenement/{idEvent}/presence")
    public ResponseEntity<Object> seDefiniCommeAbsentAUnEvenement(@PathVariable int numSalon, @PathVariable int idEvent, @RequestParam String nomUtilisateur) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("{numSalon}/evenement")
    public ResponseEntity<Object> creerEvenement(@PathVariable int numSalon, @RequestBody EvenementDTO evenementDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            Utilisateur createur = facadeSalon.getUtilisateurByPseudo(evenementDTO.getNomCreateur());
            Evenement evenement = facadeSalon.creerEvenement(salon, evenementDTO.getNomEvenement(), evenementDTO.getNombrePersonneMax(), evenementDTO.getDetailsEvenement(), evenementDTO.getLieu(), createur, evenementDTO.getDate());
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(evenement.getIdEvenement()  )
                    .toUri();
            return ResponseEntity.created(location).body(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/evenement/{idEvent}")
    public ResponseEntity<Object> getEvenementByNomEtNumSalon(@PathVariable int numSalon, @PathVariable int idEvent)   {
        String nomEvenement = facadeSalon.getNomEvenementById(idEvent);
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            return ResponseEntity.ok(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PatchMapping("{numSalon}/evenement/{idEvent}/validation")
    public ResponseEntity<Object> validerEvenement(@PathVariable int numSalon, @PathVariable int idEvent) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            boolean isValide = facadeSalon.validerEvenement(evenement);


            return ResponseEntity.ok(isValide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PostMapping("{numSalon}/messages")
    public ResponseEntity<Object> envoyerMessageSalon(@PathVariable int numSalon, @RequestBody MessageDTO messageDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            facadeSalon.envoyerMessageSalon(salon, messageDTO.getAuteur(), messageDTO.getContenu());
            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("{numSalon}/evenement/{idEvent}/messages")
    public ResponseEntity<Object> envoyerMessageEvenement(@PathVariable int numSalon, @PathVariable int idEvent,@RequestBody MessageDTO messageDTO) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvent);
                   if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }
            facadeSalon.envoyerMessageEvenement(evenement,messageDTO.getAuteur(), messageDTO.getContenu());

            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/messages")
    public ResponseEntity<Object> getMessagesSalon(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            List<Message> messages = facadeSalon.getMessagesSalon(salon);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("{numSalon}/evenement/{idEvent}/messages")
    public ResponseEntity<Object> getMessagesEvenement(@PathVariable int numSalon,@PathVariable int idEvent) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement;
            evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            List<Message> messages = facadeSalon.getMessagesEvenement(evenement);

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    //TODO BACK ?
    @PostMapping("{numSalon}/invitation")
    public ResponseEntity<Object> inviterUtilisateur(@RequestBody String pseudoUtilisateur, @PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            String invitationUrl = facadeSalon.inviterUtilisateur(salon, utilisateur);
            return ResponseEntity.ok("Invitation envoyée. URL: " + invitationUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PostMapping("{numSalon}/adhesion")
    public ResponseEntity<?> rejoindreSalon(@PathVariable int numSalon, @RequestParam String pseudoUtilisateur){
        try{
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            facadeSalon.rejoindreSalon(utilisateur,salon);

            return ResponseEntity.ok(salon);
        }catch(UtilisateurDejaPresentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Utilisateur deja présent dans salon");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable String nomUtilisateur){
        try {
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            facadeSalon.supprimerUtilisateur(utilisateur);
            return ResponseEntity.noContent().build();
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> getUtilisateur(@PathVariable String nomUtilisateur){
        try {
            Utilisateur user = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            return ResponseEntity.ok(user);
        } catch (NomUtilisateurVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/{numSalon}/moderation")
    public ResponseEntity<?> ajouterModerateur(@PathVariable int numSalon, @RequestParam String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);
            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }

            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if(salon==null){
                throw new SalonInexistantException();
            }
            facadeSalon.ajouterModerateurAuSalon(utilisateur,salon);

            return ResponseEntity.ok(salon);
        } catch (NomUtilisateurVideException | NumeroSalonVideException | NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{numSalon}/moderation")
    public ResponseEntity<?> supprimerModerateur(@PathVariable int numSalon, @RequestParam String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);

            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if(salon==null){
                throw new SalonInexistantException();
            }
            facadeSalon.retirerModerateurDuSalon(salon,utilisateur);

            return ResponseEntity.ok(salon);
        } catch (NomUtilisateurVideException | NumeroSalonVideException | NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/utilisateur/{nomUtilisateur}/salons")
    public ResponseEntity<?> recupererSalonsDeLutilisateur(@PathVariable String nomUtilisateur) {
        try{
            return ResponseEntity.ok(facadeSalon.getSalonByUser(nomUtilisateur));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NomUtilisateurVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/utilisateur/{nomUtilisateur}/evenements")
    public ResponseEntity<?> recupererEventDeLutilisateur(@PathVariable String nomUtilisateur) {
        try{
            return ResponseEntity.ok(facadeSalon.getEvenementsUser(nomUtilisateur));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NomUtilisateurVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("/utilisateur/{nomMembre}")
    public ResponseEntity<?> ajouterMembre(@PathVariable String nomMembre, @RequestParam String email){
        try{
            facadeSalon.ajouterMembre(nomMembre,email);
            return ResponseEntity.created(null).body("Membre ajouté");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Echec de l'ajout");
        }
    }

    @GetMapping("/utilisateurs/mail/{email}")
    public ResponseEntity<?> getUtilisateurByEmail(@PathVariable String email){
        try{
            Utilisateur user = facadeSalon.getUtilisateurByEmail(email);
            return ResponseEntity.ok().body(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Echec de la récupération");
        }
    }


}
