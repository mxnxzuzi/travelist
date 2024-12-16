package ddwu.com.mobile.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ddwu.com.mobile.myapplication.data.model.Note
import ddwu.com.mobile.myapplication.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    val new_notes: LiveData<List<Note>> = repository.getAllNotesLiveData()

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun insertNoteAndGetId(note: Note, tripId: Int, onResult: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val noteId = repository.insertNoteAndGetId(note, tripId)
                viewModelScope.launch(Dispatchers.Main) {
                    onResult(noteId)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
        }
    }

    fun deleteNotesByTripId(tripId: Int) {
        viewModelScope.launch {
            repository.deleteNotesByTripId(tripId)
        }
    }


    suspend fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }
}
