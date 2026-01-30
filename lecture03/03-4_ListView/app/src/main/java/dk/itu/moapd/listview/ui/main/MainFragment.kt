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
 * NONINFRINGEMENT.  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.listview.ui.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.github.javafaker.Faker
import dk.itu.moapd.listview.R
import dk.itu.moapd.listview.databinding.FragmentMainBinding
import dk.itu.moapd.listview.domain.model.DummyModel
import dk.itu.moapd.listview.ui.list.CustomAdapter
import dk.itu.moapd.listview.utils.viewBinding
import java.util.Random

/**
 * A fragment to display the main screen of the app.
 */
class MainFragment : Fragment(R.layout.fragment_main) {
    /**
     * A set of private constants used in this class.
     */
    companion object {
        private const val FAKER_SEED = 42L
        private const val DUMMY_ITEM_COUNT = 50
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentMainBinding::bind)

    /**
     * Faker instance to generate fake data such as names and phone numbers.
     */
    private val faker: Faker by lazy { Faker(Random(FAKER_SEED)) }

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
        setupListView()
    }

    /**
     * Set up the list view with a custom adapter and dummy data.
     */
    private fun setupListView() {
        val data = createDummyData()
        val adapter =
            CustomAdapter(
                context = requireContext(),
                itemLayoutResId = R.layout.row_item,
                data = data,
            )
        binding.listView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.listView) { view, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarHeight
            }
            insets
        }
    }

    /**
     * Create a list of dummy data using the Faker library.
     *
     * @return A list of dummy model objects.
     */
    private fun createDummyData(): List<DummyModel> =
        (1..DUMMY_ITEM_COUNT).map { index ->
            val address = faker.address()
            DummyModel(
                cityName = address.cityName(),
                zipCode = address.zipCode(),
                country = address.country(),
                description = faker.lorem().paragraph(),
                url = "https://picsum.photos/seed/$index/400/194",
            )
        }
}
