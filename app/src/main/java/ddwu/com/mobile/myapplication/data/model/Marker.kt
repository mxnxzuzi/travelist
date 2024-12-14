package ddwu.com.mobile.myapplication.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Marker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val location: String,
    val visitCount: Int = 1,
    val specialColor: Boolean = false
)
