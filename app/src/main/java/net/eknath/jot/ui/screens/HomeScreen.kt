package net.eknath.jot.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.eknath.jot.R
import net.eknath.jot.ui.componenets.NoteDisplayCard
import net.eknath.jot.ui.screens.states.EditorState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(editorState: EditorState) {
    val showCreation = remember { mutableStateOf(false) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(modifier = Modifier.shadow(elevation = 10.dp), title = {
            Text(text = "JOT-Notes and more", fontWeight = FontWeight.Medium)
        }, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                editorState.resetTextFieldAndSelection()
                showCreation.value = true
            }, shape = RoundedCornerShape(10.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }, floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(5.dp),
            content = {
                items(editorState.jotterList.value) {
                    NoteDisplayCard(title = it.title, description = it.note, onClick = {
                        editorState.getJot(id = it.id,
                            onSuccess = { showCreation.value = true },
                            onFailure = {

                            })
                    })
                }
            })
    }

    //CreationComponent
    AnimatedVisibility(
        visible = showCreation.value,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        CreationComponent(
            visibility = showCreation,
            editorState = editorState,
            onBackPressed = {
                showCreation.value = false
            }
        )
    }
}