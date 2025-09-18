@file:OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components.NoteItem
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components.OrderSection
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.plcoding.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotesScreen(
    navController: NavController, viewModel: NotesViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Nova Nota"
                )
            }
        }, scaffoldState = scaffoldState
    ) {
        // Conteúdo separado para permitir preview sem Hilt/NavController
        NotesScreenContent(
            state = state,
            onToggleOrderSection = { viewModel.onEvent(NotesEvent.ToggleOrderSection) },
            onOrderChange = { viewModel.onEvent(NotesEvent.Order(it)) },
            onNoteClick = { note -> navController.navigate(Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}") },
            onDeleteClick = { note ->
                viewModel.onEvent(NotesEvent.DeleteNote(note))
                scope.launch {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Nota deletada",
                        actionLabel = "Desfazer"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotesScreenContent(
    state: NotesState,
    modifier: Modifier = Modifier,
    onToggleOrderSection: () -> Unit = {},
    onOrderChange: (NoteOrder) -> Unit = {},
    onNoteClick: (Note) -> Unit = {},
    onDeleteClick: (Note) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Anotações", style = MaterialTheme.typography.h4)
            IconButton(onClick = { onToggleOrderSection() }) {
                Icon(imageVector = Icons.Default.Sort, contentDescription = "Ordenar")
            }
        }
        AnimatedVisibility(
            visible = state.isOrderSectionVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                noteOrder = state.noteOrder,
                onOrderChange = { onOrderChange(it) },
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.notes) { note ->
                NoteItem(
                    note = note,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { onNoteClick(note) },
                    onDeleteClick = { onDeleteClick(note) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    CleanArchitectureNoteAppTheme {
        val sampleNotes = listOf(
            Note(title = "Compra", content = "Comprar leite e pão", timestamp = 0L, color = AndroidColor.parseColor("#ffab91")),
            Note(title = "Tarefa", content = "Estudar Compose", timestamp = 0L, color = AndroidColor.parseColor("#81DEEA"))
        )
        val state = NotesState(notes = sampleNotes, isOrderSectionVisible = true)
        NotesScreenContent(state = state)
    }
}
