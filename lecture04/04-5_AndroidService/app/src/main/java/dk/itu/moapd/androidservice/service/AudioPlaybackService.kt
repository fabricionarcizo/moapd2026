/*
 * MIT License
 *
 * Copyright (c) 2026 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.androidservice.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import dk.itu.moapd.androidservice.R
import dk.itu.moapd.androidservice.app.ServiceDemoApplication
import dk.itu.moapd.androidservice.ui.main.MainActivity

/**
 * Foreground service that plays a ringtone in the background.
 * Uses a foreground notification to comply with Android's background execution limits.
 */
class AudioPlaybackService : Service() {
    /**
     * A set of private constants used in this class.
     */
    companion object {
        /**
         * Tag used for logging purposes.
         */
        private val TAG = AudioPlaybackService::class.qualifiedName

        /**
         * Notification ID for the foreground service.
         */
        private const val NOTIFICATION_ID = 1

        /**
         * Indicates whether the service is running.
         */
        var isRunning = false
            private set

        /**
         * Action broadcast when the service starts.
         */
        const val ACTION_SERVICE_STARTED = "dk.itu.moapd.androidservice.SERVICE_STARTED"

        /**
         * Action broadcast when the service stops.
         */
        const val ACTION_SERVICE_STOPPED = "dk.itu.moapd.androidservice.SERVICE_STOPPED"
    }

    /**
     * MediaPlayer instance used to control playback of audio/video files and streams.
     */
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Called by the system when the service is first created. Do not call this method directly.
     */
    override fun onCreate() {
        super.onCreate()
        isRunning = true
        Log.d(TAG, "onCreate()")
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * `startService()`, providing the arguments it supplied and a unique integer token representing
     * the start request. Do not call this method directly.
     *
     * For backwards compatibility, the default implementation calls `onStart()` and returns either
     * `START_STICKY` or `START_STICKY_COMPATIBILITY`.
     *
     * Note that the system calls this on your service's main thread. A service's main thread is the
     * same thread where UI operations take place for Activities running in the same process. You
     * should always avoid stalling the main thread's event loop. When doing long-running
     * operations, network calls, or heavy disk I/O, you should kick off a new thread, or use
     * Kotlin Coroutines.
     *
     * @param intent The Intent supplied to `startService()`, as given. This may be `null` if the
     *      service is being restarted after its process has gone away, and it had previously
     *      returned anything except `START_STICKY_COMPATIBILITY`.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start. Use with
     *      `stopSelfResult(int)`.
     *
     * @return The return value indicates what semantics the system should use for the service's
     *      current started state. It may be one of the constants associated with the
     *      `START_CONTINUATION_MASK` bits.
     */
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        // Start the service as a foreground service with a notification
        try {
            startForeground(NOTIFICATION_ID, createNotification())
        } catch (e: SecurityException) {
            // SecurityException can be thrown if FOREGROUND_SERVICE_MEDIA_PLAYBACK permission
            // is not granted or service restrictions apply (Android 12+ / API 31+)
            Log.e(TAG, "Failed to start foreground service: SecurityException", e)
            stopSelf()
            return START_NOT_STICKY
        } catch (e: IllegalStateException) {
            // IllegalStateException thrown when startForeground() is called after onStartCommand()
            // returns or when app is in background on Android 12+ without proper permissions
            Log.e(TAG, "Failed to start foreground service: IllegalStateException", e)
            stopSelf()
            return START_NOT_STICKY
        }

        // Start playing the default ringtone audio.
        if (mediaPlayer == null) {
            try {
                mediaPlayer =
                    MediaPlayer().apply {
                        setDataSource(applicationContext, Settings.System.DEFAULT_RINGTONE_URI)
                        isLooping = true
                        prepare()
                        start()
                    }
                Log.d(TAG, "onStartCommand()")

                // Send broadcast that service has started
                sendBroadcast(
                    Intent(ACTION_SERVICE_STARTED).apply {
                        setPackage(packageName)
                    },
                )
            } catch (e: java.io.IOException) {
                Log.e(TAG, "Failed to access the ringtone file", e)
                cleanupMediaPlayer()
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid data source URI", e)
                cleanupMediaPlayer()
            } catch (e: IllegalStateException) {
                Log.e(TAG, "MediaPlayer was in an incorrect state", e)
                cleanupMediaPlayer()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Helper method to reset the media player state in case of initialization failure.
     */
    private fun cleanupMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * Return the communication channel to the service. May return `null` if clients can not bind to
     * the service. The returned `IBinder` is usually for a complex interface that has been
     * described using aidl.
     *
     * Note that unlike other application components, calls on to the `IBinder` interface returned
     * here may not happen on the main thread of the process. More information about the main thread
     * can be found in the official Android documentation (`Processes and Threads`).
     *
     * @param intent The `Intent` that was used to bind to this service, as given to
     *      `bindService()`. Note that any extras that were included with the `Intent` at that point
     *      will not be seen here.
     *
     * @return Return an `IBinder` through which clients can call on to the service.
     */
    override fun onBind(intent: Intent): IBinder? = null

    /**
     * Called by the system to notify a `Service` that it is no longer used and is being removed.
     * The service should clean up any resources it holds (threads, registered receivers, etc) at
     * this point. Upon return, there will be no more calls in to this `Service` object and it is
     * effectively dead. Do not call this method directly.
     */
    override fun onDestroy() {
        mediaPlayer?.run {
            stop()
            release()
        }
        mediaPlayer = null

        // Send broadcast that service has stopped
        sendBroadcast(
            Intent(ACTION_SERVICE_STOPPED).apply {
                setPackage(packageName)
            },
        )

        isRunning = false
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    /**
     * Creates a notification for the foreground service.
     *
     * @return The notification to be displayed while the service is running.
     */
    private fun createNotification() =
        NotificationCompat.Builder(this, ServiceDemoApplication.AUDIO_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE,
                ),
            )
            .build()
}
