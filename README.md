# 🎉 EVNTS 

## 🚀 LANCEMENT GÉNÉRAL DE L'APPLICATION
Depuis la racine du projet, lancer les commandes suivantes :

>cd authentification  
>docker build -t mon-application-authentification .  
>cd ../gestionSalon  
>docker build -t mon-application-salon .  
>cd ../gestionUtilisateur  
>docker build -t mon-application-utilisateur .  
>cd ..  
>docker compose up -d


## 🎖️ Service Authentification
### 📝 Description
Permet à l'utilisateur de se connecter à l'application. Ce service gère également le token pour la navigation.

### 📦 Installation
##### 🔰 Framework
☕️ Java 17 - 🍃 SpringBoot

##### 💿 Gestion des données
🐘 MySQL  
BDD : *authentification*

##### 🚀 Lancement
Le lancement s'opère avec docker.
> cd authentification
> 
> docker build -t mon-application-authentification .

## 🎖️ Service GestionUtilisateur
### 📝 Description
Permet à l'application de gérer un utilisateur, avec sa biographie, sa photo de profil, etc...


### 📦 Installation
##### 🔰 Framework
☕️ Java 17 - 🍃 SpringBoot

##### 💿 Gestion des données
🐘 MySQL  
BDD : *utilisateur*

##### 🚀 Lancement
Le lancement s'opère avec docker.
> cd gestionUtilisateur
> 
> docker build -t mon-application-utilisateur .

## 🎖️ Service GestionSalon
### 📝 Description
Permet à l'utilisateur de gérer un salon ainsi que ses événements liés.

### 📦 Installation
##### 🔰 Framework
☕️ Java 17 - 🍃 SpringBoot

##### 💿 Gestion des données
🐘 MySQL  
BDD : *salon*

##### 🚀 Lancement
Le lancement s'opère avec docker.
> cd salon
> 
> docker build -t mon-application-salon .

## 🎖️ Service Contact
### 📝 Description
Permets aux utilisateurs d'échanger entre eux

### 📦 Installation
##### 🔰 Framework
#️⃣ C# - ASP.NET
##### 💿 Gestion des données
🐘 MySQL  
BDD : *contact*
##### 🚀 Lancement
Le lancement s'opère avec docker.
> cd contact
> 
> docker build -t mon-application-contact .

## 🎖️ Service Google
### 📝 Description
Permet à l'utilisateur de s'authentifier avec Google, d'ajouter des événements à son calendrier Google ainsi que de procéder à des envois de mail.

### 📦 Installation
##### 🔰 Framework
☕️ Java 17 - 🍃 SpringBoot

##### 💿 Gestion des données
🐘 MySQL  
BDD : *google*

##### 🚀 Lancement
Le lancement s'opère avec docker.
> cd gestionGoogle
> 
> docker build -t mon-application-google .

## 🎖️ Service IHM
### 📝 Description
> IHM de l'application EVNTS.

### 📦 Installation
##### 🔰 Framework
⚛️ React JS

##### 🚀 Lancement
> npm start

## 👨🏻‍💻 Contributeurs

AUBERT Vincent - vincent.aubert45130@gmail.com
<br/>
CAMACHO Romain - romaincamachopro@gmail.com
<br/>
CARON Baptiste - contact.baptistecaron@gmail.com
<br/>
GITTEAU Sidney - gitteau.sidney@gmail.com