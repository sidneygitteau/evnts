import './Connexion.scss';

import { Link} from 'react-router-dom'
import logo from '../../assets/logo/logo-black-nocurve.png';
import{ useState } from 'react';
import axios from "axios";
import config from '../../config';


const Connexion = () => {

    const [password, setPassword] = useState("");
    const [pseudo, setEmail] = useState("");

    const handleSubmit = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        await axios
            .post(config.url.root_api + config.url.auth.connexion+"?pseudo="+pseudo+"&mdp="+password)
            .then(response =>{
                localStorage.setItem("username", pseudo);
                localStorage.setItem("token", response.data);
                window.location.reload();
            })
            .catch(error =>{
                console.log("Erreur lors de l'authentification : ", error);           
                alert("Erreur lors de l'authentification")
            });
    }

    const handleClick = () => {
        if(!(localStorage.getItem("register")===("yes"))){
            localStorage.setItem("register","yes")
       }
       window.location.reload()

    }

    return (
        <div className='pageConnexion'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

            <form className="form-container" onSubmit={handleSubmit}>
                 <div className="img-form-connexion">
     
                 </div>
     
                 <div className="form">
                     <h3 className="title-form">Connexion</h3>
     
                     <div className="field">
                         <label htmlFor="login">Login</label>
                         <input type="text" name="login" id="login" required value={pseudo} onChange={(event) => setEmail(event.target.value)}/>
                     </div>
                     
                     <div className="field">
                         <label htmlFor="pwd">Mot de Passe</label>
                         <input type="password" name="pwd" id="pwd" required value={password} onChange={(event) => setPassword(event.target.value)}/>
                     </div>

                     <div className="buttonContainer">
                         <button className="connexionButton" type="submit">Connexion</button>
                     </div>
                     
                     
                 </div>

            </form>

                <div className="inscription-link">
                    <Link to="" onClick={handleClick}>
                        Pas encore inscrit ? Inscrivez vous ici.
                    </Link>
                </div>
            </div>
        </div>
       
    );
}

export default Connexion;