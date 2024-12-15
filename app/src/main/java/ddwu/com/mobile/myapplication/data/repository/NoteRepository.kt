package ddwu.com.mobile.myapplication.data.repository


import ddwu.com.mobile.myapplication.data.dao.NoteDao
import ddwu.com.mobile.myapplication.data.dao.TripDao
import ddwu.com.mobile.myapplication.data.model.Note

class NoteRepository(
    private val noteDao: NoteDao,
    private val tripDao: TripDao
) {
    suspend fun insertNoteWithValidation(note: Note, tripId: Int) {
        val isTripExists = tripDao.isTripExists(tripId)
        if (isTripExists == 0) {
            throw IllegalArgumentException("해당 tripId가 데이터베이스에 존재하지 않습니다.")
        }
        noteDao.insertNote(note)
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }
}
