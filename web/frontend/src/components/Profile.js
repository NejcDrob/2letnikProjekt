import { useContext, useEffect, useState } from 'react';
import { UserContext } from '../userContext';
import { Navigate } from 'react-router-dom';
import Road from './Road';
import BarChart from './BarChart';
function Profile(){
    const userContext = useContext(UserContext); 
    const [profile, setProfile] = useState({});
    const [roadList, setRoadList] = useState([])
  
    useEffect(function(){
        const getProfile = async function(){
            const res = await fetch("http://localhost:3001/users/profile", {credentials: "include"});
            const data = await res.json();
            setProfile(data);
        }
        getProfile();
    }, []);

    useEffect(() => {
        const getRoadList = async function() {
          const res = await fetch("http://localhost:3001/roads");
          const data = await res.json();
          const userRoads = data.filter(road => road.postedBy === profile.username);
          setRoadList(userRoads);
          console.log(userRoads);
        };
        getRoadList();
      }, [profile]);

    return (
        <>
            {!userContext.user ? <Navigate replace to="/login" /> : ""}
            <div className="d-flex justify-content-center">
    <div className="d-inline-block">
        <div className="card">
            <div className="card-body text-center">
                <h1 className="card-title display-4">User profile</h1>
                <p className="card-text h5">Username: {profile.username}</p>
                <p className="card-text h5">Email: {profile.email}</p>
            </div>

        </div>
    </div>
</div>
<div className="d-flex flex-column roads-list">
    {roadList.map(road => (
        <Road road={road} key={road._id}></Road>
    ))}
</div>
        </>
        
    );
}

export default Profile;