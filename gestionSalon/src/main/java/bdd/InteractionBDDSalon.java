package bdd;


import gestSal.dto.UtilisateurDTO;
import gestSal.facade.erreurs.EvenementIncompletException;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InteractionBDDSalon {

    private static final String NOM_MEMBRE = "nomMembre";

    private static final String EMAIL = "email";

    public static Statement connecterAuSalonSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://dbSalonName:3306/salon";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        return connection.createStatement();

    }


    public static void creerSalonSQL(String nomSalon, String nomCreateur, String logo) throws SQLException {
        Statement st = connecterAuSalonSQL();
        nomSalon = nomSalon.replace("'", "\\'");
        nomCreateur = nomCreateur.replace("'", "\\'");
        logo = logo.replace("'", "\\'");

        String SQL = "INSERT INTO Salon (nomSalon, nomCreateur, logo) " +
                "SELECT '"+nomSalon+"', '"+nomCreateur+"', '"+logo+"' " +
                "WHERE NOT EXISTS (SELECT 1 FROM Salon WHERE nomSalon = '"+nomSalon+"')";
        st.executeUpdate(SQL);
    }


    public static Salon getSalonById(int id) throws SQLException {
        Salon salon = new Salon();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Salon where idSalon="+id;
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            salon.setIdSalon(rs.getInt("idSalon"));
            salon.setNomSalon(rs.getString("nomSalon"));
            salon.setNomCreateur(rs.getString("nomCreateur"));
            salon.setLogo(rs.getString("logo"));
        }

        return salon;

    }

    public static Salon getSalonByName(String nom) throws SQLException {
        Salon salon = new Salon();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Salon where nomSalon='"+nom+"'";
        ResultSet rs = st.executeQuery(SQL);
        while (rs.next()) {
            salon.setIdSalon(rs.getInt("idSalon"));
            salon.setNomSalon(rs.getString("nomSalon"));
            salon.setNomCreateur(rs.getString("nomCreateur"));
            salon.setLogo(rs.getString("logo"));

        }
        return salon;

    }

    public static Salon modifierSalonSQL(Salon salon, String choix, String valeur, int id) throws SQLException {
        Statement st = connecterAuSalonSQL();
        valeur = valeur.replace("'", "\\'");

        switch (choix){
            case "nom" -> {
                salon.setNomSalon(valeur);
                String SQL = "UPDATE Salon SET nomSalon = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);
            }
            case "logo" -> {
                salon.setLogo(valeur);
                String SQL = "UPDATE Salon SET logo = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);
            }
            case "createur" -> {
                salon.setNomCreateur(valeur);
                String SQL = "UPDATE Salon SET nomCreateur = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);

            }
            default -> {
                return null;
            }
        }
        return getSalonById(id);
    }


    public static Utilisateur getUtilisateurByPseudoSQL(String pseudoUtilisateur) throws SQLException {
        Utilisateur userDTO = new Utilisateur();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Membre where nomMembre='"+pseudoUtilisateur+"'";
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
           userDTO.setIdUtilisateur(rs.getInt("idMembre"));
           userDTO.setPseudo(rs.getString(NOM_MEMBRE));
           userDTO.setEmail(rs.getString(EMAIL));
        }


        return userDTO;
    }

    public static Utilisateur getUtilisateurByEmail(String email) throws SQLException{
        Utilisateur utilisateur = new Utilisateur();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Membre where email='"+email+"'";
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            utilisateur.setIdUtilisateur(rs.getInt("idMembre"));
            utilisateur.setPseudo(rs.getString(NOM_MEMBRE));
            utilisateur.setEmail(rs.getString(EMAIL));
        }
        return utilisateur;
    }

    public static UtilisateurDTO getUtilisateurByIdSQL(int idUtilisateur) throws SQLException {
        UtilisateurDTO userDTO = new UtilisateurDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Membre where idMembre="+idUtilisateur;
        ResultSet rs = st.executeQuery(SQL);
        while (rs.next()) {
            userDTO.setIdUtilisateur(rs.getInt("idMembre"));
            userDTO.setPseudo(rs.getString(NOM_MEMBRE));
            userDTO.setEmail(rs.getString(EMAIL));
        }

        return userDTO;
    }

    public static void rejoindreSalonSql(Utilisateur utilisateurDTO, Salon salonDTO) throws SQLException {
        int idUser = utilisateurDTO.getIdUtilisateur();
        int idSalon = salonDTO.getIdSalon();

        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO SalonMembre (idSalon, idMembre) " +
                "SELECT " + idSalon + ", " + idUser + " " +
                "WHERE NOT EXISTS " +
                "(SELECT 1 FROM SalonMembre WHERE idSalon = " + idSalon + " AND idMembre = " + idUser + ")";

        st.executeUpdate(SQL);
    }


    public static Evenement getEvenementByNomEtNumSalonSQL(int id, String nomEvenement) throws SQLException {
        Evenement evenement = new Evenement();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Evenement where nomEvenement='"+nomEvenement+"' and idSalon ="+id;
        ResultSet rs = st.executeQuery(SQL);
        while (rs.next()) {
            evenement.setIdEvenement(rs.getInt("idEvenement"));
            evenement.setNombrePersonneMax(rs.getInt("nombrePersonneMax"));
            evenement.setNomEvenement(rs.getString("nomEvenement"));
            evenement.setDetailsEvenement(rs.getString("details"));
            evenement.setDate(rs.getString("dateEvenement"));
            evenement.setLieu(rs.getString("lieu"));
            evenement.setEstValide(rs.getBoolean("isValide"));
            evenement.setNomCreateur(rs.getString("nomCreateur"));
        }

        return evenement;
    }

    public static void modifierEvenementSQL(int idSalon, Evenement evenement, String choix, String valeur) throws SQLException {
        int idEvenement = getEvenementByNomEtNumSalonSQL(idSalon,evenement.getNomEvenement()).getIdEvenement();
        valeur = valeur.replace("'", "\\'");
        Statement st = connecterAuSalonSQL();
        switch (choix) {
            case "description" -> {
                evenement.setDetailsEvenement(valeur);
                String SQL = "UPDATE Evenement SET details = '" + valeur + "' WHERE idEvenement = " + idEvenement;
                st.executeUpdate(SQL);
            }
            case "date" -> {
                evenement.setDate(valeur);
                String SQL = "UPDATE Evenement SET dateEvenement = '" + valeur + "' WHERE idEvenement = " + idEvenement;
                st.executeUpdate(SQL);
            }
            case "lieu" -> {
                evenement.setLieu(valeur);
                String SQL = "UPDATE Evenement SET lieu = '" + valeur + "' WHERE idEvenement = " + idEvenement;
                st.executeUpdate(SQL);
            }
            case "nombre" ->
            {
                evenement.setNombrePersonneMax(Integer.parseInt(valeur));
                String SQL = "UPDATE Evenement SET nombrePersonneMax = " + Integer.parseInt(valeur) + " WHERE idEvenement = " + idEvenement;
                st.executeUpdate(SQL);
            }
            case "nom" -> {
                evenement.setNomEvenement(valeur);
                String SQL = "UPDATE Evenement SET nomEvenement = '" + valeur + "' WHERE idEvenement = " + idEvenement;
                st.executeUpdate(SQL);
            }
            default -> {
                return;
            }
        }
    }


    public static void retirerModerateurDuSalonSQL(Salon salonDTO, Utilisateur utilisateurDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM ModerateurSalon where idSalon="+salonDTO.getIdSalon()+" and idMembre="+utilisateurDTO.getIdUtilisateur();
        st.executeUpdate(SQL);
    }

    public static void ajouterModerateurAuSalon(Utilisateur utilisateurDTO, Salon salonDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String checkIfExistsSQL = "SELECT COUNT(*) FROM ModerateurSalon WHERE idSalon = " + salonDTO.getIdSalon() + " AND idMembre = " + utilisateurDTO.getIdUtilisateur();
        ResultSet resultSet = st.executeQuery(checkIfExistsSQL);
        resultSet.next();
        int count = resultSet.getInt(1);

        if (count > 0) {
            return;
        }

        String SQL = "INSERT INTO ModerateurSalon (idSalon, idMembre) VALUES (" + salonDTO.getIdSalon() + ", " + utilisateurDTO.getIdUtilisateur() + ")";
        st.executeUpdate(SQL);
    }


    public static List<Utilisateur> seDefiniCommePresentAUnEvenementSQL(Utilisateur utilisateurDTO, Evenement evenementDTO) throws SQLException {
        List<Utilisateur> participants = new ArrayList<>();

        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO PresenceEvenement (idMembre,idEvenement) values ("+utilisateurDTO.getIdUtilisateur()+",+"+evenementDTO.getIdEvenement()+")";
        st.executeUpdate(SQL);

        String SQL2 = "select * from PresenceEvenement where idEvenement="+evenementDTO.getIdEvenement();
        ResultSet rs = st.executeQuery(SQL2);

        while (rs.next()) {
            Utilisateur user = new Utilisateur();
            user.setPseudo(getUtilisateurByIdSQL(rs.getInt("idMembre")).getPseudo());
            user.setEmail(getUtilisateurByIdSQL(rs.getInt("idMembre")).getEmail());
            participants.add(user);
        }

        return participants;
    }



    public static List<Utilisateur> seDefiniCommeAbsentAUnEvenementSQL(Utilisateur utilisateurDTO, Evenement evenementDTO) throws SQLException {
        List<Utilisateur> participants = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM PresenceEvenement WHERE idMembre = " + utilisateurDTO.getIdUtilisateur() + " AND idEvenement = " + evenementDTO.getIdEvenement();
        st.executeUpdate(SQL);

        String SQL2 = "select * from PresenceEvenement where idEvenement="+evenementDTO.getIdEvenement();
        ResultSet rs = st.executeQuery(SQL2);
        while(rs.next()){
            Utilisateur user = new Utilisateur();
            user.setPseudo(getUtilisateurByIdSQL(rs.getInt("idMembre")).getPseudo());
            user.setEmail(getUtilisateurByIdSQL(rs.getInt("idMembre")).getEmail());
            participants.add(user);
        }
        return participants;
    }



    public static Boolean validerEvenementSQL(Evenement evenementDTO) throws SQLException, EvenementIncompletException {
        Boolean isValide = false;
        int idEvenement = evenementDTO.getIdEvenement();
        int nombrePersonneMax = evenementDTO.getNombrePersonneMax();

        Statement st = connecterAuSalonSQL();
        String countSQL = "SELECT COUNT(*) AS count FROM PresenceEvenement WHERE idEvenement = " + idEvenement;
        ResultSet rs = st.executeQuery(countSQL);
        int nombrePersonnesPresentes = 0;
        if (rs.next()) {
            nombrePersonnesPresentes = rs.getInt("count");
        }

        if (nombrePersonnesPresentes == nombrePersonneMax) {
            isValide=true;
            String updateSQL = "UPDATE Evenement SET isValide = true WHERE idEvenement = " + idEvenement;
            st.executeUpdate(updateSQL);
        } else {
            throw new EvenementIncompletException();
        }
        return isValide;
    }




    public static List<Message> getMessageSalonSQL(int numSalon) throws SQLException {
        List<Message> lesMessages = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQLMessage = "select * from MessageSalon where idSalon="+numSalon;
        ResultSet rs = st.executeQuery(SQLMessage);

        while (rs.next()) {
            Message message = new Message();
            String auteur = rs.getString("nomAuteur");
            String contenu = rs.getString("contenu");
            String dateMessage = rs.getString("dateMessage");
            message.setAuteur(auteur);
            message.setContenu(contenu);
            message.setDate(dateMessage);
            lesMessages.add(message);

        }
        return lesMessages;
    }


    public static List<Message> getMessageEventSQL(int idEvenement) throws SQLException {
        ArrayList<Message> lesMessages = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from MessageEvenement where idEvenement="+idEvenement;
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            Message message = new Message();
            String auteur = rs.getString("nomAuteur");
            String contenu = rs.getString("contenu");
            String dateMessage = rs.getString("dateMessage");
            message.setAuteur(auteur);
            message.setContenu(contenu);
            message.setDate(dateMessage);
            lesMessages.add(message);

        }
        return lesMessages;
    }

    public static void supprimerUtilisateurSQL(Utilisateur utilisateur) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM Membre where idMembre="+utilisateur.getIdUtilisateur();
        st.executeUpdate(SQL);
    }


    public static void creerEvenement(Salon salon, String nomEvenement, int nombrePersonneMax, String detailsEvenement, String lieu, Utilisateur createur, String date) throws SQLException {
        Statement st = connecterAuSalonSQL();
        detailsEvenement = detailsEvenement.replace("'", "\\'");
        nomEvenement = nomEvenement.replace("'", "\\'");
        lieu = lieu.replace("'", "\\'");
        date = date.replace("'", "\\'");

        // Check if the event already exists
        String checkIfExistsSQL = "SELECT COUNT(*) FROM Evenement WHERE nomEvenement = '" + nomEvenement + "' AND dateEvenement = '" + date + "' AND lieu = '" + lieu + "'";
        ResultSet rs = st.executeQuery(checkIfExistsSQL);
        rs.next();
        int count = rs.getInt(1);
        if (count > 0) {
            // Event already exists, return 409 conflict
            throw new SQLException("L'évent existe déjà", "409");
        } else {
            // Event doesn't exist, insert it
            String SQL = "INSERT INTO Evenement (nomEvenement,nombrePersonneMax,details,dateEvenement,lieu,isValide,nomCreateur,idSalon) VALUES ('" + nomEvenement + "'," + nombrePersonneMax + ",'" + detailsEvenement + "','" + date + "','" + lieu + "',false,'" + createur.getPseudo() + "'," + salon.getIdSalon() + ")";
            st.executeUpdate(SQL);
        }
    }


    public static String getEvenementById(int idEvent) throws SQLException {
        String nomEvent = null;
        Statement st = connecterAuSalonSQL();
        String SQL = "SELECT * from Evenement WHERE idEvenement="+idEvent;
        ResultSet rs = st.executeQuery(SQL);
        while (rs.next()){
            nomEvent = rs.getString("nomEvenement");
        }
        return nomEvent;
    }


    public static List<Integer> getSalonByUser(int idUtilisateur) throws SQLException {
        List<Integer> listeIdSalon = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from SalonMembre where idMembre="+idUtilisateur;
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            listeIdSalon.add(rs.getInt("idSalon"));
        }

        return listeIdSalon;
    }

    public static List<Integer> getEventByUser(int idUtilisateur) throws SQLException {
        List<Integer> listeIdEvent = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from PresenceEvenement where idMembre="+idUtilisateur;
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            listeIdEvent.add(rs.getInt("idEvenement"));
        }

        return listeIdEvent;
    }

    public static Evenement getEventById(int idEvenement) throws SQLException {
        Evenement evenement = new Evenement();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Evenement where idEvenement="+idEvenement;
        ResultSet rs = st.executeQuery(SQL);

        while (rs.next()) {
            evenement.setNomEvenement(rs.getString("nomEvenement"));
            evenement.setNombrePersonneMax(rs.getInt("nombrePersonneMax"));
            evenement.setNomCreateur(rs.getString("nomCreateur"));
            evenement.setLieu(rs.getString("lieu"));
            evenement.setDetailsEvenement(rs.getString("details"));
            evenement.setDate(String.valueOf(rs.getDate("dateEvenement")));
            evenement.setEstValide(rs.getBoolean("isValide"));

        }
        return evenement;
    }

    public static void ajouterMembre(String nomMembre, String email) throws SQLException {
        Statement st = connecterAuSalonSQL();
        nomMembre = nomMembre.replace("'", "\\'");

        String checkIfExistsSQL = "SELECT COUNT(*) FROM Membre WHERE nomMembre = '" + nomMembre + "' AND email='"+email+"'";
        ResultSet rs = st.executeQuery(checkIfExistsSQL);
        rs.next();
        int count = rs.getInt(1);
        if (count == 0) {
            String SQL = "INSERT INTO Membre (nomMembre,email) VALUES ('" + nomMembre + "','"+email+"')";
            st.executeUpdate(SQL);
        }else{
            throw new SQLException("Le membre est déjà présent","409");
        }
    }


    public void envoyerMessageSalonSQL(int idSalon, String pseudoUtilisateur, String contenu, String dateTime) throws SQLException {
        Statement st = connecterAuSalonSQL();
        pseudoUtilisateur = pseudoUtilisateur.replace("'", "\\'");
        contenu = contenu.replace("'", "\\'");
        dateTime = dateTime.replace("'", "\\'");

        String SQL = "INSERT INTO MessageSalon (idSalon,nomAuteur,contenu,dateMessage) values ("+idSalon+",'"+pseudoUtilisateur+"','"+contenu+"','"+dateTime+"')";
        st.executeUpdate(SQL);

    }

    public void envoyerMessageEventSQL(int idEvenement, String pseudoUtilisateur, String contenu, String dateTime) throws SQLException {
        Statement st = connecterAuSalonSQL();
        pseudoUtilisateur = pseudoUtilisateur.replace("'", "\\'");
        contenu = contenu.replace("'", "\\'");
        dateTime = dateTime.replace("'", "\\'");

        String SQL = "INSERT INTO MessageEvenement (idEvenement,nomAuteur,contenu,dateMessage) values ("+idEvenement+",'"+pseudoUtilisateur+"','"+contenu+"','"+dateTime+"')";
        st.executeUpdate(SQL);
    }
}
