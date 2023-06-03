import React, {useEffect, useState} from 'react';
import Road from './Road';
import  Map  from './Map';
function Roads() {

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
    <div className='roads'>
    <Map />
    <div className="d-flex flex-column roads-list">
      <p className='title'><b>Objave</b></p>
      {roadList.map(road => (   
          <Road road={road} key={road._id}></Road>
      ))}
    </div>

    </div>
  );
}

export default Roads;
