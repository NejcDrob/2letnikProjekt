
import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import RoutingMachine from "./RoutingMachine";
import "leaflet/dist/leaflet.css"
import "leaflet-routing-machine"
import L from 'leaflet';

function MyMap() {
  const position = [46.638587, 15.615779];
  const [roadList, setRoadList] = useState([]);
  const mapRef = React.createRef();
  const markerIcon = L.icon({
    iconUrl: './images/icon3.png',
    iconSize: [30, 30], // Adjust the size of the icon
  });

  useEffect(() => {
    const getRoadList = async function () {
      const res = await fetch("http://localhost:3001/roads");
      const data = await res.json();
      setRoadList(data);
      console.log(data);
    };
    getRoadList();
  }, []);

  useEffect(() => {
    const map = mapRef.current;
    if (map) {
      setTimeout(() => {
        map.invalidateSize(); // Refresh the map to ensure correct rendering
      }, 0);
    }
  }, [roadList]);


  return (
  <div className="map">
     <MapContainer
      center={position}
       zoom={13}
       style={{ height: "100%", width: "100%" }}
      ref={mapRef}
      >
       <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {roadList.map((road) => (
          <RoutingMachine road={road} />
        ))}
      </MapContainer>
    </div>
  );
}

export default MyMap;
