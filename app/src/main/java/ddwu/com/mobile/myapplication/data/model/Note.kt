package ddwu.com.mobile.myapplication.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val markerId: Int,
    val content: String,
    val date: String?
)
