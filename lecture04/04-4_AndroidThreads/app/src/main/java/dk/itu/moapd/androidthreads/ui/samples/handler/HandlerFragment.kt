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
package dk.itu.moapd.androidthreads.ui.samples.handler

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dk.itu.moapd.androidthreads.R
import dk.itu.moapd.androidthreads.databinding.FragmentHandlerBinding
import dk.itu.moapd.androidthreads.ui.shared.DataViewModel
import dk.itu.moapd.androidthreads.ui.utils.viewBinding
import kotlin.getValue

/**
 * A fragment to show the `Handler Fragment`.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 */
class HandlerFragment : Fragment(R.layout.fragment_handler) {
    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = HandlerFragment::class.qualifiedName
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentHandlerBinding::bind)

    /**
     * The `DataViewModel` instance is created using the `by viewModels()` Kotlin property delegate,
     * which is part of the AndroidX Activity KTX library. This delegate simplifies the process of
     * obtaining a ViewModel instance that is scoped to the Activity's lifecycle. It ensures that
     * the ViewModel is created only once and is retained across configuration changes, such as
     * screen rotations. This approach promotes a clean separation of concerns, allowing the
     * Activity to focus on UI-related tasks while the ViewModel handles data and business logic.
     */
    private val viewModel: DataViewModel by viewModels()

    /**
     * Reference to the currently running thread to enable proper cleanup.
     */
    private var handlerThread: Thread? = null

    /**
     * Handler for posting updates to the main thread. Created once to avoid memory pressure.
     */
    private val handler = Handler(Looper.getMainLooper())

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view.  This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created.  The
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

        // Define the UI components behavior.
        binding.apply {
            // Reset button.
            resetButton.setOnClickListener {
                viewModel.resetCont()
            }

            // Start/Stop button.
            startButton.setOnClickListener {
                if (viewModel.status) {
                    // Stop: interrupt thread first, then update status
                    handlerThread?.interrupt()
                    handlerThread = null
                    viewModel.status = false
                } else {
                    // Start: create and start new thread
                    viewModel.status = true
                    handlerThread = Thread(HandlerTask())
                    handlerThread?.start()
                }
                updateButtons()
            }

            // The initial value of the button status.
            updateButtons()

            // Set an observer to check when the `cont` variable in updated in the `ViewModel`.
            viewModel.cont.observe(viewLifecycleOwner) { value ->
                progressBar.progress = value
            }
        }

        // In the case of changing the device orientation.
        if (viewModel.status) {
            // Clean up any existing thread before creating a new one
            handlerThread?.interrupt()
            handlerThread = Thread(HandlerTask())
            handlerThread?.start()
        }
    }

    /**
     * Called when the view previously created by `onCreateView()` has been detached from the
     * fragment. The next time the fragment needs to be displayed, a new view will be created.
     * This is called after `onStop()` and before `onDestroy()`. It is called regardless of
     * whether `onCreateView()` returned a non-null view. Internally it is called after the view's
     * state has been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Interrupt and clean up the running thread to prevent thread leaks
        handlerThread?.interrupt()
        handlerThread = null
        // Remove all pending messages and callbacks to prevent memory leaks and crashes
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * A simple method to update the text/status of the UI buttons.
     */
    private fun updateButtons() {
        // Update the start button text using a lambda expression.
        binding.startButton.text =
            getString(
                if (viewModel.status) R.string.stop_button else R.string.start_button,
            )

        // Update the reset button enabled state using a higher-order function.
        binding.resetButton.isEnabled = viewModel.status
    }

    /**
     * A internal class to manager a worker thread to execute an Android task in background.
     */
    private inner class HandlerTask : Runnable {
        /**
         * When an object implementing interface `Runnable` is used to create a thread, starting the
         * thread causes the object's `run()` method to be called in that separately executing
         * thread.
         *
         * The general contract of the method `run()` is that it may take any action whatsoever.
         */
        override fun run() {
            // Run this block until the user presses the stop button or the thread is interrupted.
            while (viewModel.status && !Thread.currentThread().isInterrupted) {
                // Stops the worker thread for 250 milliseconds.
                try {
                    Thread.sleep(250)
                    Log.d(TAG, "`HandlerTask` cont is ${viewModel.cont.value}.")
                } catch (e: InterruptedException) {
                    // Thread was interrupted, exit gracefully
                    Log.d(TAG, "`HandlerTask` interrupted, exiting gracefully.")
                    return
                }

                // Send a post to update the progress bar in the UI thread.
                handler.post(viewModel::increaseCont)
            }
        }
    }
}
