package ddwu.com.mobile.myapplication.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ddwu.com.mobile.myapplication.data.dao.NoteDao
import ddwu.com.mobile.myapplication.data.dao.TripDao
import ddwu.com.mobile.myapplication.data.model.Note
import ddwu.com.mobile.myapplication.data.model.Trip

@Database(entities = [Trip::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trip_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
