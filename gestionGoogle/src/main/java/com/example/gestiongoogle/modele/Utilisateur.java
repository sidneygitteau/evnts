package com.example.gestiongoogle.modele;

//@AllArgsConstructor
//@Entity
public class Utilisateur {
    //@Id
    private String email;

    public Utilisateur(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

}
