import { Link } from "react-router-dom";
import { useState } from "react";

function Road(props) {
    return (
        <>
            <div className="card bg-dark text-white mb-2 border border-dark border-2 w-50 ms-5">
                <div className="card-img-overlay">
                    <h5 className="card-title border-rounded rounded d-inline bg-dark border border-dark border-2">{props.road.state}</h5>
                </div>
                <h6 className="border-rounded text-white rounded d-inline bg-dark border border-dark border-2">Objavil: {props.road.postedBy} </h6>
            </div>
        </>
    );
}

export default Road;
//<Link class="nav-link text-white" to='/PhotoPost' photo={props.photo}>Podrobno</Link>