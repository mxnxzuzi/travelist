package ddwu.com.mobile.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ddwu.com.mobile.myapplication.data.model.Trip
import ddwu.com.mobile.myapplication.data.repository.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripViewModel(private val repository: TripRepository) : ViewModel() {

    val allTrips: LiveData<List<Trip>> = repository.getAllTrips()

    fun insertTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTrip(trip)

        }
    }
    fun deleteTrip(tripId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTrip(tripId)
        }
    }


}
