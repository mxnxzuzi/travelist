package ddwu.com.mobile.myapplication.data.repository


import androidx.lifecycle.LiveData
import ddwu.com.mobile.myapplication.data.dao.NoteDao
import ddwu.com.mobile.myapplication.data.dao.TripDao
import ddwu.com.mobile.myapplication.data.model.Note

class NoteRepository(
    private val noteDao: NoteDao,
    private val tripDao: TripDao
) {
    suspend fun insertNoteAndGetId(note: Note, tripId: Int): Long {
        val isTripExists = tripDao.isTripExists(tripId)
        if (isTripExists == 0) {
            throw IllegalArgumentException("해당 tripId가 데이터베이스에 존재하지 않습니다.")
        }
        return noteDao.insertNote(note)
    }
    fun getAllNotesLiveData(): LiveData<List<Note>> {
        return noteDao.getAllNotesLiveData()
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
    }

}
