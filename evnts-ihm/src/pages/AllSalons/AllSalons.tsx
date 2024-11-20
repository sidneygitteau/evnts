import Navbar from "../../components/Navbar/Navbar";
import './AllSalons.scss'
import axios from "axios";
import { useState } from "react";

const salons: any[]=[];
const token = localStorage.getItem("token");
const username = localStorage.getItem("username");

// récupération de la liste des salons
    axios
    .get("http://localhost:8080/salon/utilisateur/"+localStorage.getItem("username")+"/salons",{
    headers: {
        'Authorization': token
    }
    })
    .then(response => {
        salons.push(response.data)
        console.log(salons)

    })
    .catch(error =>{
        console.log(error)
    });



function AllSalons(){
    
    const [nomSalon,setNomSalon] = useState("");

    async function nouveauSalon (event: { preventDefault: () => void; }){
        event.preventDefault();

        console.log("Nom salon : %s, User : %s",nomSalon,username)

        await axios
        .post("http://localhost:8080/salon?nomCreateur="+username+"&nomSalon="+nomSalon,{
        headers: {
            'Authorization': token
        }
        })
        .then(response => {
            console.log("sdsdsdsdsdsdsds",response.data)
        
        })
        .catch(error =>{
            console.log(error)
        });

    }

    const listSalonRender = salons.map(salon => 
        <li className="rowSalon" key={salon.nomSalon}>
            <div className="salonNameContainer">
                <div className="labelSalonName" >
                    YA UN SALON LA
                </div>
            </div>
         </li>
    )

    return(
        <div className="page">
            <div className='navbarContainer'>
                <Navbar/>
            </div>

            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Salons</span>
                </div>

                <div className="contentContainer">

                    <form onSubmit={nouveauSalon}>
                        <label>Nom</label>
                        <input type="text" required value={nomSalon} onChange={(event) => setNomSalon(event.target.value)}/>
                        <button id="newSalonButton" type="submit">Créer salon</button>
                    </form>
                    
                    <ul className="allRowsSalon">
                        {listSalonRender}
                    </ul>

                
                        
                    
                </div>
            </div>

            </div>
        
    )
}

export default AllSalons ;