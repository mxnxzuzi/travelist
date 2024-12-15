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

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    fun insertNoteWithValidation(note: Note, tripId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertNoteWithValidation(note, tripId)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getAllNotes(): List<Note> {
        return repository.getAllNotes()
    }
}
