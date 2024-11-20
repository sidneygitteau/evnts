USE contact;

CREATE TABLE Utilisateur (
                             email VARCHAR(255) PRIMARY KEY
);

CREATE TABLE Contact (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         demandeur VARCHAR(255),
                         cible VARCHAR(255),
                         CONSTRAINT fk_demandeur FOREIGN KEY (demandeur) REFERENCES Utilisateur(email),
                         CONSTRAINT fk_cible FOREIGN KEY (cible) REFERENCES Utilisateur(email),
                         UNIQUE KEY idx_demandeur_cible (demandeur, cible)
);

CREATE TABLE Conversation (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              utilisateur1 VARCHAR(255),
                              utilisateur2 VARCHAR(255),
                              CONSTRAINT fk_utilisateur1 FOREIGN KEY (utilisateur1) REFERENCES Utilisateur(email),
                              CONSTRAINT fk_utilisateur2 FOREIGN KEY (utilisateur2) REFERENCES Utilisateur(email),
                              UNIQUE KEY idx_utilisateur1_utilisateur2 (utilisateur1, utilisateur2)
);

CREATE TABLE Message (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         idConversation INT,
                         envoyeur VARCHAR(255),
                         contenu TEXT,
                         dateEnvoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_conversation FOREIGN KEY (idConversation) REFERENCES Conversation(id),
                         CONSTRAINT fk_envoyeur FOREIGN KEY (envoyeur) REFERENCES Utilisateur(email)
);

