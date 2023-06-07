import React, { useEffect, useState } from 'react';
import Road from './Road';
import Map from './Map';

function Roads() {
  const [roadList, setRoadList] = useState([]);

  useEffect(() => {
    const getRoadList = async () => {
      const res = await fetch("http://localhost:3001/roads");
      const data = await res.json();
      setRoadList(data);
      console.log(data);
    };

    // Fetch roadList initially
    getRoadList();

    // Refresh roadList every 1 second
    const interval = setInterval(getRoadList, 1000);

    // Clean up the interval on component unmount
    return () => {
      clearInterval(interval);
    };
  }, []);

  return (
    <div className='roads'>
      <Map />
      <div className="d-flex flex-column roads-list">
        {roadList.map((road) => (
          <Road road={road} key={road._id}></Road>
        ))}
      </div>
    </div>
  );
}

export default Roads;
