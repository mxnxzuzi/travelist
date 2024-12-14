package ddwu.com.mobile.myapplication.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ddwu.com.mobile.myapplication.data.model.Trip

@Dao
interface TripDao {

    @Insert
    suspend fun insertTrip(trip: Trip)

    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>

    @Query("DELETE FROM trip WHERE id = :tripId")
    suspend fun deleteTrip(tripId: Int)
}
