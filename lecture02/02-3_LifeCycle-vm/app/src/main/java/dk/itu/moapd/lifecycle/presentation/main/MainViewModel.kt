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
package dk.itu.moapd.lifecycle.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A view model sensitive to changes in the `MainActivity` UI components.
 */
class MainViewModel : ViewModel() {
    /**
     * The current text showing in the main activity.
     */
    private val _message = MutableLiveData<String>()

    /**
     * A `LiveData` which publicly exposes any update in the UI TextView.
     */
    val message: LiveData<String>
        get() = _message

    /**
     * The current status of the UI checkbox.
     */
    private val _isChecked = MutableLiveData(false)

    /**
     * A `LiveData` which publicly exposes any update in the UI checkbox.
     */
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    /**
     * This method will be executed when the user interacts with any UI component and it is
     * necessary to update the text in the UI TextView. It sets the text into the LiveData instance.
     *
     * @param text A `String` to show in the UI TextView.
     */
    fun setMessage(text: String) {
        _message.value = text
    }

    /**
     * This method will be executed when the user interacts with the UI checkbox and it is necessary
     * to update its status and the text in the UI TextView. It sets the checkbox status and the
     * text into their respective LiveData instances.
     *
     * @param checked A `Boolean` representing the current status of the checkbox.
     * @param selectedText A lambda function that takes a `String` and returns a `String` to be
     *      shown in the UI TextView based on the checkbox status.
     */
    fun onCheckedChanged(
        checked: Boolean,
        selectedText: (String) -> String,
    ) {
        _isChecked.value = checked
        val status = if (checked) "checked" else "unchecked"
        _message.value = selectedText(status)
    }
}
