import React, {useEffect, useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import Road from './Road';
function MyMap() {
  const position = [46.55472, 15.64667]; 
  const [roadList, setRoadList] = useState([])
  useEffect(()=>{
    const getRoadList=async function(){
      const res=await fetch("http://localhost:3001/roads")
      const data=await res.json()
      setRoadList(data)
      console.log(data)
    }
    getRoadList()
  }, [])

  return (
    <div className='map'>
      <MapContainer center={position} zoom={13} style={{ height: "100%", width: "100%" }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <Marker position={position}>
          <Popup>
            A pretty CSS3 popup. <br /> Easily customizable.
          </Popup>
        </Marker>
      </MapContainer>
      <div>
            {roadList.map(road => (
                    <>
                    <Road road={road} key={road._id}></Road>
                    <hr />
                </>
            ))}
        </div>
    </div>
  );
}

export default MyMap;
