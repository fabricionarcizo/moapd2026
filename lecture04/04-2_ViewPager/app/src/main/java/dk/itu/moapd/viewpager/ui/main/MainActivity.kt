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
package dk.itu.moapd.viewpager.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.tabs.TabLayoutMediator
import dk.itu.moapd.viewpager.R
import dk.itu.moapd.viewpager.databinding.ActivityMainBinding
import dk.itu.moapd.viewpager.ui.pager.MainTabsAdapter

/**
 * An activity class with methods to manage the main activity of ViewPager application.
 */
class MainActivity : AppCompatActivity() {
    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is starting. This is where most initialization should go: calling
     * `setContentView(int)` to inflate the activity's UI, using `findViewById()` to
     * programmatically interact with widgets in the UI, calling
     * `managedQuery(android.net.Uri, String[], String, String[], String)` to retrieve cursors for
     * data being displayed, etc.
     *
     * You can call `finish()` from within this function, in which case `onDestroy()` will be
     * immediately called after `onCreate()` without any of the rest of the activity lifecycle
     * (`onStart()`, `onResume()`, onPause()`, etc) executing.
     *
     * <em>Derived classes must call through to the super class's implementation of this method. If
     * they do not, an exception will be thrown.</em>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     * down then this Bundle contains the data it most recently supplied in `onSaveInstanceState()`.
     * <b><i>Note: Otherwise it is null.</i></b>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Migrate from Kotlin synthetics to Jetpack view binding.
        // https://developer.android.com/topic/libraries/view-binding/migration
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar as the app bar for the activity.
        setSupportActionBar(binding.topAppBar)
        setupViewPager()
    }

    /**
     * Sets up the view pager with tabs.
     */
    private fun setupViewPager() {
        val adapter = MainTabsAdapter(this)
        binding.contentMain.viewPager.adapter = adapter

        val tabs =
            listOf(
                TabSpec(R.string.fragment_articles, R.drawable.baseline_article_24),
                TabSpec(R.string.fragment_contacts, R.drawable.baseline_people_24),
                TabSpec(R.string.fragment_albums, R.drawable.baseline_dashboard_24),
            )

        TabLayoutMediator(binding.tabLayout, binding.contentMain.viewPager) { tab, position ->
            val spec = tabs[position]
            tab.text = getString(spec.titleResId)
            tab.icon = AppCompatResources.getDrawable(this, spec.iconResId)
        }.attach()

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentMain.viewPager) { view, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarHeight
            }
            insets
        }
    }

    /**
     * A data class to hold tab specifications.
     *
     * @param titleResId The resource ID for the tab title.
     * @param iconResId The resource ID for the tab icon.
     */
    private data class TabSpec(
        @param:StringRes val titleResId: Int,
        @param:DrawableRes val iconResId: Int,
    )
}
