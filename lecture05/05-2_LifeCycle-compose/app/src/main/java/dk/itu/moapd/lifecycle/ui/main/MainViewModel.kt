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
package dk.itu.moapd.lifecycle.ui.main

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import dk.itu.moapd.lifecycle.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A view model sensitive to changes in the `MainActivity` UI components.
 */
class MainViewModel : ViewModel() {

    /**
     * The current state of all UI components shown in the main activity.
     */
    private val _uiState = MutableStateFlow(MainUiState())

    /**
     * A `StateFlow` which publicly exposes any update in the UI components.
     */
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Updates the text to be displayed based on the selected text resource ID.
     *
     * @param textId The resource ID of the text to be displayed.
     */
    fun onTextSelected(@StringRes textId: Int) {
        _uiState.update { it.copy(textId = textId) }
    }

    /**
     * Updates the checked state of the checkbox and the corresponding text to be displayed.
     *
     * @param isChecked A boolean indicating whether the checkbox is checked or not.
     */
    fun onCheckedChanged(isChecked: Boolean) {
        val textId = if (isChecked) R.string.checked_text else R.string.unchecked_text
        _uiState.update { it.copy(textId = textId, checked = isChecked) }
    }
}
