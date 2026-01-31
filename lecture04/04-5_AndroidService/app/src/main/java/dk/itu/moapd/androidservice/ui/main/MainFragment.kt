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
package dk.itu.moapd.androidservice.ui.main

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.androidservice.R
import dk.itu.moapd.androidservice.databinding.FragmentMainBinding
import dk.itu.moapd.androidservice.service.AudioPlaybackService
import dk.itu.moapd.androidservice.ui.utils.viewBinding

/**
 * Main fragment with controls to start/stop a background service.
 */
class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        /**
         * Permission required for posting notifications on Android 13+ (API 33).
         */
        private const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentMainBinding::bind)

    /**
     * ViewModel for managing service state that survives configuration changes.
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * ActivityResultLauncher for requesting POST_NOTIFICATIONS permission on Android 13+.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, start the service
                startAudioServiceInternal()
            } else {
                // Permission denied, show a message
                view?.let {
                    Snackbar
                        .make(
                            it,
                            "Notification permission is required to run the service",
                            Snackbar.LENGTH_LONG,
                        ).show()
                }
            }
        }

    /**
     * BroadcastReceiver to listen for service state changes.
     */
    private val serviceStateReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                when (intent?.action) {
                    AudioPlaybackService.ACTION_SERVICE_STARTED -> {
                        viewModel.setServiceRunning(true)
                    }
                    AudioPlaybackService.ACTION_SERVICE_STOPPED -> {
                        viewModel.setServiceRunning(false)
                    }
                }
            }
        }

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view. This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created. The
     * fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Check if service is actually running and sync ViewModel state
        viewModel.setServiceRunning(isServiceRunning())

        // Observe service state changes and update button states accordingly
        viewModel.isServiceRunning.observe(viewLifecycleOwner) { isRunning ->
            updateButtonStates(isRunning)
        }

        setupClickListeners()
    }

    /**
     * Called when the Fragment is visible to the user. This is generally tied to Activity.onStart
     * of the containing Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()

        // Register the broadcast receiver to listen for service state changes
        val filter =
            IntentFilter().apply {
                addAction(AudioPlaybackService.ACTION_SERVICE_STARTED)
                addAction(AudioPlaybackService.ACTION_SERVICE_STOPPED)
            }

        ContextCompat.registerReceiver(
            requireContext(),
            serviceStateReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    /**
     * Called when the Fragment is no longer started. This is generally tied to Activity.onStop of
     * the containing Activity's lifecycle.
     */
    override fun onStop() {
        super.onStop()

        // Unregister the broadcast receiver
        requireContext().unregisterReceiver(serviceStateReceiver)
    }

    /**
     * Sets up the click listeners for the buttons in the fragment.
     */
    private fun setupClickListeners() {
        binding.startButton.setOnClickListener { startAudioService() }
        binding.stopButton.setOnClickListener { stopAudioService() }
    }

    /**
     * Starts the audio playback service.
     */
    private fun startAudioService() {
        // Check for notification permission on Android 13+ (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    NOTIFICATION_PERMISSION,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted
                    startAudioServiceInternal()
                }
                shouldShowRequestPermissionRationale(NOTIFICATION_PERMISSION) -> {
                    // Show rationale and request permission
                    view?.let {
                        Snackbar
                            .make(
                                it,
                                "Notification permission is needed to show service status",
                                Snackbar.LENGTH_LONG,
                            ).setAction("Grant") {
                                requestPermissionLauncher.launch(NOTIFICATION_PERMISSION)
                            }.show()
                    }
                }
                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(NOTIFICATION_PERMISSION)
                }
            }
        } else {
            // No permission needed for Android 12 and below (API 32 and below)
            startAudioServiceInternal()
        }
    }

    /**
     * Internal method to start the audio service after permission checks.
     */
    private fun startAudioServiceInternal() {
        val serviceIntent = createAudioServiceIntent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(serviceIntent)
        } else {
            requireContext().startService(serviceIntent)
        }
        viewModel.setServiceRunning(true)
    }

    /**
     * Stops the audio playback service.
     */
    private fun stopAudioService() {
        requireContext().stopService(createAudioServiceIntent())
        viewModel.setServiceRunning(false)
    }

    /**
     * Creates an intent for the audio playback service.
     *
     * @return An intent for the audio playback service.
     */
    private fun createAudioServiceIntent(): Intent =
        Intent(
            requireContext(),
            AudioPlaybackService::class.java,
        )

    /**
     * Updates the enabled state of the start and stop buttons based on service state.
     *
     * @param isRunning True if the service is running, false otherwise.
     */
    private fun updateButtonStates(isRunning: Boolean) {
        binding.startButton.isEnabled = !isRunning
        binding.stopButton.isEnabled = isRunning
    }

    /**
     * Checks if the AudioPlaybackService is currently running.
     *
     * @return True if the service is running, false otherwise.
     */
    private fun isServiceRunning(): Boolean = AudioPlaybackService.isRunning
}
