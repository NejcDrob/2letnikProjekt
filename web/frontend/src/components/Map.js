import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import RoutingMachine from "./RoutingMachine";

function MyMap() {
  const position = [51.505, -0.09];
  const [roadList, setRoadList] = useState([]);
  const mapRef = useRef(null);

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
        <Marker position={position}>
          <Popup>A pretty CSS3 popup. <br /> Easily customizable.</Popup>
        </Marker>
        {roadList.map((road, index) => (
          <RoutingMachine key={index} road={road} weight={5} />
        ))}
      </MapContainer>
    </div>
  );
}

export default MyMap;
