package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InteractionBDDGoogle {

    public InteractionBDDGoogle() {
    }

    public static Statement connecterAuthentificationSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://dbGoogle:3306/google";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;
    }

    public void enregistrerUser(String email) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "insert into GoogleUser(email) VALUES ('"+email+"')";
        st.executeUpdate(SQL);
    }

    public void deleteUtilisateur(String email) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "DELETE FROM GoogleUser WHERE email = '"+email+"'";
        st.executeUpdate(SQL);
    }
}
