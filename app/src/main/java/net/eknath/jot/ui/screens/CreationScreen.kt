@file:OptIn(ExperimentalMaterial3Api::class)

package net.eknath.jot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import net.eknath.jot.ui.screens.states.EditorState

@Composable
fun CreationComponent(
    visibility: State<Boolean>,
    editorState: EditorState,
    onBackPressed: () -> Unit, background:
    Color = MaterialTheme.colorScheme.background,
    focusRequester: FocusRequester = FocusRequester()
) {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }, title = {}, actions = {})
        }) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .imePadding()
        ) {
            val textFieldColor = TextFieldDefaults.textFieldColors(
                containerColor = background,

                disabledIndicatorColor = background,
                errorIndicatorColor = background,
                focusedIndicatorColor = background,
                unfocusedIndicatorColor = background
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                value = editorState.titleTextFieldState.value,
                onValueChange = { editorState.titleTextFieldState.value = it },
                placeholder = { Text("Title") },
                colors = textFieldColor
            )

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = editorState.entryTextFieldState.value,
                onValueChange = {
                    editorState.entryTextFieldState.value = it
                },
                placeholder = { Text(text = "note") },
                colors = textFieldColor
            )
        }
    }

    LaunchedEffect(key1 = editorState.entryTextFieldState, block = {
        focusRequester.requestFocus()
    })
}