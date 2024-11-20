import './CardConnexion.scss';

function CardConnexion() {
    return (
    
       <div className="form-container">
            <div className="img-form-connexion">

            </div>

            <div className="form">
                <h3 className="title-form">Connexion</h3>

                <div className="field">
                    <label htmlFor="login">Login</label>
                    <input type="text" name="login" id="login" />
                </div>
                
                <div className="field">
                    <label htmlFor="pwd">Mot de Passe</label>
                    <input type="password" name="pwd" id="pwd" />
                </div>

                <div className="buttonContainer">
                    <button className="connexionButton" type="submit">Connexion</button>
                </div>
                
                
            </div>
       

       
    </div>
    );
}

export default CardConnexion;