import { useContext } from "react";
import { UserContext } from "../userContext";
import { Link } from "react-router-dom";

function Header(props) {
    return (
        <header className="header">
            <nav class="navbar navbar-expand bg-dark navbar-dark text-white fixed-top">
                <div class="container-fluid">
                    <a class="navbar-brand nav-link" href="/">{props.title}</a>
                    <div class="collapse navbar-collapse justify-content-end">
                        <ul class="navbar-nav">
                    <li class="nav-item"><Link class="nav-link" to='/'>Home</Link></li>
                    <li class="nav-item"><Link  class="nav-link" to='/Live'>Live</Link></li>

                    <UserContext.Consumer>
                        {context => (
                            context.user ?
                                <>
                                
                                    <li class="nav-item"><Link  class="nav-link" to='/profile'>Profile</Link></li>
                                    <li class="nav-item"><Link  class="nav-link" to='/logout'>Logout</Link></li>
                                </>
                            :
                                <>
                                    <li class="nav-item"><Link  class="nav-link" to='/login'>Login</Link></li>
                                    <li class="nav-item"><Link   class="nav-link" to='/register'>Register</Link></li>
                                </>

                        )}
                    </UserContext.Consumer>
                    </ul>
                    </div>
                </div>
            </nav>
        </header>
    );
}

export default Header;