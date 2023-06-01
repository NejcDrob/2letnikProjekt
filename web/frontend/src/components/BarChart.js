import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';

function BarChart() {
  const [roadList, setRoadList] = useState([]);
  const [profile, setProfile] = useState({});

  useEffect(() => {
    const getProfile = async function() {
      const res = await fetch("http://localhost:3001/users/profile", {credentials: "include"});
      const data = await res.json();
      setProfile(data);
    }
    getProfile();
  }, []);

  useEffect(() => {
    if (profile.username) {  // only fetch data if profile.username is set
      const fetchData = async () => {
        try {
          const res = await fetch('http://localhost:3001/roads');
          const data = await res.json();
          const userRoads = data.filter(road => road.postedBy === profile.username)
          setRoadList(userRoads)
          console.log(userRoads)
        } catch (error) {
          console.error('Error fetching road data:', error);
        }
      };

      fetchData();
    }
  }, [profile]);  // add profile to the dependency array

  const stateCounts = [0, 0, 0];
  roadList.forEach((road) => {
    const state = road.state;
    if (state >= 0 && state <= 2) {
      stateCounts[state]++;
    }
  });

  const chartData = {
    labels: ['Bad', 'OK', 'Good'],
    datasets: [
      {
        label: 'State Count',
        data: stateCounts,
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  return (
    <div>
      <h2>Bar Chart</h2>
      <Bar data={chartData} />
    </div>
  );
}

export default BarChart;
