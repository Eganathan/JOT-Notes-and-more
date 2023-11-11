package net.eknath.jot.ui.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository
import net.eknath.jot.domain.usecase.AddNoteUseCase
import net.eknath.jot.domain.usecase.DeleteNoteUseCase
import net.eknath.jot.domain.usecase.BulkDeleteUseCase
import net.eknath.jot.domain.usecase.GetAllNotesUseCase
import net.eknath.jot.domain.usecase.GetNoteUseCase
import net.eknath.jot.domain.usecase.SearchNotesUseCase
import net.eknath.jot.domain.usecase.UpdateNoteUseCase
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val getAllNotesUseCase = GetAllNotesUseCase(noteRepository)
    private val getNoteUseCase = GetNoteUseCase(noteRepository)
    private val addNoteUseCase = AddNoteUseCase(noteRepository)
    private val updateNoteUseCase = UpdateNoteUseCase(noteRepository)
    private val deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    private val bulkDeleteUseCase = BulkDeleteUseCase(noteRepository)
    private val searchUseCase = SearchNotesUseCase(noteRepository)

    val selectedNoteId: MutableState<Long?> = mutableStateOf(null)
    val selectedNote: MutableState<Note?> = mutableStateOf(null)

    val notes = getAllNotesUseCase().asLiveData()
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = searchQuery
        .debounce(300)  // Wait for 300ms of no input before processing the query
        .distinctUntilChanged()  // Only emit if the current query is different from the last
        .flatMapLatest { query ->
            searchUseCase(query)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )


    fun addNote(note: Note) = viewModelScope.launch {
        selectedNoteId.value = addNoteUseCase(note)
    }

    fun getNoteById(id: Long) = viewModelScope.launch {
        val note = getNoteUseCase(id)
        if (note != null) {
            selectedNoteId.value = note.id
            selectedNote.value = note
        }
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        updateNoteUseCase(note)
    }

    fun deleteNoteById(id: Long) = viewModelScope.launch {
        deleteNoteUseCase(id)
        selectedNoteId.value = null
    }

    fun deleteNotes(notes: List<Long>) = viewModelScope.launch {
        bulkDeleteUseCase(notes)
    }

    fun resetSelection() {
        selectedNoteId.value = null
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
