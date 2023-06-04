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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.app.databinding.ActivityMainBinding
import com.example.drivingapp.LogInFragment
import com.example.drivingapp.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.bson.Document
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
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
    private var updatedGyroscope:Int=0;
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_INTERVAL = 1000L // 1 second
        private const val MIN_DISTANCE_CHANGE = 1.0f // 1 meter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication

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
        }*/

        // Set initial visibility of TextViews to GONE
        accelerometerDataTextView.visibility = View.GONE
        locationDataTextView.visibility = View.GONE
        speedTextView.visibility = View.GONE
    }

    private var isScanning = false
    data class Road(
        val xStart: Double,
        val yStart: Double,
        val xEnd: Double,
        val yEnd: Double,
        val state: Int,
        val postedBy: String
    )

    private fun generateRoad(xStart: Double, yStart: Double, xEnd: Double, yEnd: Double): Road {

        var statOfRoad:Int =100
        var state:Int=0
        if((avgX>2) or(avgX<-2))
            statOfRoad-20
        if((avgY>2) or(avgY<-2))
            statOfRoad-10
        if(avgY<9)
            statOfRoad-30
        if(speedAVG<30)
            statOfRoad-40
        if(statOfRoad<40)
            state=0
        else if((statOfRoad>40) or(statOfRoad<70))
            state=1
        else if(statOfRoad<70)
            state=2

        val road = Road(xStart,yStart,xEnd,yEnd,state,"postedBy")
        return road
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
        }
    }

    private fun stopSensors() {
        println("called stop sensors")

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
                    myApplication.sendRoad(
                        it.latitude,
                        firstLocation!!.longitude, lastLocation!!.latitude, it1.longitude, 2
                    )
                }
            }
        }
            }
    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        lastLocation = location
        println(lastLocation)
        val locationData = "Latitude: $latitude\nLongitude: $longitude"
        locationDataTextView.text = locationData
        updatedLocation+=1
        val speed = location.speed // speed in meters/second
        val speedKmPerHour = speed * 3.6 // convert to kilometers/hour
        speedAVG+=speedKmPerHour
        val speedData = "Speed: $speedKmPerHour km/h"
        speedTextView.text = speedData

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
                    avgX+=x
                    avgY+=y
                    avgZ+=y
                    updatedGyroscope++
                    val accelerometerData = "X: $x\nY: $y\nZ: $z"
                    accelerometerDataTextView.text = accelerometerData
                }

            }
        }
    }

}
