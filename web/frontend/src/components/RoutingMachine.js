import L from "leaflet"
import { createControlComponent } from "@react-leaflet/core"
import "leaflet-routing-machine"

const createRoutineMachineLayer = ({road}) => {
    const color = 
        road.state === 2 ? "green" : road.state === 1 ? "yellow" : "red"

    const startPoint = L.latLng(road.xStart, road.yStart);
    const endPoint = L.latLng(road.xEnd, road.yEnd);

    const instance = L.Routing.control({
        waypoints: [startPoint, endPoint],
        lineOptions: {
            styles: [{ color: color, weight: 8 }]
        },
        show: false,
        addWaypoints: false,
        routeWhileDragging: false,
        draggableWaypoints: false,
        fitSelectedRoutes: false,
        showAlternatives: false,
        altLineOptions: { styles: [{opacity: 0}] },
        createMarker: function () {
            return null
        }
    })

    return instance
}

const RoutingMachine = createControlComponent(createRoutineMachineLayer)

export default RoutingMachine
