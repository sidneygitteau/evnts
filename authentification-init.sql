USE authentification;

CREATE TABLE Utilisateur (
                      idUtilisateur INT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL,
                      pseudo VARCHAR(255) NOT NULL,
                      motDePasse VARCHAR(255) NOT NULL
);
INSERT INTO Utilisateur (email,pseudo, motDePasse) VALUES ('vincent.aubert@evnt.com', 'vince','123456');



