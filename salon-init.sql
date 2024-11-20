USE salon;


CREATE TABLE Membre (
                        idMembre INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL,
                        nomMembre VARCHAR(255) NOT NULL
);

CREATE TABLE Salon (
                       idSalon INT AUTO_INCREMENT PRIMARY KEY,
                       nomSalon VARCHAR(255) NOT NULL,
                       nomCreateur VARCHAR(255) NOT NULL,
                       logo VARCHAR(255)
);

CREATE TABLE SalonMembre (
                             idSalon INT,
                             idMembre INT,
                             FOREIGN KEY (idSalon) REFERENCES Salon(idSalon) ON DELETE CASCADE,
                             FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE,
                             PRIMARY KEY (idSalon, idMembre)
);

CREATE TABLE ModerateurSalon (
                             idSalon INT,
                             idMembre INT,
                             FOREIGN KEY (idSalon) REFERENCES Salon(idSalon) ON DELETE CASCADE,
                             FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE,
                             PRIMARY KEY (idSalon, idMembre)
);

CREATE TABLE Evenement (
                           idEvenement INT AUTO_INCREMENT PRIMARY KEY,
                           nomEvenement VARCHAR(255) NOT NULL,
                           nombrePersonneMax INT,
                           details TEXT,
                           dateEvenement VARCHAR(255),
                           lieu VARCHAR(255),
                           isValide BOOLEAN,
                           nomCreateur VARCHAR(255) NOT NULL,
                           idSalon INT,
                           FOREIGN KEY (idSalon) REFERENCES Salon(idSalon) ON DELETE CASCADE
);

CREATE TABLE PresenceEvenement (
                                 idMembre INT,
                                 idEvenement INT,
                                 FOREIGN KEY (idEvenement) REFERENCES Evenement(idEvenement) ON DELETE CASCADE,
                                 FOREIGN KEY (idMembre) REFERENCES Membre(idMembre) ON DELETE CASCADE,
                                 PRIMARY KEY (idEvenement, idMembre)
);

CREATE TABLE MessageSalon (
                                   idMessage INT AUTO_INCREMENT PRIMARY KEY,
                                   idSalon INT,
                                   nomAuteur VARCHAR(255),
                                   contenu VARCHAR(255),
                                   dateMessage VARCHAR(255),
                                   FOREIGN KEY (idSalon) REFERENCES Salon(idSalon) ON DELETE CASCADE

);

CREATE TABLE MessageEvenement (
                                  idMessage INT AUTO_INCREMENT PRIMARY KEY,
                                  idEvenement INT,
                                  nomAuteur VARCHAR(255),
                                  contenu VARCHAR(255),
                                  dateMessage VARCHAR(255),
                                  FOREIGN KEY (idEvenement) REFERENCES Evenement(idEvenement) ON DELETE CASCADE
);


-- Insérer un salon
INSERT INTO Salon (nomSalon, nomCreateur, logo) VALUES ('Mon Premier Salon', 'Vince', 'https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png');

-- Insérer des membres
INSERT INTO Membre (nomMembre,email) VALUES ('Vince','vincent.aubert@gmail.com');
INSERT INTO Membre (nomMembre,email) VALUES ('Camacho','romain.camacho@gmail.com');

-- Associer des membres à un salon
INSERT INTO SalonMembre (idSalon, idMembre) VALUES (1, 1); -- Associer Vince à Mon Premier Salon
INSERT INTO SalonMembre (idSalon, idMembre) VALUES (1, 2); -- Associer Camacho à Mon Premier Salon

-- Ajouter un événement au salon
INSERT INTO Evenement (nomEvenement, nombrePersonneMax, details, dateEvenement, lieu, isValide, nomCreateur, idSalon)
VALUES ('Jeux de société', 100, 'Super jeux in coming !', '2024-02-20', 'Chez oim', false, 'Vince', 1);

INSERT INTO ModerateurSalon (idSalon, idMembre) VALUES (1, 1); -- Associer Vince à Modérateur du Salon
