package bdd;

import gestUtil.dto.UtilisateurDTO;

import java.sql.*;
import java.util.ArrayList;

public class InteractionBDDUtilisateur {
    public static Statement connecterGestionUtilisateurSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://dbGestionUtilisateur:3306/utilisateur";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        return connection.createStatement();

    }

    public static void creerCompteSQL(String pseudo, String email, String bio, String photoDeProfil) throws SQLException {
        Statement st = connecterGestionUtilisateurSQL();
        pseudo = pseudo.replace("'", "\\'");
        bio = bio.replace("'", "\\'");
        photoDeProfil = photoDeProfil.replace("'", "\\'");
        email = email.replace("'", "\\'");

        String SQL = "insert into User(email, pseudo, description,status,photo) VALUES ('"+email+"', '"+pseudo+"','"+bio+"','Actif','"+photoDeProfil+"')";
        st.executeUpdate(SQL);
    }

    public static void changerPseudoSQL(String email, String nouveauPseudo) throws SQLException {
        Statement st = connecterGestionUtilisateurSQL();
        nouveauPseudo = nouveauPseudo.replace("'", "\\'");

        String SQL = "UPDATE User SET pseudo = '"+nouveauPseudo+"' WHERE email = '"+email+"'";
        st.executeUpdate(SQL);
    }

    public static void changerBioSQL(String email, String nouvelleBio) throws SQLException{
        String nouvelleBioEscaped = nouvelleBio.replace("'", "\\'");
        Statement st = connecterGestionUtilisateurSQL();
        String SQL = "UPDATE User SET description = '" + nouvelleBioEscaped + "' WHERE email = '" + email + "'";
        st.executeUpdate(SQL);

    }

    public static void changerPhotoDeProfilSQL(String email, String nouvellePhotoDeProfil)throws SQLException {
        String nouvellePhotoDeProfilEscaped = nouvellePhotoDeProfil.replace("'", "\\'");
        Statement st = connecterGestionUtilisateurSQL();
        String SQL = "UPDATE User SET photo = '" + nouvellePhotoDeProfilEscaped + "' WHERE email = '" + email + "'";
        st.executeUpdate(SQL);
    }

    public static void supprimerCompteSQL(String email) throws  SQLException{
        Statement st = connecterGestionUtilisateurSQL();
        String SQL = "DELETE FROM User WHERE email = '"+email+"'";
        st.executeUpdate(SQL);
    }

    public static UtilisateurDTO getInformationsPubliquesSQL(String pseudo) throws SQLException {
        Statement st = connecterGestionUtilisateurSQL();
        UtilisateurDTO userDTO = new UtilisateurDTO();
        String SQLUser = "SELECT * FROM User where pseudo='"+pseudo+"'";
        ResultSet rs = st.executeQuery(SQLUser);

        while (rs.next()) {
            int id = rs.getInt("idUser");
            String email = rs.getString("email");
            String pseudoSQL = rs.getString("pseudo");
            String description = rs.getString("description");
            String status = rs.getString("status");
            String photo = rs.getString("photo");
            userDTO.setId(id);
            userDTO.setEmail(email);
            userDTO.setPseudo(pseudoSQL);
            userDTO.setBio(description);
            userDTO.setStatus(status);
            userDTO.setPhotoDeProfil(photo);

        }
        return userDTO;
    }

    public static void ajoutContactSQL(String pseudo1, String pseudo2) throws SQLException {
        int id1 =getIdByPseudo(pseudo1);
        int id2 =getIdByPseudo(pseudo2);
        Statement st = connecterGestionUtilisateurSQL();
        String SQL = "insert into ListeContact(idUser, idUser2) VALUES ("+id1+", "+id2+")";
        String SQL2 = "insert into ListeContact(idUser, idUser2) VALUES ("+id2+", "+id1+")";

        st.executeUpdate(SQL);
        st.executeUpdate(SQL2);
    }

    public static ArrayList<String> getListeContactByPseudo(String pseudo) throws SQLException{
        int id1 =getIdByPseudo(pseudo);
        ArrayList<String> lesContacts = new ArrayList<>();
        Statement st = connecterGestionUtilisateurSQL();
        String SQLUser = "SELECT idUser2 FROM ListeContact where idUser="+id1;
        ResultSet rs = st.executeQuery(SQLUser);

        while (rs.next()) {
            int idRecup = rs.getInt("idUser2");
            lesContacts.add(getPseudoById(idRecup));
        }

        return lesContacts;
    }

    public static int getIdByPseudo(String pseudo) throws SQLException {
        Statement st = connecterGestionUtilisateurSQL();
        int userid = 0;
        String SQLUser = "SELECT * FROM User where pseudo='"+pseudo+"'";
        ResultSet rs = st.executeQuery(SQLUser);
        while (rs.next()) {
            userid = rs.getInt("idUser");
        }
        return userid;
    }

    public static String getPseudoById(int id) throws SQLException {
        Statement st = connecterGestionUtilisateurSQL();
        String pseudo =null;
        String SQLUser = "SELECT * FROM User where idUser="+id;
        ResultSet rs = st.executeQuery(SQLUser);

        while (rs.next()) {
            pseudo = rs.getString("pseudo");
        }

        return pseudo;
    }
}
