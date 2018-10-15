package com.techgimmicks.mydriver

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager



class ShakeEventListener : SensorEventListener {

    /** Minimum movement force to consider.  */
    private val MIN_FORCE = 10

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private val MIN_DIRECTION_CHANGE = 3

    /** Maximum pause between movements.  */
    private val MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200

    /** Maximum allowed time for shake gesture.  */
    private val MAX_TOTAL_DURATION_OF_SHAKE = 400

    /** Time when the gesture started.  */
    private var mFirstDirectionChangeTime: Long = 0

    /** Time when the last movement started.  */
    private var mLastDirectionChangeTime: Long = 0

    /** How many movements are considered so far.  */
    private var mDirectionChangeCount = 0

    /** The last x position.  */
    private var lastX = 0f

    /** The last y position.  */
    private var lastY = 0f

    /** The last z position.  */
    private var lastZ = 0f

    /** OnShakeListener that is called when shake is detected.  */
    private var mShakeListener: OnShakeListener? = null

    /**
     * Interface for shake gesture.
     */
    interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        fun onShake()
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        mShakeListener = listener
    }

    override fun onSensorChanged(se: SensorEvent) {
        // get sensor data
        val x = se.values[0]
        val y = se.values[1]
        val z = se.values[2]

        // calculate movement
        val totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ)

        if (totalMovement > MIN_FORCE) {

            // get time
            val now = System.currentTimeMillis()

            // store first movement time
            if (mFirstDirectionChangeTime == 0L) {
                mFirstDirectionChangeTime = now
                mLastDirectionChangeTime = now
            }

            // check if the last movement was not long ago
            val lastChangeWasAgo = now - mLastDirectionChangeTime
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now
                mDirectionChangeCount++

                // store last sensor data
                lastX = x
                lastY = y
                lastZ = z

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    val totalDuration = now - mFirstDirectionChangeTime
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        mShakeListener!!.onShake()
                        resetShakeParameters()
                    }
                }

            } else {
                resetShakeParameters()
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private fun resetShakeParameters() {
        mFirstDirectionChangeTime = 0
        mDirectionChangeCount = 0
        mLastDirectionChangeTime = 0
        lastX = 0f
        lastY = 0f
        lastZ = 0f
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}




}