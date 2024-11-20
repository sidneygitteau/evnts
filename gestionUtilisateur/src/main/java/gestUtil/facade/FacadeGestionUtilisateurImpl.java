package gestUtil.facade;

import gestUtil.dto.UtilisateurDTO;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component("facadeUtilisateur")
public class FacadeGestionUtilisateurImpl implements IFacadeGestionUtilisateur {



    public FacadeGestionUtilisateurImpl() {

    }
    @Override
    public void creerCompte(String pseudo, String email, String bio, String photoDeProfil){
        try {
            UtilisateurDTO.creerCompte(pseudo,email,bio,photoDeProfil);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changerPseudo(String email, String pseudo) {
        try {
            UtilisateurDTO.changerPseudo(email,pseudo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changerBio(String email, String nouvelleBio)  {
        try {
            UtilisateurDTO.changementBio(email,nouvelleBio);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changerPhotoDeProfil(String email, String nouvellePhotoDeProfil) {
        try {
            UtilisateurDTO.changerPhotoDeProfil(email,nouvellePhotoDeProfil);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimerCompte(String email)  {
        try {
            UtilisateurDTO.supprimerCompte(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtilisateurDTO getInformationsPubliques(String pseudo)  {

        UtilisateurDTO user;
        try {
            user = UtilisateurDTO.getInformationsPubliques(pseudo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;

    }

    @Override
    public void ajoutContact(String pseudo1, String pseudo2) {

        try {
            UtilisateurDTO.ajoutContact(pseudo1,pseudo2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
