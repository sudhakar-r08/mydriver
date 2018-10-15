package com.techgimmicks.mydriver

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event);
            }
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {

             /*   val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val diff = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                if (diff > 0.5)
                // 0.5 is a threshold, you can test it and change it
                    Log.d("MainActivity", "Device motion detected!!!!")

                    onMotionDetected(event, diff)

                mMotionDetectionTextView.setText("Acceleration: ["+String.format("%.3f",event.values[0])+","+String.format("%.3f",event.values[1])+","+String.format("%.3f",event.values[2])+"] "+String.format("%.3f", acceleration));
                if (diff > SettingsHelper.getInstance().getMotionDetectionThreshold()){
                    mMotionDetectionTextView.setTextColor(Color.RED);
                } else {
                    mMotionDetectionTextView.setTextColor(Color.WHITE);
                }
*/
            }
        }
    }

    private fun onMotionDetected(event: SensorEvent, diff: Float) {


    }

    private lateinit var sensorManager: SensorManager
    private var color = false
    private lateinit var view: TextView
    private var lastUpdate: Long = 0

    private val mListeners = HashSet<SensorEventListener>()

    /*   var lastLocation: android.location.Location? = null
       var lastPreviousLocation: android.location.Location? = null
       private lateinit var locationManager: LocationManager
       private var currentSpeed: Double = 0.0
       private var previousSpeed: Double = 0.0

       val MY_PERMISSIONS_REQUEST_LOCATION = 99*/


    override fun onCreate(savedInstanceState: Bundle?) {
        /*  requestWindowFeature(Window.FEATURE_NO_TITLE)
          getWindow().setFlags(
              WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN
          )*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.textView) as TextView;
        //  view.setBackgroundColor(Color.GREEN);

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()
        // locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //  checkLocationPermission()

    }


    override fun onResume() {
        super.onResume()
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        );
        /*  if (checkLocationPermission())
          // Register the listener with the Location Manager to receive location updates
              locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)*/
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this);
        //  locationManager.removeUpdates(this)
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = event.timestamp


        if (accelationSquareRoot >= 2)
        //
        {
            if (actualTime - lastUpdate < 300) {
                return
            }
            lastUpdate = actualTime


            setImage(ivSmile, R.drawable.ic_scared)
            view.text = ("Hard Driving")
            /*  if (previousSpeed < currentSpeed) {
                  setImage(ivSmile, R.drawable.ic_scared)

                  view.text = ("Hard Acceleration")
              } else if (previousSpeed > currentSpeed) {
                  setImage(ivSmile, R.drawable.ic_crying)
                  // ivSmile.setImageDrawable(resources.getDrawable(R.drawable.ic_crying))
                  view.text = ("Hard Brakes")

              }*/
        } else {
            // ivSmile.setImageDrawable(resources.getDrawable(R.drawable.ic_smile))
            //  setImage(ivSmile, R.drawable.ic_smile)
            view.text = ("Smooth Driving")
            setImage(ivSmile, R.drawable.ic_smile)
            /* if (previousSpeed < currentSpeed) {
                 view.text = ("Smooth Acceleration")
             } else if (previousSpeed > currentSpeed) {
                 view.text = ("Smooth Brakes")
             }*/
        }
        /*  Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
              .show()
          if (color) {
              view.setBackgroundColor(Color.GREEN)
          } else {
              view.setBackgroundColor(Color.RED)
          }
          color = !color*/

    }

    fun setImage(imageView: ImageView, imgResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(resources.getDrawable(imgResource, null))
        } else {
            imageView.setImageDrawable(resources.getDrawable(imgResource))
        }
    }


    /*   var recentGPSLocationSegments = listOf<Pair<android.location.Location, android.location.Location>>()

      fun applyWeightedMovingAverageSpeed(
          location: android.location.Location,
          previous: android.location.Location
      ): Double {
          recentGPSLocationSegments += Pair(location, previous)
          val cachedLocationsNs =
              location.elapsedRealtimeNanos - 500000000 // .5 seconds, This will typically get 4 entries (1 second apart)
          val targetZeroWeightNs =
              location.elapsedRealtimeNanos - 1000000000 // 5.0 seconds, Weights will be approx 5000000000, 4000000000, 3000000000, 1000000000

          // Toss old locations
          recentGPSLocationSegments =
                  recentGPSLocationSegments.filter { it -> it.first.elapsedRealtimeNanos > cachedLocationsNs }

          // Total up all the weights. Weight is based on age, younger has higher weight
          val weights = recentGPSLocationSegments.map { it.first.elapsedRealtimeNanos - targetZeroWeightNs }.sum()

          // Apply the weights and get average speed in meters/second
          return recentGPSLocationSegments.map {
              speedFromGPS(
                  it.first,
                  it.second
              ) * (it.first.elapsedRealtimeNanos - targetZeroWeightNs)
          }.sum() / weights
      }

      fun speedFromGPS(location: android.location.Location, previous: android.location.Location): Double {
          val dist = location.distanceTo(previous)
          val time = (location.elapsedRealtimeNanos - previous.elapsedRealtimeNanos) / 100000000.0
          return dist / time
      }


     override fun onLocationChanged(location: Location?) {
          if (location != null) {
              previousSpeed = currentSpeed
              if (lastPreviousLocation != null) {
                  currentSpeed = applyWeightedMovingAverageSpeed(location, lastPreviousLocation!!)

                  lastPreviousLocation = lastLocation
              }

              lastLocation = location

              if (currentSpeed < 0.0) {
                  currentSpeed = 0.0
              }
          }
      }

      override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

      }

      override fun onProviderEnabled(provider: String?) {

      }

      override fun onProviderDisabled(provider: String?) {

      }

      fun checkLocationPermission(): Boolean {
          if (ContextCompat.checkSelfPermission(
                  this,
                  Manifest.permission.ACCESS_FINE_LOCATION
              ) != PackageManager.PERMISSION_GRANTED
          ) {
              ActivityCompat.requestPermissions(
                  this,
                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                  MY_PERMISSIONS_REQUEST_LOCATION
              )
              return false
          } else {
              return true
          }
      }

      override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

          when (requestCode) {
              MY_PERMISSIONS_REQUEST_LOCATION -> {
                  // If request is cancelled, the result arrays are empty.
                  if (grantResults.size > 0
                      && grantResults[0] == PackageManager.PERMISSION_GRANTED
                  ) {

                      // permission was granted, yay! Do the
                      // location-related task you need to do.
                      if (ContextCompat.checkSelfPermission(
                              this,
                              Manifest.permission.ACCESS_FINE_LOCATION
                          )
                          == PackageManager.PERMISSION_GRANTED
                      ) {

                          //Request location updates:
                          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
                      }

                  } else {

                      // permission denied, boo! Disable the
                      // functionality that depends on this permission.

                  }
                  return
              }

          }
      }*/
}
