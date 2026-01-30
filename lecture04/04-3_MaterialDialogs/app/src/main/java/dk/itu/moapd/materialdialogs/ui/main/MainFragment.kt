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
package dk.itu.moapd.materialdialogs.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.materialdialogs.R
import dk.itu.moapd.materialdialogs.databinding.FragmentMainBinding
import dk.itu.moapd.materialdialogs.domain.model.DialogOption
import dk.itu.moapd.materialdialogs.ui.list.DialogOptionsAdapter
import dk.itu.moapd.materialdialogs.ui.list.OnItemClickListener
import dk.itu.moapd.materialdialogs.ui.utils.viewBinding

/**
 * A fragment to show the `Main Fragment`.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 */
class MainFragment :
    Fragment(R.layout.fragment_main),
    OnItemClickListener {
    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentMainBinding::bind)

    /**
     * A list of dialog options to be displayed in the RecyclerView.
     */
    private val dialogOptions =
        listOf(
            DialogOption("Alert Dialog", R.id.show_fragment_alert),
            DialogOption("Simple Dialog", R.id.show_fragment_simple),
            DialogOption("Confirmation Dialog", R.id.show_fragment_confirmation),
        )

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

        // Create the `String` and `Fragments` lists.
        val adapter = DialogOptionsAdapter(this, dialogOptions)

        // Define the recycler view layout manager and adapter.
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL),
            )
            this.adapter = adapter
        }
    }

    /**
     * Called when an item in the `RecyclerView` is clicked.
     *
     * @param position The position of the clicked item in the RecyclerView.
     */
    override fun onItemClick(position: Int) {
        findNavController().navigate(dialogOptions[position].destinationId)
    }
}
