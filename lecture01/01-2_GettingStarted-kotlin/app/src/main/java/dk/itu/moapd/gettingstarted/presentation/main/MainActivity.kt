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
package dk.itu.moapd.gettingstarted.presentation.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.gettingstarted.R

/**
 * An activity class with methods to manage the main activity of Getting Started application.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Companion object to hold constant values.
     */
    companion object {
        /**
         * Default name to use when no name is provided.
         */
        private const val DEFAULT_NAME = "World"
    }

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

        // Set the user interface layout for this Activity.
        setContentView(R.layout.activity_main)

        // Handle window insets to support edge-to-edge content.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Method to be executed when the user presses the `Send` button.
     *
     * @param view An instance that represents the basic building block for user interface
     *             components.
     */
    fun sendMessage(view: View) {
        sendMessage()
        hideKeyboard(view)
    }

    /**
     * Sends a message by updating the TextView with a greeting that includes the name entered
     * in the EditText. If no name is entered, a default name is used.
     */
    private fun sendMessage() {
        val editTextName: EditText = findViewById(R.id.edit_text_name)
        val name = editTextName.text.trim().ifEmpty { DEFAULT_NAME }
        editTextName.clearFocus()

        val textViewMessage: TextView = findViewById(R.id.text_view_message)
        textViewMessage.text = getString(R.string.text_view_message, name)
    }

    /**
     * Hides the soft keyboard.
     *
     * @param anchorView An optional view to anchor the keyboard hiding operation.
     */
    private fun hideKeyboard(anchorView: View? = null) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager ?: return

        val token = (currentFocus?.windowToken ?: anchorView?.windowToken) ?: return
        imm.hideSoftInputFromWindow(token, 0)
    }
}
