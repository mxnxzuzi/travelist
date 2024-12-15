package ddwu.com.mobile.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ddwu.com.mobile.myapplication.data.model.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM note")
    suspend fun getAllNotes(): List<Note>

    @Transaction
    suspend fun insertNoteWithValidation(note: Note, tripId: Int, tripDao: TripDao) {
        val isTripExists = tripDao.isTripExists(tripId)
        if (isTripExists == 0) {
            throw IllegalArgumentException("해당 tripId가 데이터베이스에 존재하지 않습니다.")
        }
        insertNote(note)
    }
}