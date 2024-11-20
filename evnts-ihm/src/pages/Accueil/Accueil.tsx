import { useState } from 'react';
import Navbar from '../../components/Navbar/Navbar';
import Connexion from '../Connexion/Connexion';
import Inscription from '../Inscription/Inscription';
import './Accueil.scss';
import axios from 'axios';

const Accueil =()=> {

    const API_URL = "http://localhost:8080/salon/utilisateur/"+localStorage.getItem("username")+"/evenements"

    const events: any[] = []; 


    const token = localStorage.getItem("token");

    axios.get(API_URL, {
        headers: {
            'Authorization': token
        }
    })
    .then(response => {
        events.push(response);
    })
    .catch(error => {
        console.log(error);
    });
   
    const listEvenementsRender = events.map(event => 
        <li className="rowSalon" key={event.nom}>
            <div className="salonNameContainer">
                <div className="labelSalonName" >
                    {event.nom},{event.horaire},{event.lieu}
                </div>
            </div>
         </li>
    )

    const [nomEvent,setNomEvent] = useState("");
    const [dateEvent,setDateEvent] = useState("");
    const [lieuEvent,setLieuEvent] = useState("");


    function nouvelEvenement(){
        const user = localStorage.getItem("user")
        console.log("Nom événement :")
        console.log(nomEvent,user)
    }

    if (!localStorage.getItem("token")){
        if (localStorage.getItem("register")===("no")){
            return(<Connexion/>)
        }else{

            return(<Inscription/>)
        }
    }else{

        return (
                <div className="page">
                    <div className='navbarContainer'>
                <Navbar/>
            </div>

            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Mes événements</span>
                </div>

                <div className="contentContainer">

                    <form onSubmit={nouvelEvenement}>
                        <label>Nom</label>
                        <input type="text" required value={nomEvent} onChange={(event) => setNomEvent(event.target.value)}/>
                        <label>Date</label>
                        <input type="date" required value={dateEvent} onChange={(event) => setDateEvent(event.target.value)}/>
                        <label>Lieu</label>
                        <input type="text" required value={lieuEvent} onChange={(event) => setLieuEvent(event.target.value)}/>

                        <button id="newSalonButton" type="submit">Créer salon</button>
                    </form>
                    
                    <ul className="allRowsSalon">
                        {listEvenementsRender}
                    </ul>

                
                        
                    
                </div>
            </div>
                </div>
            )
    }


}

export default Accueil;