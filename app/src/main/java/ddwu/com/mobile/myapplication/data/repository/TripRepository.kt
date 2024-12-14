package ddwu.com.mobile.myapplication.data.repository

import androidx.lifecycle.LiveData
import ddwu.com.mobile.myapplication.data.dao.TripDao
import ddwu.com.mobile.myapplication.data.model.Trip

class TripRepository(private val tripDao: TripDao) {

    fun getAllTrips(): LiveData<List<Trip>> = tripDao.getAllTrips()

    suspend fun insertTrip(trip: Trip) {
        tripDao.insertTrip(trip)
    }
    suspend fun deleteTrip(tripId: Int) {
        tripDao.deleteTrip(tripId)
    }

}
