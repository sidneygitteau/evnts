import './CardInscription.scss';

function CardInscription() {
    return (
    
       <div className="form-container">
            <div className="img-form-inscription">

            </div>

            <div className="form">
                <h3 className="title-form-inscription">Inscription</h3>

                <div className="field">
                    <label htmlFor="email">Email</label>
                    <input type="text" name="email" id="email" />
                </div>

                <div className="field">
                    <label htmlFor="login">Login</label>
                    <input type="text" name="login" id="login" />
                </div>
                
                <div className="field">
                    <label htmlFor="pwd">Mot de Passe</label>
                    <input type="password" name="pwd" id="pwd" />
                </div>

                <div className="buttonContainer">
                    <button className="inscriptionButton" type="submit">Inscription</button>
                </div>
                
                
            </div>
       

       
    </div>
    );
}

export default CardInscription;