package ddwu.com.mobile.myapplication.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ddwu.com.mobile.myapplication.data.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note")
    fun getAllNotesLiveData(): LiveData<List<Note>>

    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Insert
    suspend fun insertNote(note: Note): Long

    @Query("DELETE FROM note WHERE tripId = :tripId")
    suspend fun deleteNotesByTripId(tripId: Int)

}