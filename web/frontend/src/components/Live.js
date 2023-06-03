import React, {useEffect, useState} from 'react';

function Live() {

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
    <div >
    
    </div>
  );
}

export default Live;
