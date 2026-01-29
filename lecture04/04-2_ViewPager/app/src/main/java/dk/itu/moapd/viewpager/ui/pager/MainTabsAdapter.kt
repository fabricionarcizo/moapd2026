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
package dk.itu.moapd.viewpager.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dk.itu.moapd.viewpager.ui.pages.albums.AlbumsFragment
import dk.itu.moapd.viewpager.ui.pages.articles.ArticlesFragment
import dk.itu.moapd.viewpager.ui.pages.contacts.ContactsFragment

/**
 * A class to customize an adapter with a `FragmentStateAdapter` to manage a set of fragments
 * showing in different pages.
 */
class MainTabsAdapter(
    activity: FragmentActivity,
) : FragmentStateAdapter(activity) {
    /**
     * A set of private constants used in this class.
     */
    private companion object {
        private const val PAGE_COUNT = 3
    }

    /**
     * Returns the total number of pages in the `ViewPager`.
     *
     * @return The total number of pages managed by this adapter.
     */
    override fun getItemCount(): Int = PAGE_COUNT

    /**
     * Provide a new Fragment associated with the specified position.
     *
     * The adapter will be responsible for the Fragment lifecycle:
     * * The Fragment will be used to display an item.
     * * The Fragment will be destroyed when it gets too far from the viewport, and its state will
     *      be saved. When the item is close to the viewport again, a new Fragment will be
     *      requested, and a previously saved state will be used to initialize it.
     *
     * @param position The position of the current `Fragment` in the `ViewPager`.
     */
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> ArticlesFragment()
            1 -> ContactsFragment()
            2 -> AlbumsFragment()
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
}
