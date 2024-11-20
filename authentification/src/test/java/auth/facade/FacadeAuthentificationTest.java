package auth.facade;

import auth.dto.UtilisateurDTO;
import auth.exception.EMailDejaPrisException;
import auth.exception.EmailOuPseudoDejaPrisException;
import auth.modele.Utilisateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacadeAuthentificationTest {

    private MockedStatic<UtilisateurDTO> mockedUtilisateurDTO;
    private FacadeAuthentificationImpl facadeAuth;

    @Mock
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    void init() {
        mockedUtilisateurDTO = Mockito.mockStatic(UtilisateurDTO.class);
        facadeAuth = new FacadeAuthentificationImpl(passwordEncoder);
    }

    @AfterEach
    void finish() {
        mockedUtilisateurDTO.close();
    }

    @Test
    void testInscriptionSuccess() throws EMailDejaPrisException, EmailOuPseudoDejaPrisException {
        // Arrange
        String email = "user@example.com";
        String pseudo = "username";
        String mdp = "password";
        String encodedPassword = "encodedPassword";

        // Act
        facadeAuth.inscription(pseudo, mdp, email);

        // Assert
        mockedUtilisateurDTO.verify(() -> UtilisateurDTO.enregistrerUser(email, pseudo, encodedPassword));
    }
}