import { Link } from "react-router-dom";
import { useState } from "react";

function Road(props) {
    return (
        <>
<div className="card d-inline-block">
  <div className="card-body">
    <h5 className="card-title">{props.road.state}</h5>
    <h6 className="card-subtitle mb-2 text-muted">Objavil: {props.road.postedBy}</h6>
  </div>
</div>


        </>
    );
}

export default Road;
//<Link class="nav-link text-white" to='/PhotoPost' photo={props.photo}>Podrobno</Link>