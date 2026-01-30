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
                updateButtons()
            }

            // The initial value of the button status.
            updateButtons()
        }

        // Use repeatOnLifecycle as the single source of truth for coroutine management.
        // This continuously monitors the status and runs updateTask only when status is true,
        // ensuring proper lifecycle handling and preventing duplicate coroutines during
        // configuration changes like device rotation.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateTask()
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
     * This method will be executed in an asynchronous Coroutine thread running in the background.
     */
    private suspend fun updateTask() {
        // Keep collecting while the lifecycle is in STARTED state.
        // The flow only emits when status is true.
        flow {
            while (true) {
                if (viewModel.status) {
                    emit(Unit)
                    delay(50)
                    Log.d(TAG, "`CoroutinesTask` cont is ${viewModel.cont.value}.")
                } else {
                    // Small delay when not active to avoid busy waiting
                    delay(50)
                }
            }
        }.collect {
            viewModel.increaseCont()
        }
    }
}
