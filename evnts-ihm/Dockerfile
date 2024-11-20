FROM node:latest

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier package.json pour installer les dépendances
COPY package.json .

# Installer les dépendances
RUN npm install

# Copier le reste des fichiers
COPY . .

# Exposer le port
EXPOSE 3000

# Commande par défaut pour démarrer l'application
CMD ["npm", "start"]
