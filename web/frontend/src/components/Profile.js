import { useContext, useEffect, useState } from 'react';
import { UserContext } from '../userContext';
import { Navigate } from 'react-router-dom';
import Road from './Road';
import BarChart from './BarChart';
function Profile(){
    const userContext = useContext(UserContext); 
    const [profile, setProfile] = useState({});
    const [roadList, setRoadList] = useState([])
    useEffect(()=>{
      const getRoadList=async function(){
        const res=await fetch("http://localhost:3001/roads")
        const data=await res.json()
        const userRoads = data.filter(road => road.postedBy === profile.username)
        setRoadList(userRoads)
        console.log(userRoads)
      }
      getRoadList()
    }, [])
  
    useEffect(function(){
        const getProfile = async function(){
            const res = await fetch("http://localhost:3001/users/profile", {credentials: "include"});
            const data = await res.json();
            setProfile(data);
        }
        getProfile();
    }, []);

    return (
        <>
            {!userContext.user ? <Navigate replace to="/login" /> : ""}
            <h1>User profile</h1>
            <p>Username: {profile.username}</p>
            <p>Email: {profile.email}</p>
            <div>
            {roadList.map(road => (   
                <Road road={road} key={road._id}></Road>
            ))}
        </div>
         
        </>
        
    );
}

export default Profile;