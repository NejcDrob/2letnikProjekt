import { Link } from "react-router-dom";
import { useState } from "react";

function Road(props) {
    return (
        <>
            <div >
                <div >
                    <h5 >{props.road.state}</h5>
                </div>
                <h6 >Objavil: {props.road.postedBy} </h6>
            </div>
        </>
    );
}

export default Road;
//<Link class="nav-link text-white" to='/PhotoPost' photo={props.photo}>Podrobno</Link>