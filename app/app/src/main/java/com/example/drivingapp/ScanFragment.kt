package com.example.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.time.LocalTime
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.drivingapp.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class ScanFragment : Fragment(R.layout.fragment_scan), SensorEventListener, LocationListener {
    lateinit var myApplication: MyApplication
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private lateinit var locationManager: LocationManager
    private lateinit var accelerometerDataTextView: TextView
    private lateinit var locationDataTextView: TextView
    private lateinit var speedTextView: TextView
    private lateinit var buttonStart: Button
    private  var firstLocation: Location? = null
    private var lastLocation: Location? = null
    private var speedAVG:Double =0.0
    private var updatedLocation:Int=0
    private var avgX:Double=0.0
    private var avgY:Double=0.0
    private var avgZ:Double=9.0
    private var updatedGyroscope:Int=0
    data class SensorData(
        var location: String,
        var speed: String,
        var accelerometer: String,
        var user: String,
        var score: Double
    )
    lateinit var sensorData:SensorData

    val gson = Gson()
    //val client = OkHttpClient()
    //change
    val request = Request.Builder().url("ws://192.168.1.212:8080").build()
    // val webSocket = client.newWebSocket(request, listener)

    var listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            println("WebSocket connection opened")
            webSocket.send("Hello, server!")
        }


        override fun onMessage(webSocket: WebSocket, text: String) {
            println("Received message: $text")

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            println("WebSocket connection failed: ${t.message}")
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_INTERVAL = 1000L // 1 second
        private const val MIN_DISTANCE_CHANGE = .1f // 1 meter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication
        sensorData=SensorData("","","",myApplication.user.getString("username"),0.0)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Check and request location permission if needed
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize sensor manager
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Get the accelerometer and gyroscope sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        // Initialize the TextViews
        accelerometerDataTextView = view.findViewById(R.id.accelerometerDataTextView)
        locationDataTextView = view.findViewById(R.id.locationDataTextView)
        speedTextView = view.findViewById(R.id.speedDataTextView)

        // Initialize the button
        buttonStart = view.findViewById(R.id.buttonStart)
        buttonStart.text = "Start"
        buttonStart.setOnClickListener {
            startSensors()
        }
       /* if (myApplication.loggedIn==false)
        {
            buttonStart.text="login to enable this button"
            buttonStart.isEnabled=false
        }
*/
        // Set initial visibility of TextViews to GONE
        accelerometerDataTextView.visibility = View.GONE
        locationDataTextView.visibility = View.GONE
        speedTextView.visibility = View.GONE
    }

    private var isScanning = false


    private fun generateRoad(xStart: Double, yStart: Double, xEnd: Double, yEnd: Double)  {

        var statOfRoad:Int =100
        var state:Int=0
        if((avgX>2) or(avgX<-2))
            statOfRoad-20
        if((avgY>2) or(avgY<-2))
            statOfRoad-10
        if(avgZ<9)
            statOfRoad-30
        if(speedAVG<30)
            statOfRoad-40

        if(statOfRoad<40)
            state=0
        else if((statOfRoad>40) or(statOfRoad<70))
            state=1
        else if(statOfRoad<70)
            state=2

        myApplication.sendRoad(xStart,yStart,xEnd,yEnd,state)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun sendDataServer(sensorData: SensorData) {
        val json = JSONObject()
        json.put("location", sensorData.location)
        json.put("speed", sensorData.speed)
        json.put("accelerometer", sensorData.accelerometer)
        json.put("user", sensorData.user)
        json.put("score", sensorData.score)
        myApplication.sendRawData(sensorData.location,sensorData.speed,sensorData.accelerometer,sensorData.score)
        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request: Request = Request.Builder()
            .url("http://164.8.162.186:3002")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("ni poslalo")
                println("error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                println(" poslalo")
            }
        })
    }

    private fun startSensors() {


        if (!isScanning) {
            startAccelerometer()
            startGyroscope()
            startLocationUpdates()

            // Start scanning


            // Update TextViews with initial sensor values
            accelerometerDataTextView.text = "X: 0.0\nY: 0.0\nZ: 0.0"
            locationDataTextView.text = "Latitude: 0.0\nLongitude: 0.0"
            speedTextView.text = "Speed: 0.0 km/h"
            buttonStart.text = "Stop"

            // Show TextViews
            accelerometerDataTextView.visibility = View.VISIBLE
            locationDataTextView.visibility = View.VISIBLE
            speedTextView.visibility = View.VISIBLE
        } else {
            stopSensors()
            buttonStart.text = "Start"

            accelerometerDataTextView.visibility = View.GONE
            locationDataTextView.visibility = View.GONE
            speedTextView.visibility = View.GONE
        }

        isScanning = !isScanning



    }

    private fun startAccelerometer() {
        accelerometer?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun startGyroscope() {
        gyroscope?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun startLocationUpdates() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if the permission is still granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_INTERVAL,
                MIN_DISTANCE_CHANGE,
                this
            )
            firstLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            println("first loction: ${firstLocation}")
        }
    }

    private fun stopSensors() {
        println("called stop sensors")
        //webSocket.close(1000, "Goodbye")
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
        speedAVG /= updatedLocation
        avgX /= updatedGyroscope
        avgY /= updatedGyroscope
        avgZ /= updatedGyroscope
        firstLocation?.let {
            println("got to here1")
            lastLocation?.let { it1 ->
                println("got to here")
                lifecycleScope.launch(Dispatchers.Default) {
                    generateRoad(
                        it.latitude,
                        firstLocation!!.longitude, lastLocation!!.latitude, it1.longitude
                    )
                }
            }
        }
            }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        lastLocation = location
        println(lastLocation)
        val locationData = "Latitude: $latitude\nLongitude: $longitude"
        locationDataTextView.text = locationData
        updatedLocation += 1
        val speed = location.speed // speed in meters/second
        val speedKmPerHour = speed * 3.6 // convert to kilometers/hour
        speedAVG += speedKmPerHour
        val speedData = "Speed: $speedKmPerHour km/h"
        speedTextView.text = speedData
        sensorData.location=locationData
        sensorData.speed=speedData
        // Send location data and speed as JSON
        GlobalScope.launch {
            try {
                sendDataServer(sensorData)
            } catch (e: Exception){
                println("error:$e")
            }
        }
    }
    override fun onResume() {
        super.onResume()
        startAccelerometer()
        startGyroscope()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    avgX += x
                    avgY += y
                    avgZ += z
                    updatedGyroscope++
                    val accelerometerData = "X: $x\nY: $y\nZ: $z"
                    sensorData.accelerometer=accelerometerData

                    accelerometerDataTextView.text = accelerometerData
                }
            }
        }
    }
}
