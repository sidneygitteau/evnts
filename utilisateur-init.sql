USE utilisateur;

CREATE TABLE User (
                      idUser INT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL,
                      pseudo VARCHAR(255) NOT NULL,
                      description TEXT,
                      status VARCHAR(50),
                      photo VARCHAR(255)
);

CREATE TABLE Message (
                         idMessage INT AUTO_INCREMENT PRIMARY KEY,
                         contenu TEXT NOT NULL,
                         dateEnvoi DATETIME NOT NULL,
                         idSender INT,
                         idReceiver INT,
                         FOREIGN KEY (idSender) REFERENCES User(idUser) ON DELETE CASCADE,
                         FOREIGN KEY (idReceiver) REFERENCES User(idUser)ON DELETE CASCADE
);



CREATE TABLE ListeContact (
                              idUser INT,
                              idUser2 INT,
                              FOREIGN KEY (idUser) REFERENCES User(idUser)ON DELETE CASCADE,
                              FOREIGN KEY (idUser2) REFERENCES User(idUser)ON DELETE CASCADE
);



-- Créer les utilisateurs Vince et Sid
INSERT INTO User (email, pseudo, description, status, photo) VALUES ('vince@bg.com', 'Vince', 'Description de Vince', 'Actif', 'nerd.png');
INSERT INTO User (email, pseudo, description, status, photo) VALUES ('sid@gital.com', 'Sid', 'Description de Sid', 'Actif', 'chauve.png');

-- Ajouter deux messages entre Vince et Sid
INSERT INTO Message (contenu, dateEnvoi, idSender, idReceiver) VALUES ('Salut Sid, comment ça va ?', NOW(), 1, 2);
INSERT INTO Message (contenu, dateEnvoi, idSender, idReceiver) VALUES ('Salut Vince, ça va bien, merci !', NOW(), 2, 1);

-- Ajouter des utilisateurs dans la liste de contacts
INSERT INTO ListeContact (idUser, idUser2) VALUES (1, 2); -- Vince ajoute Sid à ses contacts
INSERT INTO ListeContact (idUser, idUser2) VALUES (2, 1); -- Sid ajoute Vince à ses contacts
