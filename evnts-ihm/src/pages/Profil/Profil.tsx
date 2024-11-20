import React, { useState, ChangeEvent } from 'react';
import Navbar from "../../components/Navbar/Navbar";
import axios from 'axios';
import Inscription from '../Inscription/Inscription';
import Accueil from '../Accueil/Accueil';
import { useNavigate } from 'react-router-dom';

function Profil() {

    const API_URL = "http://localhost:8080/";
    const token = localStorage.getItem("token");
    const navigate = useNavigate();

    const [newUsername, setNewUsername] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [mdp, setMdp] = useState("");

    const handleMdpChange = (event: ChangeEvent<HTMLInputElement>) => {
        setMdp(event.target.value);
    };

    const handleUsernameChange = (event: ChangeEvent<HTMLInputElement>) => {
        setNewUsername(event.target.value);
    };

    const handlePasswordChange = (event: ChangeEvent<HTMLInputElement>) => {
        setNewPassword(event.target.value);
    };

    const handleUsernameSubmit = async (event: { preventDefault: () => void; }) => {
        console.log(token);
        event.preventDefault();
        await axios
            .patch(API_URL + "auth/modification-pseudo?pseudo="+localStorage.getItem("username")+"&nouveauPseudo="+newUsername,{
                headers: {
                    'Authorization': token
                }
            }).then(response =>{
                axios.patch(API_URL + "utilisateurs/changementPseudo?email="+localStorage.getItem("email")+"&nouveauPseudo="+newUsername,{
                    headers: {
                        'Authorization': token
                    }
                })
                .then(response =>{
                    localStorage.setItem("username",newUsername);
                    window.location.reload();
                    return(<Accueil/>)
                })
                .catch(error =>{
                    console.log("Erreur lors de la modification du pseudo user : ", error);           
                    alert("Erreur lors de la modification")
                });
            })
            .catch(error =>{
                console.log("Erreur lors de la modification du pseudo : ", error);           
                alert("Erreur lors de la modification")
            });
    };

    const handlePasswordSubmit = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        await axios
            .patch(API_URL + "auth/modification-mdp?pseudo="+localStorage.getItem("username")+"&mdp="+mdp+"&nouveauMDP="+newPassword,{
                headers: {
                    'Authorization': token
                }
            })
            .catch(error =>{
                console.log("Erreur lors de la modification du mdp : ", error);           
                alert("Erreur lors de la modification")
            });
    };

    const handleDeleteAccount = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        await axios
            .delete(API_URL + "auth/suppression?pseudo="+localStorage.getItem("username")+"&mdp="+mdp,{
                headers: {
                    'Authorization': token
                }
            })
            .then(response =>{
                axios.delete(API_URL + "utilisateurs/suppressionCompte?email="+localStorage.getItem("email"),{
                    headers: {
                        'Authorization': token
                    }
                })
                .then(response =>{
                    axios.delete(API_URL + "salon/utilisateur/"+localStorage.getItem("username"),{
                        headers: {
                            'Authorization': token
                        }
                    })        
                    .then(response =>{
                        axios.delete(API_URL + "google/utilisateur?email="+localStorage.getItem("email"),{
                            headers: {
                                'Authorization': token
                            }
                        })
                        .then(response =>{
                            console.log("caca")
                            localStorage.removeItem("username");
                            localStorage.removeItem("email");
                            localStorage.removeItem("token");
                            localStorage.setItem("register","no");
                            navigate("/");
                        })
                        .catch(error =>{
                            console.log("Erreur lors de la suppression google : ", error);           
                            alert("Erreur lors de la suppression")
                        });
                    })
                    .catch(error =>{
                        console.log("Erreur lors de la suppression salon : ", error);           
                        alert("Erreur lors de la suppression")
                    });
                }).catch(error =>{
                    console.log("Erreur lors de la suppression util : ", error);           
                    alert("Erreur lors de la suppression")
                });
            }).catch(error =>{
                console.log("Erreur lors de la suppression auth : ", error);           
                alert("Erreur lors de la suppression")
            })                                  
    };

    return (
        <div className="page">
            <div className='navbarContainer'>
                <Navbar />
            </div>
            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Mon profil</span>
                </div>
                <div className="contentContainer">                     
                    <div>
                        <button onClick={handleUsernameSubmit}>Changer de pseudo</button>
                        <input type="text" placeholder="Nouveau pseudo" value={newUsername} onChange={handleUsernameChange} />
                    </div>
                    <div>
                        <button onClick={handlePasswordSubmit}>Changer son mdp</button>
                        <input type="password" placeholder="Nouveau mot de passe" value={newPassword} onChange={handlePasswordChange} />
                    </div>
                    <div>
                        <button onClick={handleDeleteAccount}>Supprimer son compte</button>
                    </div>
                    <div>
                        <label>Votre mot de passe actuel : </label>
                        <input type="text" placeholder="mdp" value={mdp} onChange={handleMdpChange} />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profil;
