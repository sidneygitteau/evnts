package gestSal.modele;

import java.util.List;

public class Conversation {
    private int idConversation;
    private String utilisateurUn;
    private String utilisateurDeux;
    private List<Message> lesMessagesDeLaConversation;

    public Conversation(int idConversation, String utilisateurUn, String utilisateurDeux, List<Message> lesMessagesDeLaConversation) {
        this.idConversation = idConversation;
        this.utilisateurUn = utilisateurUn;
        this.utilisateurDeux = utilisateurDeux;
        this.lesMessagesDeLaConversation = lesMessagesDeLaConversation;
    }

    public Conversation() {
    }

    public int getIdConversation() {
        return idConversation;
    }


    public String getUtilisateurUn() {
        return utilisateurUn;
    }

    public void setUtilisateurUn(String utilisateurUn) {
        this.utilisateurUn = utilisateurUn;
    }

    public String getUtilisateurDeux() {
        return utilisateurDeux;
    }

    public void setUtilisateurDeux(String utilisateurDeux) {
        this.utilisateurDeux = utilisateurDeux;
    }

    public List<Message> getLesMessagesDeLaConversation() {
        return lesMessagesDeLaConversation;
    }

    public void setLesMessagesDeLaConversation(List<Message> lesMessagesDeLaConversation) {
        this.lesMessagesDeLaConversation = lesMessagesDeLaConversation;
    }
}
