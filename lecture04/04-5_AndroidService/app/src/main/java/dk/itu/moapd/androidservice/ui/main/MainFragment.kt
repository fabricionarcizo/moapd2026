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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dk.itu.moapd.androidservice.R
import dk.itu.moapd.androidservice.databinding.FragmentMainBinding
import dk.itu.moapd.androidservice.service.AudioPlaybackService
import dk.itu.moapd.androidservice.ui.utils.viewBinding

/**
 * Main fragment with controls to start/stop a background service.
 */
class MainFragment : Fragment(R.layout.fragment_main) {
    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentMainBinding::bind)

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
        setupClickListeners()
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
        requireContext().startService(createAudioServiceIntent())
    }

    /**
     * Stops the audio playback service.
     */
    private fun stopAudioService() {
        requireContext().stopService(createAudioServiceIntent())
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
}
