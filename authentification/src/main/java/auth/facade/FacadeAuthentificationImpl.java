package auth.facade;

import auth.dto.UtilisateurDTO;
import auth.exception.*;
import auth.modele.Utilisateur;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("facadeAuth")
public class FacadeAuthentificationImpl implements FacadeAuthentificationInterface {

    private PasswordEncoder passwordEncoder;
    String idConnection;

    public FacadeAuthentificationImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void inscription(String pseudo, String mdp, String eMail) throws EMailDejaPrisException, EmailOuPseudoDejaPrisException {
        UtilisateurDTO.enregistrerUser(eMail,pseudo,passwordEncoder.encode(mdp));
    }

    @Override
    public Utilisateur connexion(String pseudo, String mdp) throws MdpIncorrecteException {
        return mapToUtilisateur(UtilisateurDTO.connexionUser(pseudo),mdp);
    }

    @Override
    public void deconnexion(String pseudo) throws UtilisateurDejaDeconnecteException {
        if(idConnection==null){
            idConnection =null;
        }else{
            throw new UtilisateurDejaDeconnecteException();
        }
    }

    @Override
    public boolean getStatus(String pseudo)  {
        return false;
    }


    @Override
    public void reSetPseudo(String ancienPseudo, String nouveauPseudo) throws UtilisateurInexistantException {
        UtilisateurDTO.resetPseudo(ancienPseudo,nouveauPseudo);
    }

    @Override
    public void reSetMDP(String pseudo, String mdp, String nouveauMDP) throws CombinaisonPseudoMdpIncorrect {
        try {
            UtilisateurDTO.resetMDP(pseudo,passwordEncoder.encode(nouveauMDP));
        } catch (CombinaisonPseudoMdpIncorrect e) {
            throw new CombinaisonPseudoMdpIncorrect();
        }
    }

    @Override
    public void supprimerUtilisateur(String pseudo, String mdp) throws UtilisateurInexistantException {
        UtilisateurDTO.supprimerUtilisateur(pseudo);
    }

    private Utilisateur mapToUtilisateur(UtilisateurDTO utilisateurDTO, String mdp) throws MdpIncorrecteException {
        if (passwordEncoder.matches(mdp, utilisateurDTO.getMdp()))
            return new Utilisateur(utilisateurDTO.getPseudo(),utilisateurDTO.getEmail(), utilisateurDTO.getMdp());
        throw new MdpIncorrecteException();
    }

}
