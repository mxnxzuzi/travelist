package ddwu.com.mobile.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val startDate: String?,
    val endDate: String?,
    val location: String?,
    val color: Int
)