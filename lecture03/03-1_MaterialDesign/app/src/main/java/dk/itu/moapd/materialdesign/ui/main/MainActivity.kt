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
package dk.itu.moapd.materialdesign.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dk.itu.moapd.materialdesign.R
import dk.itu.moapd.materialdesign.databinding.ActivityMainBinding
import dk.itu.moapd.materialdesign.ui.common.showSnackBar

/**
 * Main screen demonstrating Material Design components (TopAppBar + BottomAppBar).
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
     * (`onStart()`, `onResume()`, `onPause()`, etc) executing.
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

        // Setup UI components.
        setupTopAppBar()
        setupBottomAppBar()
        setupClickListeners()
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You should place your menu
     * items in to menu.  This is only called once, the first time the options menu is displayed.
     * To update the menu every time it is displayed, see `onPrepareOptionsMenu(Menu)`.  The default
     * implementation populates the menu with standard system menu items.  These are placed in the
     * `Menu#CATEGORY_SYSTEM` group so that they will be correctly ordered with application-defined
     * menu items.  Deriving classes should always call through to the base implementation.  You can
     * safely hold on to menu (and any items created from it), making modifications to it as
     * desired, until the next time `onCreateOptionsMenu()` is called.  When you add items to the
     * menu, you can implement the Activity's `onOptionsItemSelected(MenuItem)` method to handle
     * them there.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return `true` for the menu to be displayed; if you return `false` it will
     *      not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    /**
     * This hook is called whenever an item in your options menu is selected.  The default
     * implementation simply returns `false` to have the normal processing happen (calling the
     * item's `Runnable` or sending a message to its `Handler` as appropriate).  You can use this
     * method for any items for which you would like to do processing without those other
     * facilities.  Derived classes should call through to the base class for it to perform the
     * default menu handling.
     *
     * @param item The menu item that was selected.  This value cannot be `null`.
     *
     * @return Return `false` to allow normal menu processing to proceed, `true` to consume it here.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val message =
            when (item.itemId) {
                // Top app bar.
                R.id.attach_file -> "Attach file icon pressed"
                R.id.calendar_today -> "Calendar today icon pressed"
                R.id.more -> "More item (inside overflow menu) pressed"

                // Bottom app bar.
                R.id.search -> "Search icon pressed"
                R.id.delete -> "Delete icon pressed"
                R.id.shortcut -> "Shortcut icon pressed"
                R.id.system_update -> "System update icon pressed"

                else -> return super.onOptionsItemSelected(item)
            }

        binding.root.showSnackBar(message)
        return true
    }

    /**
     * Sets up the top app bar by configuring it as the support action bar and adding a navigation
     * click listener.
     *
     * Note: The navigation click listener currently shows a snackbar message as a placeholder
     * action. This should be replaced with the actual navigation behavior as needed.
     */
    @Suppress("ForbiddenComment")
    private fun setupTopAppBar() =
        with(binding) {
            setSupportActionBar(topAppBar)

            topAppBar.setNavigationOnClickListener {
                // TODO: replace with real navigation behavior
                root.showSnackBar("Navigation icon pressed")
            }
        }

    /**
     * Sets up the bottom app bar by replacing its menu and setting a menu item click listener.
     *
     * Note: The menu item click listener currently delegates to the activity's
     * `onOptionsItemSelected` method to handle menu item selections.
     */
    private fun setupBottomAppBar() =
        with(binding) {
            bottomAppBar.replaceMenu(R.menu.bottom_app_bar)
            bottomAppBar.setOnMenuItemClickListener(this@MainActivity::onOptionsItemSelected)
        }

    /**
     * Sets up click listeners for various UI components.
     *
     * Note: The click listener for the bottom add button currently shows a snackbar message as a
     * placeholder action. This should be replaced with the actual action as needed.
     */
    @Suppress("ForbiddenComment")
    private fun setupClickListeners() =
        with(binding) {
            bottomAdd.setOnClickListener {
                // TODO: replace with real action (e.g., create new item)
                root.showSnackBar("Bottom navigation icon pressed")
            }
        }
}
