import { Link } from "react-router-dom";
import { useState } from "react";

function Road(props) {
    // helper function to convert state number to string
    const getStateString = (stateNumber) => {
        switch(stateNumber) {
            case 0:
                return 'Bad';
            case 1:
                return 'OK';
            case 2:
                return 'Good';
            default:
                return '';
        }
    }

    return (
        <>
        <div className="card d-inline-block">
            <div className="card-body">
                <h5 className="card-title">Status: {getStateString(props.road.state)}</h5>
                <h6 className="card-subtitle mb-2 text-muted">Objavil: {props.road.postedBy}</h6>
            </div>
        </div>
        </>
    );
}

export default Road;
