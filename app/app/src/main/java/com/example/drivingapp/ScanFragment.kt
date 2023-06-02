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

class ScanFragment : Fragment(R.layout.fragment_scan), SensorEventListener, LocationListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private lateinit var locationManager: LocationManager
    private lateinit var accelerometerDataTextView: TextView
    private lateinit var locationDataTextView: TextView
    private lateinit var speedTextView: TextView
    private lateinit var buttonStart: Button

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_INTERVAL = 1000L // 1 second
        private const val MIN_DISTANCE_CHANGE = 1.0f // 1 meter
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

        // Set initial visibility of TextViews to GONE
        accelerometerDataTextView.visibility = View.GONE
        locationDataTextView.visibility = View.GONE
        speedTextView.visibility = View.GONE
    }

    private var isScanning = false

    private fun startSensors() {
        if (!isScanning) {
            startAccelerometer()
            startGyroscope()
            startLocationUpdates()
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

            // Hide TextViews
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
        }
    }

    private fun stopSensors() {
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        val locationData = "Latitude: $latitude\nLongitude: $longitude"
        locationDataTextView.text = locationData

        val speed = location.speed // speed in meters/second
        val speedKmPerHour = speed * 3.6 // convert to kilometers/hour

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

                    val accelerometerData = "X: $x\nY: $y\nZ: $z"
                    accelerometerDataTextView.text = accelerometerData
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // Update TextViews with gyroscope values
                    // Replace the following lines with your desired implementation
                    val gyroscopeData = "X: $x\nY: $y\nZ: $z"
                    // Update the corresponding TextView with gyroscopeData
                }
            }
        }
    }
}