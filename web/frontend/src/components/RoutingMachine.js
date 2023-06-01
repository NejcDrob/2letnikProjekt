import L from "leaflet";
import { createControlComponent } from "@react-leaflet/core";
import "leaflet-routing-machine";

const createRoutingMachineLayer = ({ road, weight, map }) => {
  const color =
    road.color === 0 ? "green" : road.color === 1 ? "yellow" : "red";

  const startPoint = L.latLng(road.startPointLAT, road.startPointLNG);
  const endPoint = L.latLng(road.endPointLAT, road.endPointLNG);

  const waypoints = [
    L.Routing.waypoint(startPoint),
    L.Routing.waypoint(endPoint)
  ];

  const routingMachineLayer = L.Routing.control({
    waypoints: waypoints,
    lineOptions: {
      styles: [{ color: color, weight: weight }],
    },
    show: false,
    addWaypoints: false,
    routeWhileDragging: false,
    draggableWaypoints: false,
    fitSelectedRoutes: false,
    showAlternatives: false,
    altLineOptions: { styles: [{ opacity: 0 }] },
    createMarker: function () {
      return null;
    },
  });

  return routingMachineLayer.getPlan();
};

const RoutingMachine = createControlComponent(createRoutingMachineLayer);

export default RoutingMachine;
