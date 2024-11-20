import './Inscription.scss';
import logo from '../../assets/logo/logo-black-nocurve.png';
import { useState } from 'react';
import axios from 'axios';


import { Link } from 'react-router-dom'

const API_URL = "http://localhost:8080/";

const Inscription = () => {
    const [nom, setNom] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
   

    const handleSubmit = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        await axios
            .post(API_URL + "auth/inscription?pseudo="+nom+"&mdp="+password+"&eMail="+email)
            .then(response =>{
                axios.post(API_URL + "utilisateurs/inscription?pseudo="+nom+"&mdp="+password+"&email="+email+"&bio="+null+"&photoDeProfil="+null)
                .then(response =>{
                    axios.post(API_URL + "salon/utilisateur/"+nom+"?email="+email)
                    .then(response =>{
                        axios.post(API_URL + "google/utilisateur/"+email)
                        .then(response =>{
                            if(response.status.toString() === "201"){
                            localStorage.setItem("register", "no");
                            localStorage.setItem("email",email);
                            alert("Succès");
                            window.location.reload();
                            }
                        })
                        .catch(error =>{
                            console.log("Erreur lors de l'authentification google : ", error);           
                            alert("Erreur lors de l'authentification")
                        });
                    })
                    .catch(error =>{
                        console.log("Erreur lors de l'authentification salon : ", error);           
                        alert("Erreur lors de l'authentification")
                    });
                })
                .catch(error =>{
                    console.log("Erreur lors de l'authentification utilsateurs : ", error);           
                    alert("Erreur lors de l'authentification")
                });
            })
            .catch(error =>{
                console.log("Erreur lors de l'authentification auth : ", error);           
                alert("Erreur lors de l'authentification")
            });
    }

    const handleClick = () => {
       if(localStorage.getItem("register")==="yes"){
            localStorage.setItem("register","no")
       }
        window.location.reload()

    }
    return (
        <div className='pageInscription'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

            <form className="form-container" onSubmit={handleSubmit}>
                <div className="img-form-inscription">

                </div>

                <div className="form" >
                    <h3 className="title-form-inscription">Inscription</h3>

                    <div className="field">
                        <label htmlFor="email">Email</label>
                        <input type="text" name="email" id="email" required value={email} onChange={(event) => setEmail(event.target.value)}/>
                    </div>

                    <div className="field">
                        <label htmlFor="login">Login</label>
                        <input type="text" name="login" id="login" required value={nom} onChange={(event) => setNom(event.target.value)} />
                    </div>
                    
                    <div className="field">
                        <label htmlFor="pwd">Mot de Passe</label>
                        <input type="password" name="pwd" id="pwd" required value={password} onChange={(event) => setPassword(event.target.value)}/>
                    </div>

                    <div className="buttonContainer">
                        <button className="inscriptionButton" type="submit">Inscription</button>
                    </div>
                
                
                </div>
    
            </form>

                <div className="connexion-link">
                    <Link to="/" onClick={handleClick}>
                        Déjà inscrit ? Connectez vous ici.
                    </Link>
                </div>

            </div>
        </div>
       
    );
}

export default Inscription;