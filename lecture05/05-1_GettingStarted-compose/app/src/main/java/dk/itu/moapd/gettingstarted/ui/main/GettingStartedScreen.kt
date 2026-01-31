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
package dk.itu.moapd.gettingstarted.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dk.itu.moapd.gettingstarted.R

/**
 * The default name to be used in the greeting message.
 */
private const val DEFAULT_NAME = "World"

/**
 * Composable function for displaying the getting started content.
 *
 * This function creates a user interface (UI) layout using Jetpack Compose. It consists of a column
 * that spans the entire screen and contains a row with a `TextField` and a `Button`. When the
 * button is clicked, the name entered in the `TextField` is displayed in a text component below the
 * row.
 *
 * @see NameInput
 * @see SendButton
 */
@Composable
fun GettingStartedScreen() {
    var name by remember { mutableStateOf("") }
    var greetingTarget by rememberSaveable { mutableStateOf("") }
    var isGreetingVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .clickable { keyboardController?.hide() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            NameInput(
                name = name,
                modifier = Modifier.weight(1f),
                onNameChange = { newName -> name = newName }
            )

            SendButton(
                onClick = {
                    greetingTarget = name.ifEmpty { DEFAULT_NAME }
                    isGreetingVisible = true
                    keyboardController?.hide()
                }
            )
        }

        if (isGreetingVisible) {
            GreetingText(text = greetingTarget)
        }
    }
}

/**
 * Composable function for displaying a text field for entering the user's name.
 *
 * @param name The current value of the user's name.
 * @param modifier Optional modifier for configuring the layout and behavior of the text field.
 * @param onNameChange Callback function invoked when the user's name changes.
 *
 * @see OutlinedTextField
 */
@Composable
private fun NameInput(
    name: String,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = { newName ->
            // Keep it single-line and predictable.
            if (!newName.contains("\n")) onNameChange(newName)
        },
        label = { Text(stringResource(R.string.edit_text_name)) },
        maxLines = 1,
        modifier = modifier.padding(start = 16.dp, top = 48.dp)
    )
}

/**
 * Composable function for displaying a button to send the user's name to the text component.
 *
 * @param onClick Callback function invoked when the button is clicked.
 *
 * @see Button
 */
@Composable
private fun SendButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp)
    ) {
        Text(
            text = stringResource(R.string.button_send),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable function for displaying the user's name in a text component.
 *
 * @param text The user's name to display.
 *
 * @see Text
 */
@Composable
private fun GreetingText(text: String) {
    Text(
        text = stringResource(R.string.text_view_message, text),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
    )
}
