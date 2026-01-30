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
package dk.itu.moapd.androidthreads.ui.samples.coroutines

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dk.itu.moapd.androidthreads.R
import dk.itu.moapd.androidthreads.databinding.FragmentCoroutinesBinding
import dk.itu.moapd.androidthreads.ui.shared.DataViewModel
import dk.itu.moapd.androidthreads.ui.utils.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * A fragment to show the `Coroutines Fragment`.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 */
class CoroutinesFragment : Fragment(R.layout.fragment_coroutines) {
    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = CoroutinesFragment::class.qualifiedName
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentCoroutinesBinding::bind)

    /**
     * Job to track the running coroutine task, allowing proper cancellation and single-instance
     * enforcement.
     */
    private var updateJob: Job? = null

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

        // Set up data binding and lifecycle owner.
        binding.apply {
            dataViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // Define the UI components behavior.
        binding.apply {
            // Reset button.
            resetButton.setOnClickListener {
                viewModel.resetCont()
            }

            // Start/Stop button.
            startButton.setOnClickListener {
                viewModel.status = !viewModel.status
                // Manage the coroutine based on status
                if (viewModel.status) {
                    startUpdateTask()
                } else {
                    stopUpdateTask()
                }
                // Update UI after managing the coroutine
                updateButtons()
            }

            // The initial value of the button status.
            updateButtons()
        }

        // Use repeatOnLifecycle to restore the task after configuration changes.
        // This ensures the coroutine restarts if it was running before the lifecycle stopped.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Only start if status is true and there's no active job
                if (viewModel.status && updateJob?.isActive != true) {
                    startUpdateTask()
                }
            }
        }
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
     * Starts the update task coroutine. Cancels any existing job first
     * to ensure only one instance of the task runs at a time.
     */
    private fun startUpdateTask() {
        // Only start if not already active
        if (updateJob?.isActive == true) return

        // Cancel any completed job before starting a new one
        updateJob?.cancel()
        updateJob =
            viewLifecycleOwner.lifecycleScope.launch {
                updateTask()
                // Clear the job reference when the coroutine completes naturally
                updateJob = null
            }
    }

    /**
     * Stops the update task coroutine if it's running.
     */
    private fun stopUpdateTask() {
        updateJob?.cancel()
        updateJob = null
    }

    /**
     * This method will be executed in an asynchronous Coroutine thread running in the background.
     */
    private suspend fun updateTask() {
        flow {
            while (viewModel.status) {
                emit(Unit)
                delay(50)
                Log.d(TAG, "`CoroutinesTask` cont is ${viewModel.cont.value}.")
            }
        }.collect {
            viewModel.increaseCont()
        }
    }
}
