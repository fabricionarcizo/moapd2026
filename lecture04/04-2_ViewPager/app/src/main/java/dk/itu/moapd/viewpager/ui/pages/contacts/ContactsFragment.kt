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
package dk.itu.moapd.viewpager.ui.pages.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.javafaker.Faker
import com.google.android.material.color.MaterialColors
import dk.itu.moapd.viewpager.R
import dk.itu.moapd.viewpager.databinding.FragmentContactsBinding
import dk.itu.moapd.viewpager.ui.common.dpToPx
import dk.itu.moapd.viewpager.ui.utils.viewBinding
import java.util.Random

/**
 * A fragment to display a list of contacts with name and phone number. In practice, we are not
 * going to design the UI components in this way. We are going to use ListView, ViewHolder, and
 * RecyclerView to display the lists dynamically.
 */
class ContactsFragment : Fragment(R.layout.fragment_contacts) {
    /**
     * A set of private constants used in this class.
     */
    private companion object {
        const val CONTACT_COUNT = 50
        const val FAKER_SEED = 42L
        const val DIVIDER_HEIGHT_DP = 2
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentContactsBinding::bind)

    /**
     * Faker instance to generate fake data such as names and phone numbers.
     */
    private val faker by lazy {
        Faker(Random(FAKER_SEED))
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

        // IMPORTANT: This is a terrible way to display a list of contacts. DON'T DO THIS! In the
        //            future, we are going to use ListView, ViewHolder, and RecyclerView to display
        //            lists dynamically.
        val inflater = LayoutInflater.from(requireContext())

        repeat(CONTACT_COUNT) {
            binding.linearLayoutContacts.addView(createContactView(inflater))
            binding.linearLayoutContacts.addView(createDividerView())
        }
    }

    /**
     * Creates a view representing a contact item.
     *
     * @param inflater The layout inflater to inflate the view.
     *
     * @return The created contact view.
     */
    private fun createContactView(inflater: LayoutInflater): View {
        val contactView =
            inflater.inflate(
                R.layout.contact_row_item,
                binding.linearLayoutContacts,
                false,
            ) as ViewGroup

        // Get references to the TextViews in the contact item layout.
        val letter = contactView.findViewById<TextView>(R.id.text_view_letter)
        val name = contactView.findViewById<TextView>(R.id.text_view_name)
        val phone = contactView.findViewById<TextView>(R.id.text_view_phone)

        // Generate deterministic fake data (seeded) for reproducible demos.
        val characterName = faker.lordOfTheRings().character()
        letter.text = characterName.firstOrNull()?.uppercase() ?: "?"
        name.text = characterName
        phone.text = faker.phoneNumber().phoneNumber()

        return contactView
    }

    /**
     * Creates a horizontal line view to be used as a divider. The line's height is set to 2dp.
     *
     * @return The created line view.
     */
    private fun createDividerView(): View =
        View(requireContext()).apply {
            layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DIVIDER_HEIGHT_DP.dpToPx(requireContext()),
                )

            // Use a public Material attribute (avoids @SuppressLint("PrivateResource")).
            val color =
                MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOutlineVariant,
                )
            setBackgroundColor(color)
        }
}
