import { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { UserContext } from "./userContext";
import Header from "./components/Header";
import Login from "./components/Login";
import Register from "./components/Register";
import Profile from "./components/Profile";
import Logout from "./components/Logout";
import Map from "./Map/Map";

function App() {
  /**
   * Podatek o tem, ali je uporabnik prijavljen ali ne, bomo potrebovali v vseh komponentah.
   * State je dosegljiv samo znotraj trenutne komponente. Če želimo deliti spremenljivke z
   * ostalimi komponentami, moramo uporabiti Context.
   * Vsebino Contexta smo definirali v datoteki userContext.js. Poleg objekta 'user', potrebujemo
   * še funkcijo, ki bo omogočala posodabljanje te vrednosti. To funkcijo definiramo v komponenti App
   * (updateUserData). V render metodi pripravimo UserContext.Provider, naš Context je potem dosegljiv
   * v vseh komponentah, ki se nahajajo znotraj tega providerja.
   * V komponenti Login ob uspešni prijavi nastavimo userContext na objekt s trenutno prijavljenim uporabnikom.
   * Ostale komponente (npr. Header) lahko uporabijo UserContext.Consumer, da dostopajo do prijavljenega
   * uporabnika.
   * Context se osveži, vsakič ko osvežimo aplikacijo v brskalniku. Da preprečimo neželeno odjavo uporabnika,
   * lahko context trajno hranimo v localStorage v brskalniku.
   */
  const [user, setUser] = useState(localStorage.user ? JSON.parse(localStorage.user) : null);
  const updateUserData = (userInfo) => {
    localStorage.setItem("user", JSON.stringify(userInfo));
    setUser(userInfo);
  }
    //new code
  const [coords, setCorrds] = useState({
    latitude: "",
    longitude: ""
  });
  const [display_name, setName] = useState("");
  const [address, setAddress] = useState({});

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      getCurrentCityName,
      error,
      options
    );
  }, []);

  function error(err) {
    if (
      err.code === 1 || //if user denied accessing the location
      err.code === 2 || //for any internal errors
      err.code === 3 //error due to timeout
    ) {     
      alert(err.message);
    } else {
      alert(err);
    }
  }

  const options = {
    enableHighAccuracy: true,
    maximumAge: 30000,
    timeout: 27000
  };

  //get current location when the app loads for the first time
  function getCurrentCityName(position) {
    setCorrds({
      latitude: position.coords.latitude,
      longitude: position.coords.longitude
    });

    let url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2" +
      "&lat=" + coords.latitude + "&lon=" + coords.longitude;
      
    fetch(url, {
      method: "GET",
      mode: "cors",
      headers: {
        "Access-Control-Allow-Origin": "https://o2cj2q.csb.app"
      }
    })
      .then((response) => response.json())
      .then((data) => setName(data.display_name));
  }

  //get input from text fields and append it to address object
  function update(field) {
    return (e) => {
      const value = e.currentTarget.value;
      setAddress((address) => ({ ...address, [field]: value }));
    };
  }

  //send the data on the state to the API
  function getData(url) {
    fetch(url, {
      method: "POST",
      mode: "cors",
      headers: {
        "Access-Control-Allow-Origin": "https://o2cj2q.csb.app"
      }
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
      })
      .then((data) => {
        setName(data[0].display_name);
        setCorrds({
          latitude: data[0].lat,
          longitude: data[0].lon
        });
      })
      .catch(() => error("Please Check your input"));
  }

  //set form input( data entered ) to state on form submit
  function submitHandler(e) {
    e.preventDefault();
    console.log(address);

    let url = `https://nominatim.openstreetmap.org/search?
    street=${address.street}
    &city=${address.city}
    &state=${address.state}
    &country=${address.country}
    &postalcode=${address.postalcode}&format=json`;

    getData(url);
  } 

  /**
   * Na vrhu vključimo komponento Header, z naslovom in menijem.
   * Nato vključimo Router, ki prikaže ustrezno komponento v odvisnosti od URL naslova.
   * Pomembno je, da za navigacijo in preusmeritve uporabljamo komponenti Link in Navigate, ki sta
   * definirani v react-router-dom modulu. Na ta način izvedemo navigacijo brez osveževanja
   * strani. Klasične metode (<a href=""> in window.location) bi pomenile osvežitev aplikacije
   * in s tem dodatno obremenitev (ponovni izris komponente Header, ponastavitev Contextov,...)
   */
  return (
    <BrowserRouter>
      <UserContext.Provider value={{
        user: user,
        setUserContext: updateUserData
      }}>
        <div className="App">
          <Header title="My application"></Header>
          <Routes>
            <Route path="/login" exact element={<Login />}></Route>
            <Route path="/register" element={<Register />}></Route>
            <Route path="/profile" element={<Profile />}></Route>
            <Route path="/logout" element={<Logout />}></Route>
          </Routes>
          <h1>Enter The address</h1>
    <section className="form-container">
      <form>
        <label>street:</label>
        <input
          value={address.street}
          placeholder="1234 abc street"
          onChange={update("street")}
          id="street"
          type="text"
        />
        <label>city:</label>
        <input
          placeholder="Los Angeles"
          type="text"
          value={address.city}
          onChange={update("city")}
          id="city"
        />
        <br />
        <label>State:</label>
        <input
          placeholder="CA / California"
          type="text"
          value={address.state}
          onChange={update("state")}
          id="state"
        />
        <label>zip code:</label>
        <input
          placeholder="91423"
          type="text"
          value={address.postalcode}
          onChange={update("postalcode")}
          id="postalcode"
        />
        <br />
        <label>Country:</label>
        <input
          placeholder="USA"
          type="text"
          value={address.country}
          onChange={update("country")}
          id="country"
        />
        <br />

        <button onClick={(e) => submitHandler(e)}>Search</button>
      </form>
    </section>
    <Map coords={coords} dispaly_name={display_name} />
        </div>
      </UserContext.Provider>
    </BrowserRouter>
  );
}

export default App;
