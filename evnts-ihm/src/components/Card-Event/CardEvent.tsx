import './CardEvent.scss';

function CardEvent(){
    return(
        <div className="cardEvent">
            <div className="titleEvent">
                <span className="labelTitleEvent">nom_evenement</span>
            </div>

            <div className="dateEvent fieldCardEvent">
                <span className="labelDateEvent">date_event, heure_event</span>
            </div>

            <div className="locationEvent fieldCardEvent">
                <span className="labelLocationEvent">lieu_event,ville</span>
            </div>

            <div className="descriptionEvent fieldCardEvent">
                <span className="labelDescriptionEvent">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut </span>
            </div>
        </div>
    );
}

export default CardEvent;