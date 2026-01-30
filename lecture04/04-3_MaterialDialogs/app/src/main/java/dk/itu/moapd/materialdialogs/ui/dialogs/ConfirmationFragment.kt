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
package dk.itu.moapd.materialdialogs.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dk.itu.moapd.materialdialogs.R
import dk.itu.moapd.materialdialogs.ui.common.showSnackBar

/**
 * A fragment to show the `Confirmation Fragment`.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 */
class ConfirmationFragment : Fragment(R.layout.fragment_confirmation) {
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
    @Suppress("TODO")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Define lambda function for showing the main fragment.
        val showMainFragment = {
            findNavController().navigate(R.id.show_fragment_main)
        }

        val singleItems = resources.getStringArray(R.array.simple_items)
        val checkedItem = 1

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirmation_title))
            .setCancelable(false)
            .setNeutralButton(getString(R.string.cancel)) { _, _ ->
                // TODO: Respond to neutral button press.
                view.showSnackBar(getString(R.string.snackbar_cancelled))
                showMainFragment()
            }.setPositiveButton(getString(R.string.ok)) { _, _ ->
                // TODO: Respond to positive button press.
                view.showSnackBar(getString(R.string.snackbar_confirmed))
                showMainFragment()
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                // TODO: Respond to item chosen.
                view.showSnackBar(singleItems[which])
            }.show()
    }
}
